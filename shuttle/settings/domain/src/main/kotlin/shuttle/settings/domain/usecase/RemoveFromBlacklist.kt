package shuttle.settings.domain.usecase

import org.koin.core.annotation.Factory
import shuttle.apps.domain.model.AppId
import shuttle.settings.domain.repository.SettingsRepository

@Factory
class RemoveFromBlacklist(
    private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke(appId: AppId) = settingsRepository.setBlacklisted(appId, blacklisted = false)
}
