package shuttle.settings.domain.usecase

import shuttle.settings.domain.SettingsRepository

class HasEnabledAccessibilityService(
    private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke(): Boolean =
        settingsRepository.hasEnabledAccessibilityService()
}
