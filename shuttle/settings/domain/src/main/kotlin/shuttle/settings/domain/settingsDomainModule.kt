package shuttle.settings.domain

import org.koin.dsl.module
import shuttle.settings.domain.usecase.AddToBlacklist
import shuttle.settings.domain.usecase.GetPrioritizeLocation
import shuttle.settings.domain.usecase.HasEnabledAccessibilityService
import shuttle.settings.domain.usecase.IsBlacklisted
import shuttle.settings.domain.usecase.ObserveAppsBlacklistSettings
import shuttle.settings.domain.usecase.ObserveCurrentIconPack
import shuttle.settings.domain.usecase.ObservePrioritizeLocation
import shuttle.settings.domain.usecase.ObserveWidgetSettings
import shuttle.settings.domain.usecase.RemoveFromBlacklist
import shuttle.settings.domain.usecase.ResetOnboardingShown
import shuttle.settings.domain.usecase.SearchAppsBlacklistSettings
import shuttle.settings.domain.usecase.SetCurrentIconPack
import shuttle.settings.domain.usecase.SetHasEnabledAccessibilityService
import shuttle.settings.domain.usecase.UpdatePrioritizeLocation
import shuttle.settings.domain.usecase.UpdateWidgetSettings

val settingsDomainModule = module {

    factory { AddToBlacklist(settingsRepository = get(), statRepository = get()) }
    factory { GetPrioritizeLocation(observePrioritizeLocation = get()) }
    factory { HasEnabledAccessibilityService(settingsRepository = get()) }
    factory { IsBlacklisted(settingsRepository = get()) }
    factory { ObserveAppsBlacklistSettings(appsRepository = get(), settingsRepository = get()) }
    factory { ObserveCurrentIconPack(settingsRepository = get()) }
    factory { ObservePrioritizeLocation(settingsRepository = get()) }
    factory { ObserveWidgetSettings(settingsRepository = get()) }
    factory { RemoveFromBlacklist(settingsRepository = get()) }
    factory { ResetOnboardingShown(settingsRepository = get()) }
    factory { SearchAppsBlacklistSettings(observeAppsBlacklistSettings = get()) }
    factory { SetCurrentIconPack(settingsRepository = get()) }
    factory { SetHasEnabledAccessibilityService(settingsRepository = get()) }
    factory { UpdatePrioritizeLocation(settingsRepository = get()) }
    factory { UpdateWidgetSettings(settingsRepository = get()) }
}
