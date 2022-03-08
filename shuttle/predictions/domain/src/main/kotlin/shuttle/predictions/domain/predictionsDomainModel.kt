package shuttle.predictions.domain

import org.koin.dsl.module
import shuttle.predictions.domain.usecase.ObserveSuggestedApps
import shuttle.predictions.domain.usecase.ObserveSuggestedAppsByCoordinates
import shuttle.predictions.domain.usecase.ObserveSuggestedAppsByCoordinatesImpl
import shuttle.predictions.domain.usecase.TimeToTimeRange

val predictionsDomainModule = module {

    factory { ObserveSuggestedApps(observeCurrentCoordinates = get(), observeSuggestedApps = get()) }
    factory<ObserveSuggestedAppsByCoordinates> {
        ObserveSuggestedAppsByCoordinatesImpl(
            statsRepository = get(),
            timeToTimeRange = get()
        )
    }
    factory { TimeToTimeRange() }
}
