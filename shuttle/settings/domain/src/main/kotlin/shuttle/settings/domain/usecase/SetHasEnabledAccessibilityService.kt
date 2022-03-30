package shuttle.settings.domain.usecase

import shuttle.settings.domain.SettingsRepository

class SetHasEnabledAccessibilityService(
    private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke() {
        settingsRepository.setHasEnabledAccessibilityService()
    }
}
