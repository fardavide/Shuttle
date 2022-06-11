package shuttle.onboarding.domain.usecase

import shuttle.settings.domain.SettingsRepository

class SetOnboardingShown(
    private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke() {
        settingsRepository.setOnboardingShow()
    }
}
