package shuttle.settings.domain.usecase

import shuttle.apps.domain.model.AppId
import shuttle.settings.domain.SettingsRepository

class RemoveFromBlacklist(
    private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke(appId: AppId) =
        settingsRepository.setBlacklisted(appId, blacklisted = false)
}
