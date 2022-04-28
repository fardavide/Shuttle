package shuttle.stats.data

import com.soywiz.klock.Time
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import shuttle.apps.domain.AppsRepository
import shuttle.apps.domain.model.AppId
import shuttle.apps.domain.model.AppModel
import shuttle.apps.domain.model.SuggestedAppModel
import shuttle.coordinates.domain.model.GeoHash
import shuttle.database.datasource.StatDataSource
import shuttle.database.model.DatabaseAppId
import shuttle.database.model.DatabaseGeoHash
import shuttle.database.model.DatabaseTime
import shuttle.stats.data.usecase.SortAppStatsByCounts
import shuttle.stats.domain.StatsRepository

internal class StatsRepositoryImpl(
    private val appsRepository: AppsRepository,
    private val statDataSource: StatDataSource,
    private val sortAppStatsByCounts: SortAppStatsByCounts,
) : StatsRepository {

    override fun observeSuggestedApps(
        location: GeoHash,
        startTime: Time,
        endTime: Time
    ): Flow<List<SuggestedAppModel>> =
        combine(
            appsRepository.observeNotBlacklistedApps(),
            statDataSource.findAllStats(
                geoHash = location.toDatabaseGeoHash(),
                startTime = startTime.toDatabaseTime(),
                endTime = endTime.toDatabaseTime()
            ).map { sortAppStatsByCounts(it) }
        ) { installedAppEither, sortedAppsIds ->
            val allInstalledApp = installedAppEither
                .toMutableList()

            // It's ok to have an app in stats, but from the installed list, as an app can be uninstalled
            val appsFromStats = sortedAppsIds.mapNotNull { appId ->
                allInstalledApp.pop { it.id == appId }?.let { installedApp ->
                    SuggestedAppModel(installedApp.id, installedApp.name, isSuggested = true)
                }
            }
            appsFromStats + allInstalledApp.map(::toNotSuggestedAppModel).shuffled()
        }

    override suspend fun incrementCounter(appId: AppId, location: GeoHash?, time: Time) {
        statDataSource.incrementCounter(
            appId = appId.toDatabaseAppId(),
            geoHash = location?.toDatabaseGeoHash(),
            time = time.toDatabaseTimeAdjusted()
        )
    }

    override suspend fun deleteCountersFor(appId: AppId) {
        statDataSource.deleteAllCountersFor(appId.toDatabaseAppId())
    }
}

private fun <T> MutableList<T>.pop(predicate: (T) -> Boolean): T? {
    val index = indexOfFirst(predicate)
    return if (index > -1) removeAt(index)
    else null
}

private fun toNotSuggestedAppModel(app: AppModel) = SuggestedAppModel(
    app.id,
    app.name,
    isSuggested = false
)

private fun AppId.toDatabaseAppId() = DatabaseAppId(value)
private fun GeoHash.toDatabaseGeoHash() = DatabaseGeoHash(value)
private fun Time.toDatabaseTime() = DatabaseTime(hour * 60 + minute)
private fun Time.toDatabaseTimeAdjusted() = DatabaseTime(hourAdjusted * 60 + minute)
