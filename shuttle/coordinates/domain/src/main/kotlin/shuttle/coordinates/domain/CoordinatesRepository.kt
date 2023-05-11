package shuttle.coordinates.domain

import arrow.core.Either
import korlibs.time.DateTime
import kotlinx.coroutines.flow.Flow
import shuttle.coordinates.domain.error.LocationError
import shuttle.coordinates.domain.model.CoordinatesResult

interface CoordinatesRepository {

    fun observeCurrentCoordinates(): Flow<CoordinatesResult>

    fun observeCurrentDateTime(): Flow<DateTime>

    suspend fun refreshLocation(): Either<LocationError, Unit>

}
