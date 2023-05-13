package shuttle.coordinates.domain.usecase

import korlibs.time.DateTime
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import shuttle.coordinates.domain.repository.CoordinatesRepository

@Factory
class ObserveCurrentDateTime(
    private val repository: CoordinatesRepository
) {

    operator fun invoke(): Flow<DateTime> = repository.observeCurrentDateTime()
}
