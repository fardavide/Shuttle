package shuttle.coordinates.domain

import org.koin.dsl.module
import shuttle.coordinates.domain.usecase.ObserveCurrentCoordinates

val coordinatesDomainModule = module {

    factory { ObserveCurrentCoordinates(repository = get()) }
}
