package shuttle.coordinates.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import shuttle.coordinates.domain.model.CoordinatesResult
import shuttle.coordinates.domain.repository.CoordinatesRepository

@Factory
class ObserveCurrentCoordinates(
    private val repository: CoordinatesRepository
) {

    operator fun invoke(): Flow<CoordinatesResult> = repository.observeCurrentCoordinates()
}
