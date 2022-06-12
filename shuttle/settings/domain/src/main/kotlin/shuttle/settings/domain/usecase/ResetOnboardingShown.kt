package shuttle.settings.domain.usecase

import shuttle.settings.domain.SettingsRepository

class ResetOnboardingShown(
    private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke() {
        settingsRepository.resetOnboardingShown()
    }
}
