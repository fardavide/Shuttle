package shuttle.settings.domain.usecase

import org.koin.core.annotation.Factory
import shuttle.settings.domain.SettingsRepository

@Factory
class HasEnabledAccessibilityService(
    private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke(): Boolean = settingsRepository.hasEnabledAccessibilityService()
}
