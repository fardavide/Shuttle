package shuttle.accessibility

import android.content.ComponentName
import org.koin.core.qualifier.named
import org.koin.dsl.module
import shuttle.accessibility.usecase.IncrementOpenCounterIfNotBlacklisted
import shuttle.accessibility.usecase.IsLaunchCounterServiceEnabled
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
    factory {
        IsLaunchCounterServiceEnabled(
            accessibilityServiceComponentName = get(LaunchCounterServiceQualifier),
            contentResolver = get()
        )
    }
    factory(LaunchCounterServiceQualifier) { ComponentName(get(), LaunchCounterAccessibilityService::class.java) }
    single {
        UpdateWidget(
            appContext = get(),
            appScope = get()
        )
    }
}

val StartAppQualifier = named("Start app")
val LaunchCounterServiceQualifier = named("LaunchCounter Accessibility Service")
