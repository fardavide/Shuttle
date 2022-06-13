package shuttle.accessibility

import android.content.ComponentName
import org.koin.core.qualifier.named
import org.koin.dsl.module
import shuttle.accessibility.usecase.IsLaunchCounterServiceEnabled
import shuttle.accessibility.usecase.StoreOpenStatsIfNotBlacklisted
import shuttle.accessibility.usecase.UpdateWidget

val accessibilityModule = module {

    factory {
        IsLaunchCounterServiceEnabled(
            accessibilityServiceComponentName = ComponentName(get(), LaunchCounterAccessibilityService::class.java),
            contentResolver = get()
        )
    }
    single {
        StoreOpenStatsIfNotBlacklisted(
            appScope = get(),
            isBlacklisted = get(),
            observeCoordinates = get(),
            storeOpenStatsByCoordinates = get()
        )
    }
    single {
        UpdateWidget(
            appContext = get(),
            appScope = get()
        )
    }
}

val StartAppQualifier = named("Start app")
