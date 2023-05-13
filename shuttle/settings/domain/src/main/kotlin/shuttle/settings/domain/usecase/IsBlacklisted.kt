package shuttle.settings.domain.usecase

import org.koin.core.annotation.Factory
import shuttle.apps.domain.model.AppId
import shuttle.settings.domain.SettingsRepository

@Factory
class IsBlacklisted(
    private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke(appId: AppId): Boolean = settingsRepository.isBlacklisted(appId)
}
