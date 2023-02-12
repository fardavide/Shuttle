package shuttle.coordinates.domain.usecase

import kotlinx.coroutines.flow.Flow
import shuttle.coordinates.domain.CoordinatesRepository
import shuttle.coordinates.domain.model.CoordinatesResult

class ObserveCurrentCoordinates(
    private val repository: CoordinatesRepository
) {

    operator fun invoke(): Flow<CoordinatesResult> = repository.observeCurrentCoordinates()
}
