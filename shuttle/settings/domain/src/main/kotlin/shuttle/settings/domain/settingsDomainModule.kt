package shuttle.settings.domain

import org.koin.dsl.module
import shuttle.settings.domain.usecase.AddToBlacklist
import shuttle.settings.domain.usecase.IsBlacklisted
import shuttle.settings.domain.usecase.ObserveAppsBlacklistSettings
import shuttle.settings.domain.usecase.ObserveWidgetSettings
import shuttle.settings.domain.usecase.RemoveFromBlacklist
import shuttle.settings.domain.usecase.UpdateWidgetSettings

val settingsDomainModule = module {

    factory { AddToBlacklist(settingsRepository = get(), statRepository = get()) }
    factory { IsBlacklisted(settingsRepository = get()) }
    factory { ObserveAppsBlacklistSettings(appsRepository = get(), settingsRepository = get()) }
    factory { ObserveWidgetSettings(settingsRepository = get()) }
    factory { RemoveFromBlacklist(settingsRepository = get()) }
    factory { UpdateWidgetSettings(settingsRepository = get()) }
}
