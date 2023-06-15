package shuttle.stats.data

import arrow.core.Option
import korlibs.time.Date
import korlibs.time.Time
import korlibs.time.plus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapLatest
import org.koin.core.annotation.Factory
import shuttle.apps.domain.model.AppId
import shuttle.apps.domain.model.AppModel
import shuttle.apps.domain.model.SuggestedAppModel
import shuttle.apps.domain.repository.AppsRepository
import shuttle.coordinates.domain.error.LocationNotAvailable
import shuttle.coordinates.domain.model.GeoHash
import shuttle.database.datasource.StatDataSource
import shuttle.database.model.DatabaseAppId
import shuttle.database.model.DatabaseGeoHash
import shuttle.stats.data.mapper.DatabaseDateAndTimeMapper
import shuttle.stats.data.usecase.SortAppStats
import shuttle.stats.data.worker.DeleteOldStatsScheduler
import shuttle.stats.domain.repository.StatsRepository

@Factory
internal class StatsRepositoryImpl(
    private val appsRepository: AppsRepository,
    private val databaseDateAndTimeMapper: DatabaseDateAndTimeMapper,
    private val deleteOldStatsScheduler: DeleteOldStatsScheduler,
    private val statDataSource: StatDataSource,
    private val sortAppStats: SortAppStats
) : StatsRepository {

    override suspend fun deleteCountersFor(appId: AppId) {
        statDataSource.deleteAllCountersFor(appId.toDatabaseAppId())
    }

    override fun observeSuggestedApps(
        location: Option<GeoHash>,
        date: Date,
        startTime: Time,
        endTime: Time,
        takeAtLeast: Int
    ): Flow<List<SuggestedAppModel>> = combine(
        appsRepository.observeNotBlacklistedApps(),
        statDataSource.findAllStats().mapLatest { stats ->
            val databaseGeoHash = location.toEither { LocationNotAvailable }.map { it.toDatabaseGeoHash() }
            val databaseDate = databaseDateAndTimeMapper.toDatabaseDate(date)
            val databaseStartTime = databaseDateAndTimeMapper.toDatabaseTime(startTime)
            val databaseEndTime = databaseDateAndTimeMapper.toDatabaseTime(endTime)
            sortAppStats(
                stats = stats,
                location = databaseGeoHash,
                date = databaseDate,
                startTime = databaseStartTime,
                endTime = databaseEndTime,
                takeAtLeast = takeAtLeast
            )
        }
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

    override fun startDeleteOldStats() {
        deleteOldStatsScheduler.schedule()
    }

    override suspend fun storeOpenStats(
        appId: AppId,
        location: Option<GeoHash>,
        time: Time,
        date: Date
    ) {
        val (databaseDate, databaseTime) = databaseDateAndTimeMapper.toDatabaseDateAndTime(dateTime = date + time)
        statDataSource.insertOpenStats(
            appId = appId.toDatabaseAppId(),
            geoHash = location.map { it.toDatabaseGeoHash() },
            date = databaseDate,
            time = databaseTime
        )
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
