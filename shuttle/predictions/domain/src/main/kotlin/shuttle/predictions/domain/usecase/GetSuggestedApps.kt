package shuttle.predictions.domain.usecase

import arrow.core.Either
import arrow.core.computations.either
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import shuttle.apps.domain.error.GenericError
import shuttle.apps.domain.model.AppModel
import shuttle.apps.domain.usecase.GetAllInstalledApps
import shuttle.predictions.domain.model.Constraints
import shuttle.stats.domain.model.LocationCounter
import shuttle.stats.domain.model.StatsCounter
import shuttle.stats.domain.model.TimeCounter
import shuttle.stats.domain.usecase.GetAllAppsStats

class GetSuggestedApps(
    private val getAllInstalledApps: GetAllInstalledApps,
    private val getAllAppsStats: GetAllAppsStats,
    private val sortAppsByStats: SortAppsByStats
) {

    suspend operator fun invoke(constraints: Constraints): Either<GenericError, Collection<AppModel>> =
        coroutineScope {
            either {
                val allInstalledAppsAsyncEither = async { getAllInstalledApps() }
                val appsStatsAsyncEither = async { getAllAppsStats() }

                val allInstalledApps = allInstalledAppsAsyncEither.await().bind()
                val appsStats = appsStatsAsyncEither.await().bind()

                sortAppsByStats(allInstalledApps, appsStats, constraints)
            }
        }
}
