package shuttle.accessibility

import android.content.ComponentName
import org.koin.core.qualifier.named
import org.koin.dsl.module
import shuttle.accessibility.service.LaunchCounterAccessibilityService
import shuttle.accessibility.usecase.IsLaunchCounterServiceEnabled
import shuttle.accessibility.usecase.StartRefreshLocationAndUpdateWidget
import shuttle.accessibility.usecase.StartStoreIfNotBlacklistAndUpdateWidget
import shuttle.accessibility.usecase.UpdateWidget

val accessibilityModule = module {

    factory {
        IsLaunchCounterServiceEnabled(
            accessibilityServiceComponentName = ComponentName(get(), LaunchCounterAccessibilityService::class.java),
            contentResolver = get()
        )
    }
    factory { StartRefreshLocationAndUpdateWidget(appScope = get(), refreshLocation = get(), updateWidget = get()) }
    single {
        StartStoreIfNotBlacklistAndUpdateWidget(
            appScope = get(),
            isBlacklisted = get(),
            observeCoordinates = get(),
            storeOpenStatsByCoordinates = get(),
            updateWidget = get()
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
