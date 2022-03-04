package shuttle.accessibility

import org.koin.core.qualifier.named
import org.koin.dsl.module
import shuttle.accessibility.usecase.IncrementOpenCounterIfNotBlacklisted
import shuttle.accessibility.usecase.UpdateWidget

val accessibilityModule = module {

    single {
        IncrementOpenCounterIfNotBlacklisted(
            appScope = get(),
            incrementOpenCounterByCoordinates = get(),
            isBlacklisted = get(),
            observeCoordinates = get()
        )
    }
    single {
        UpdateWidget(
            appContext = get(),
            appScope = get()
        )
    }
}

val StartAppId = named("Start app")
