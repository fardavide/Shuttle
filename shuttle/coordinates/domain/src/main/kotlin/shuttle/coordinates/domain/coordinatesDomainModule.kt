package shuttle.coordinates.domain

import org.koin.dsl.module
import shuttle.coordinates.domain.usecase.ObserveCurrentCoordinates
import shuttle.coordinates.domain.usecase.ObserveCurrentDateTime

val coordinatesDomainModule = module {

    factory { ObserveCurrentCoordinates(repository = get()) }
    factory { ObserveCurrentDateTime(repository = get()) }
}
