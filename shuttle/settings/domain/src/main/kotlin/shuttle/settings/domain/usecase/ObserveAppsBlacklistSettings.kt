package shuttle.settings.domain.usecase

import kotlinx.coroutines.flow.Flow
import shuttle.settings.domain.SettingsRepository
import shuttle.settings.domain.model.AppBlacklistSetting

class ObserveAppsBlacklistSettings(
    private val settingsRepository: SettingsRepository
) {

    operator fun invoke(): Flow<List<AppBlacklistSetting>> =
        settingsRepository.observeAppsBlacklistSettings()
}
