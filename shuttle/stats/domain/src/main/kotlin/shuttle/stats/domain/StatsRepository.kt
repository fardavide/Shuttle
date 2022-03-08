package shuttle.stats.domain

import com.soywiz.klock.Time
import kotlinx.coroutines.flow.Flow
import shuttle.apps.domain.model.AppId
import shuttle.apps.domain.model.SuggestedAppModel
import shuttle.coordinates.domain.model.GeoHash

interface StatsRepository {

    fun observeSuggestedApps(
        location: GeoHash,
        startTime: Time,
        endTime: Time
    ): Flow<List<SuggestedAppModel>>

    suspend fun incrementCounter(appId: AppId, location: GeoHash?, time: Time)

    suspend fun deleteCountersFor(appId: AppId)
}
