package shuttle.stats.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import shuttle.apps.domain.model.AppId
import shuttle.database.model.DatabaseAppId
import shuttle.database.model.DatabaseAppStat
import shuttle.settings.domain.usecase.GetUseCurrentLocationOnly
import kotlin.math.pow

class SortAppStatsByCounts(
    private val computationDispatcher: CoroutineDispatcher,
    private val getUseCurrentLocationOnly: GetUseCurrentLocationOnly
) {

    suspend operator fun invoke(stats: Collection<DatabaseAppStat>): List<AppId> = withContext(computationDispatcher) {
        val useCurrentLocationOnly = getUseCurrentLocationOnly()
        val groupingResult = group(stats)

        val locationPartialRatio = groupingResult.byTimeCount / groupingResult.byLocationCount
        val (locationRatio, timeRatio) =
            if (useCurrentLocationOnly) locationPartialRatio.pow(99) to 0.5
            else locationPartialRatio.pow(4) to 1.0

        groupingResult.groupedByAppId
            .asSequence()
            .map {
                val value = it.second.sumOf { stat ->
                    when (stat) {
                        is DatabaseAppStat.ByLocation -> stat.count * locationRatio
                        is DatabaseAppStat.ByTime -> stat.count.toDouble() * timeRatio
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
