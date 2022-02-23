package shuttle.predictions.domain

import org.koin.dsl.module
import shuttle.predictions.domain.usecase.LocationToLocationRange
import shuttle.predictions.domain.usecase.MetersToLatitude
import shuttle.predictions.domain.usecase.MetersToLongitude
import shuttle.predictions.domain.usecase.ObserveSuggestedApps
import shuttle.predictions.domain.usecase.ObserveSuggestedAppsImpl

val predictionsDomainModule = module {

    factory { LocationToLocationRange(metersToLatitude = get(), metersToLongitude = get()) }
    factory { MetersToLatitude() }
    factory { MetersToLongitude() }
    factory<ObserveSuggestedApps> { ObserveSuggestedAppsImpl(statsRepository = get(), locationToLocationRange = get()) }

}
