package shuttle.settings.domain

import org.koin.dsl.module
import shuttle.settings.domain.usecase.IsBlacklisted
import shuttle.settings.domain.usecase.ObserveAppsBlacklistSettings

val settingsDomainModule = module {

    factory { IsBlacklisted(settingsRepository = get()) }
    factory { ObserveAppsBlacklistSettings(observeAllInstalledApps = get()) }
}
