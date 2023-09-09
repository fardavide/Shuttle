package shuttle.stats.data.repository

import arrow.core.Nel
import arrow.core.Option
import arrow.core.toNonEmptyListOrNone
import korlibs.time.Date
import korlibs.time.Time
import korlibs.time.plus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.internal.NopCollector.emit
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onStart
import org.koin.core.annotation.Factory
import shuttle.apps.domain.model.AppId
import shuttle.apps.domain.model.SuggestedAppModel
import shuttle.apps.domain.repository.AppsRepository
import shuttle.coordinates.domain.error.LocationNotAvailable
import shuttle.coordinates.domain.model.GeoHash
import shuttle.database.datasource.StatDataSource
import shuttle.database.datasource.SuggestionCacheDataSource
import shuttle.database.model.DatabaseSuggestionCache
import shuttle.performance.SuggestionsTracer
import shuttle.stats.data.mapper.DatabaseDateAndTimeMapper
import shuttle.stats.data.mapper.toAppId
import shuttle.stats.data.mapper.toAppName
import shuttle.stats.data.mapper.toDatabaseAppId
import shuttle.stats.data.mapper.toDatabaseGeoHash
import shuttle.stats.data.mapper.toNotSuggestedAppModel
import shuttle.stats.data.usecase.SortAppStats
import shuttle.stats.data.worker.DeleteOldStatsScheduler
import shuttle.stats.domain.repository.StatsRepository

@Factory
internal class LegacyStatsRepository(
    private val appsRepository: AppsRepository,
    private val cacheDataSource: SuggestionCacheDataSource,
    private val databaseDateAndTimeMapper: DatabaseDateAndTimeMapper,
    private val deleteOldStatsScheduler: DeleteOldStatsScheduler,
    private val statDataSource: StatDataSource,
    private val sortAppStats: SortAppStats,
    private val tracer: SuggestionsTracer
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
    ): Flow<List<SuggestedAppModel>> = combineTransform(
        appsRepository.observeNotBlacklistedApps(),
        statDataSource.findAllStats().mapLatest { stats ->
            val databaseGeoHash = location.toEither { LocationNotAvailable }.map { it.toDatabaseGeoHash() }
            val databaseDate = databaseDateAndTimeMapper.toDatabaseDate(date)
            val databaseStartTime = databaseDateAndTimeMapper.toDatabaseTime(startTime)
            val databaseEndTime = databaseDateAndTimeMapper.toDatabaseTime(endTime)
            tracer.sort {
                sortAppStats(
                    stats = stats,
                    location = databaseGeoHash,
                    date = databaseDate,
                    startTime = databaseStartTime,
                    endTime = databaseEndTime,
                    takeAtLeast = takeAtLeast
                )
            }
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
        val suggestions = appsFromStats + allInstalledApp.map(::toNotSuggestedAppModel).shuffled()
        emit(suggestions)
        storeCache(suggestions)
    }.onStart { getCache().onSome { emit(it) } }

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

    private suspend fun getCache(): Option<Nel<SuggestedAppModel>> = cacheDataSource.findCachedSuggestions()
        .map { app -> SuggestedAppModel(app.id.toAppId(), app.name.toAppName(), isSuggested = app.isSuggested) }
        .toNonEmptyListOrNone()

    private suspend fun storeCache(suggestedApps: List<SuggestedAppModel>) {
        val cache = suggestedApps.mapIndexed { index, app ->
            DatabaseSuggestionCache(
                id = app.id.toDatabaseAppId(),
                position = index.toLong(),
                isSuggested = app.isSuggested
            )
        }
        cacheDataSource.insertSuggestionCache(cache)
    }
}

private fun <T> MutableList<T>.pop(predicate: (T) -> Boolean): T? {
    val index = indexOfFirst(predicate)
    return if (index > -1) removeAt(index)
    else null
}
