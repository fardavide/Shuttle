package shuttle.onboarding.domain.usecase

import shuttle.settings.domain.SettingsRepository

class DidShowOnboarding(
    private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke(): Boolean = settingsRepository.didShowOnboarding()
}
