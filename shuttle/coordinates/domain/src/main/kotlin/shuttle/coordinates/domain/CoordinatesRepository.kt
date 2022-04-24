package shuttle.coordinates.domain

import arrow.core.Either
import kotlinx.coroutines.flow.Flow
import shuttle.coordinates.domain.error.LocationError
import shuttle.coordinates.domain.model.CoordinatesResult

interface CoordinatesRepository {

    fun observeCurrentCoordinates(): Flow<CoordinatesResult>

    suspend fun refreshLocation(): Either<LocationError, Unit>
}
