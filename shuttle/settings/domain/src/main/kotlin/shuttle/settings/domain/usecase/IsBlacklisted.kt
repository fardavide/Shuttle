package shuttle.settings.domain.usecase

import shuttle.apps.domain.model.AppId
import shuttle.settings.domain.SettingsRepository

class IsBlacklisted(
    private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke(appId: AppId): Boolean =
        settingsRepository.isBlacklisted(appId)
}
