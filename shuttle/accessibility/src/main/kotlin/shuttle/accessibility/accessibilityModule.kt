package shuttle.accessibility

import org.koin.dsl.module
import shuttle.accessibility.usecase.IncrementOpenCounter

val accessibilityModule = module {

    single {
        IncrementOpenCounter(
            incrementOpenCounterByCoordinates = get(),
            observeCoordinates = get(),
            appScope = get()
        )
    }
}
