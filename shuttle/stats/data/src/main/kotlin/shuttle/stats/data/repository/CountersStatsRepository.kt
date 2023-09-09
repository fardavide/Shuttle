package shuttle.stats.data.repository

import arrow.core.Option
import korlibs.time.Date
import korlibs.time.Time
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import org.koin.core.annotation.Factory
import shuttle.apps.domain.model.AppId
import shuttle.apps.domain.model.SuggestedAppModel
import shuttle.apps.domain.repository.AppsRepository
import shuttle.coordinates.domain.model.GeoHash
import shuttle.database.datasource.CounterDataSource
import shuttle.database.model.DatabaseHour
import shuttle.stats.data.mapper.DatabaseDateAndTimeMapper
import shuttle.stats.data.mapper.toAppId
import shuttle.stats.data.mapper.toDatabaseAppId
import shuttle.stats.data.mapper.toDatabaseGeoHash
import shuttle.stats.data.mapper.toNotSuggestedAppModel
import shuttle.stats.data.mapper.toSuggestedAppModel
import shuttle.stats.domain.repository.StatsRepository

@Factory
internal class CountersStatsRepository(
    private val appsRepository: AppsRepository,
    private val counterDataSource: CounterDataSource,
    private val databaseDateAndTimeMapper: DatabaseDateAndTimeMapper
) : StatsRepository {

    override suspend fun deleteCountersFor(appId: AppId) {
        counterDataSource.deleteAllCountersFor(appId.toDatabaseAppId())
    }

    override fun observeSuggestedApps(
        location: Option<GeoHash>,
        date: Date,
        startTime: Time,
        endTime: Time,
        takeAtLeast: Int
    ): Flow<List<SuggestedAppModel>> {
        val geoHash = location.map { it.toDatabaseGeoHash() }
        val hour = run {
            val startHour = databaseDateAndTimeMapper.toDatabaseHour(startTime)
            val endHour = databaseDateAndTimeMapper.toDatabaseHour(endTime)
            DatabaseHour((startHour.hourOfTheDay + endHour.hourOfTheDay) / 2)
        }
        return combine(
            counterDataSource.findSortedApps(geoHash, hour),
            appsRepository.observeNotBlacklistedApps()
        ) { suggestedApps, allNotBlacklistedApps ->
            val suggested = suggestedApps.mapNotNull { databaseAppId ->
                allNotBlacklistedApps.find { it.id == databaseAppId.toAppId() }?.let(::toSuggestedAppModel)
            }
            val others = allNotBlacklistedApps.filterNot { appModel ->
                suggested.any { it.id == appModel.id }
            }.map(::toNotSuggestedAppModel)
            suggested + others
        }
    }

    override fun startDeleteOldStats() {
        // no-op
    }

    override suspend fun storeOpenStats(
        appId: AppId,
        location: Option<GeoHash>,
        time: Time,
        date: Date
    ) {
        counterDataSource.incrementCounter(
            appId = appId.toDatabaseAppId(),
            geoHash = location.map { it.toDatabaseGeoHash() },
            time = databaseDateAndTimeMapper.toDatabaseHour(time)
        )
    }
}
