package shuttle.settings.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import org.koin.core.annotation.Factory
import shuttle.apps.domain.repository.AppsRepository
import shuttle.settings.domain.SettingsRepository
import shuttle.settings.domain.model.AppBlacklistSetting

@Factory
class ObserveAppsBlacklistSettings(
    private val appsRepository: AppsRepository,
    private val settingsRepository: SettingsRepository
) {

    operator fun invoke(): Flow<List<AppBlacklistSetting>> = combine(
        appsRepository.observeAllInstalledApps(),
        settingsRepository.observeAppsBlacklistSettings()
    ) { _, list -> list }
}
