package shuttle.coordinates.domain

import kotlinx.coroutines.flow.Flow
import shuttle.coordinates.domain.model.CoordinatesResult

interface CoordinatesRepository {

    fun observeCurrentCoordinates(): Flow<CoordinatesResult>

    suspend fun refreshLocation()
}
