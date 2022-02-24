package shuttle.coordinates.domain

import kotlinx.coroutines.flow.Flow
import shuttle.coordinates.domain.model.Coordinates

interface CoordinatesRepository {

    fun observeCurrentCoordinates(): Flow<Coordinates>
}
