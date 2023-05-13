package shuttle.coordinates.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import shuttle.coordinates.domain.CoordinatesRepository
import shuttle.coordinates.domain.model.CoordinatesResult

@Factory
class ObserveCurrentCoordinates(
    private val repository: CoordinatesRepository
) {

    operator fun invoke(): Flow<CoordinatesResult> = repository.observeCurrentCoordinates()
}
