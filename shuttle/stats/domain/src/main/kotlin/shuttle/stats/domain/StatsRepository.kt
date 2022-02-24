package shuttle.stats.domain

import arrow.core.Either
import com.soywiz.klock.Time
import kotlinx.coroutines.flow.Flow
import shuttle.apps.domain.error.GenericError
import shuttle.apps.domain.model.AppId
import shuttle.apps.domain.model.AppModel
import shuttle.coordinates.domain.model.Location

interface StatsRepository {

    fun observeSuggestedApps(
        startLocation: Location,
        endLocation: Location,
        startTime: Time,
        endTime: Time
    ): Flow<Either<GenericError, List<AppModel>>>

    suspend fun incrementCounter(appId: AppId, location: Location, time: Time)
}
