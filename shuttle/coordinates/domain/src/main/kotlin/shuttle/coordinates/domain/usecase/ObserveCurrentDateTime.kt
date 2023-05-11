package shuttle.coordinates.domain.usecase

import korlibs.time.DateTime
import kotlinx.coroutines.flow.Flow
import shuttle.coordinates.domain.CoordinatesRepository

class ObserveCurrentDateTime(
    private val repository: CoordinatesRepository
) {

    operator fun invoke(): Flow<DateTime> = repository.observeCurrentDateTime()
}
