package shuttle.predictions.domain.usecase

import arrow.core.Either
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import shuttle.apps.domain.error.GenericError
import shuttle.apps.domain.model.AppModel
import shuttle.predictions.domain.model.Constraints
import shuttle.stats.domain.model.AppStats
import shuttle.stats.domain.model.LocationCounter
import shuttle.stats.domain.model.StatsCounter
import shuttle.stats.domain.model.TimeCounter

class SortAppsByStats(
    private val computationDispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(
        apps: Collection<AppModel>,
        stats: Collection<AppStats>,
        constraints: Constraints
    ): List<AppModel> = withContext(computationDispatcher) {
        apps.sortedByDescending { appModel ->
            val appStats = stats.find { appModel.id == it.appId }
            listOfNotNull(
                appStats?.locationCounters?.findCountValueOrZero(constraints),
                appStats?.timeCounters?.findCountValueOrZero(constraints)
            ).sum()
        }
    }

    private fun Collection<StatsCounter>.findCountValueOrZero(constraints: Constraints): Int = find { item ->
        when (item) {
            is LocationCounter -> item.location == constraints.location
            is TimeCounter -> item.time == constraints.time
        }
    }?.count?.value ?: 0
}
