package shuttle.coordinates.domain.usecase

import arrow.core.Either
import org.koin.core.annotation.Factory
import shuttle.coordinates.domain.CoordinatesRepository
import shuttle.coordinates.domain.error.LocationError

@Factory
class RefreshLocation(
    private val coordinatesRepository: CoordinatesRepository
) {

    suspend operator fun invoke(): Either<LocationError, Unit> = coordinatesRepository.refreshLocation()
}
