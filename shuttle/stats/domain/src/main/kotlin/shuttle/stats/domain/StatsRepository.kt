package shuttle.stats.domain

import arrow.core.Option
import korlibs.time.Date
import korlibs.time.Time
import kotlinx.coroutines.flow.Flow
import shuttle.apps.domain.model.AppId
import shuttle.apps.domain.model.SuggestedAppModel
import shuttle.coordinates.domain.model.GeoHash

interface StatsRepository {

    suspend fun deleteCountersFor(appId: AppId)

    fun observeSuggestedApps(
        location: Option<GeoHash>,
        date: Date,
        startTime: Time,
        endTime: Time
    ): Flow<List<SuggestedAppModel>>

    fun startDeleteOldStats()

    suspend fun storeOpenStats(
        appId: AppId,
        location: Option<GeoHash>,
        time: Time,
        date: Date
    )
}
