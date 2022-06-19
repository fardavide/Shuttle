package shuttle.coordinates.domain

import org.koin.dsl.module
import shuttle.coordinates.domain.usecase.ObserveCurrentCoordinates
import shuttle.coordinates.domain.usecase.ObserveCurrentDateTime
import shuttle.coordinates.domain.usecase.RefreshLocation

val coordinatesDomainModule = module {

    factory { ObserveCurrentCoordinates(repository = get()) }
    factory { ObserveCurrentDateTime(repository = get()) }
    factory { RefreshLocation(coordinatesRepository = get()) }
}
