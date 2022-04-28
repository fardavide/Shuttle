package shuttle.stats.data.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import shuttle.apps.domain.model.AppId
import shuttle.database.model.DatabaseAppStat
import shuttle.stats.data.model.GroupedDatabaseAppStats

internal class SortAppStatsByCounts(
    private val computationDispatcher: CoroutineDispatcher,
    private val getSortingRatios: GetSortingRatios
) {

    suspend operator fun invoke(stats: Collection<DatabaseAppStat>): List<AppId> = withContext(computationDispatcher) {
        val groupingResult = group(stats)
        val sortingRatios = getSortingRatios(groupingResult)

        groupingResult.groupedByAppId
            .asSequence()
            .map {
                val value = it.second.sumOf { stat ->
                    when (stat) {
                        is DatabaseAppStat.ByLocation -> stat.count * sortingRatios.byLocation
                        is DatabaseAppStat.ByTime -> stat.count.toDouble() * sortingRatios.byTime
                    }
                }
                Triple(it.first, it.second, value)
            }
            .sortedByDescending { it.third }
            .filterNot { it.third < 1 }
            .map { AppId(it.first.value) }
            .toList()
    }

    private fun group(stats: Collection<DatabaseAppStat>): GroupedDatabaseAppStats {
        val groupedByAppId = stats.groupBy { it.appId }.toList()
        val (byTimeCount, byLocationCount) = run {
            var both = 0
            var timeOnly = 0
            var locationOnly = 0
            for (statsByAppId in groupedByAppId) {
                var hasTime = false
                var hasLocation = false
                for (stat in statsByAppId.second) {
                    when {
                        hasTime && hasLocation -> continue
                        stat is DatabaseAppStat.ByLocation -> hasLocation = true
                        stat is DatabaseAppStat.ByTime -> hasTime = true
                    }
                }
                when {
                    hasTime && hasLocation -> both++
                    hasTime -> timeOnly++
                    hasLocation -> locationOnly++
                    else -> throw AssertionError("This should not be possible")
                }
            }
            both + timeOnly to both + locationOnly
        }

        return GroupedDatabaseAppStats(
            byTimeCount = byTimeCount.toDouble(),
            byLocationCount = byLocationCount.toDouble(),
            groupedByAppId = groupedByAppId
        )
    }
}
