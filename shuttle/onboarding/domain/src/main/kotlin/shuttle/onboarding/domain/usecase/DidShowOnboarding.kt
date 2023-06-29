package shuttle.onboarding.domain.usecase

import org.koin.core.annotation.Factory
import shuttle.settings.domain.repository.SettingsRepository

@Factory
class DidShowOnboarding(
    private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke(): Boolean = settingsRepository.didShowOnboarding()
}
