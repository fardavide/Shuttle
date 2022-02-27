package shuttle.settings.domain

import org.koin.dsl.module
import shuttle.settings.domain.usecase.IsInBlacklist
import shuttle.settings.domain.usecase.ObserveAppsBlacklistSettings

val settingsDomainModule = module {

    factory { IsInBlacklist() }
    factory { ObserveAppsBlacklistSettings(observeAllInstalledApps = get()) }
}
