package shuttle.stats.domain

import arrow.core.Option
import com.soywiz.klock.Date
import com.soywiz.klock.Time
import kotlinx.coroutines.flow.Flow
import shuttle.apps.domain.model.AppId
import shuttle.apps.domain.model.SuggestedAppModel
import shuttle.coordinates.domain.model.GeoHash

interface StatsRepository {

    suspend fun deleteCountersFor(appId: AppId)

    fun observeSuggestedAppsWithDate(
        location: Option<GeoHash>,
        startTime: Time,
        endTime: Time
    ): Flow<List<SuggestedAppModel>>

    fun startMigrationStatsToSingleTable()

    suspend fun storeOpenStats(appId: AppId, location: Option<GeoHash>, time: Time, date: Date)
}
