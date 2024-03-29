package shuttle.onboarding.domain.usecase

import org.koin.core.annotation.Factory
import shuttle.settings.domain.repository.SettingsRepository

@Factory
class SetOnboardingShown(
    private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke() {
        settingsRepository.setOnboardingShow()
    }
}
