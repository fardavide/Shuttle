package shuttle.stats.data.repository

import arrow.core.Option
import korlibs.time.Date
import korlibs.time.Time
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import shuttle.apps.domain.model.AppId
import shuttle.apps.domain.model.SuggestedAppModel
import shuttle.coordinates.domain.model.GeoHash
import shuttle.settings.domain.usecase.ObserveUseExperimentalAppSorting
import shuttle.stats.domain.repository.StatsRepository

@Factory
internal class RealStatsRepository(
    private val countersStatsRepository: CountersStatsRepository,
    private val legacyStatsRepository: LegacyStatsRepository,
    observeUseExperimentalAppSorting: ObserveUseExperimentalAppSorting
) : StatsRepository {
    
    private val repository: Flow<StatsRepository> =
        observeUseExperimentalAppSorting().map { useExperimentalAppSorting ->
            when (useExperimentalAppSorting) {
                true -> countersStatsRepository
                false -> legacyStatsRepository
            }
        }

    override suspend fun deleteCountersFor(appId: AppId) {
        repository.first().deleteCountersFor(appId)
    }

    override fun observeSuggestedApps(
        location: Option<GeoHash>,
        date: Date,
        startTime: Time,
        endTime: Time,
        takeAtLeast: Int
    ): Flow<List<SuggestedAppModel>> = repository.flatMapLatest { repository ->
        repository.observeSuggestedApps(location, date, startTime, endTime, takeAtLeast)
    }

    override fun startDeleteOldStats() {
        countersStatsRepository.startDeleteOldStats()
        legacyStatsRepository.startDeleteOldStats()
    }

    override suspend fun storeOpenStats(
        appId: AppId,
        location: Option<GeoHash>,
        time: Time,
        date: Date
    ) {
        repository.first().storeOpenStats(appId, location, time, date)
    }
}
