package shuttle.stats.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import shuttle.apps.domain.model.AppId
import shuttle.database.model.DatabaseAppId
import shuttle.database.model.DatabaseAppStat
import kotlin.math.pow

class SortAppStatsByCounts(
    private val computationDispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(stats: Collection<DatabaseAppStat>): List<AppId> = withContext(computationDispatcher) {
        val groupingResult = group(stats)
        val ratio = (groupingResult.byTimeCount / groupingResult.byLocationCount).pow(4)

        groupingResult.groupedByAppId
            .asSequence()
            .map {
                val value = it.second.sumOf { stat ->
                    when (stat) {
                        is DatabaseAppStat.ByLocation -> stat.count * ratio
                        is DatabaseAppStat.ByTime -> stat.count.toDouble()
                    }
                }
                Triple(it.first, it.second, value)
            }
            .sortedByDescending { it.third }
            .filterNot { it.third < 1 }
            .map { AppId(it.first.value) }
            .toList()
    }

    private fun group(stats: Collection<DatabaseAppStat>): GroupingResult {
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

        return GroupingResult(
            byTimeCount = byTimeCount.toDouble(),
            byLocationCount = byLocationCount.toDouble(),
            groupedByAppId = groupedByAppId
        )
    }

    private data class GroupingResult(
        val byTimeCount: Double,
        val byLocationCount: Double,
        val groupedByAppId: List<Pair<DatabaseAppId, List<DatabaseAppStat>>>
    )
}
