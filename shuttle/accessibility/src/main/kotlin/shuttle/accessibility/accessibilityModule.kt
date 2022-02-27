package shuttle.accessibility

import org.koin.dsl.module
import shuttle.accessibility.usecase.IncrementOpenCounterIfNotBlacklisted

val accessibilityModule = module {

    single {
        IncrementOpenCounterIfNotBlacklisted(
            appScope = get(),
            incrementOpenCounterByCoordinates = get(),
            isBlacklisted = get(),
            observeCoordinates = get()
        )
    }
}
