package shuttle.stats.data

import arrow.core.Either
import arrow.core.computations.either
import com.soywiz.klock.Time
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import shuttle.apps.domain.AppsRepository
import shuttle.apps.domain.error.GenericError
import shuttle.apps.domain.model.AppId
import shuttle.apps.domain.model.AppModel
import shuttle.coordinates.domain.model.Location
import shuttle.database.datasource.StatDataSource
import shuttle.database.model.DatabaseAppId
import shuttle.database.model.DatabaseAppStat
import shuttle.database.model.DatabaseLatitude
import shuttle.database.model.DatabaseLongitude
import shuttle.database.model.DatabaseTime
import shuttle.stats.domain.StatsRepository

class StatsRepositoryImpl(
    private val appsRepository: AppsRepository,
    private val statDataSource: StatDataSource
) : StatsRepository {

    override fun observeSuggestedApps(
        startLocation: Location,
        endLocation: Location,
        startTime: Time,
        endTime: Time
    ): Flow<Either<GenericError, List<AppModel>>> =
        statDataSource.findAllStats(
            startLatitude = startLocation.databaseLatitude(),
            endLatitude = endLocation.databaseLatitude(),
            startLongitude = endLocation.databaseLongitude(),
            endLongitude = endLocation.databaseLongitude(),
            startTime = startTime.toDatabaseTime(),
            endTime = endTime.toDatabaseTime()
        ).map { appStatsList ->
            val sortedAppsIds = appStatsList.groupBy { it.appId }.toList()
                .sortedByDescending { (appId, stats) ->
                    stats.sumOf { stat ->
                        when (stat) {
                            is DatabaseAppStat.ByLocation -> stat.count * 100_000
                            is DatabaseAppStat.ByTime -> stat.count
                        }
                    }
                }.map { it.first }
            either {
                val allInstalledApp = appsRepository.getAllInstalledApps()
                    .bind()
                    .toMutableList()
                // It's ok to have an app in stats, but from the installed list, as an app can be uninstalled
                val appsFromStats = sortedAppsIds.mapNotNull { databaseAppId ->
                    allInstalledApp.pop { it.id.toDatabaseAppId() == databaseAppId }
                }
                appsFromStats + allInstalledApp
            }
        }

    override suspend fun incrementCounter(appId: AppId, location: Location, time: Time) {
        statDataSource.incrementCounter(
            appId = appId.toDatabaseAppId(),
            latitude = location.databaseLatitude(),
            longitude = location.databaseLongitude(),
            time = time.toDatabaseTime()
        )
    }
}

private fun <T> MutableList<T>.pop(predicate: (T) -> Boolean): T? {
    val index = indexOfFirst(predicate)
    return if (index > -1) removeAt(index)
    else null
}

private fun AppId.toDatabaseAppId() = DatabaseAppId(value)
private fun Location.databaseLatitude() = DatabaseLatitude(latitude)
private fun Location.databaseLongitude() = DatabaseLongitude(longitude)
private fun Time.toDatabaseTime() = DatabaseTime(minute)
