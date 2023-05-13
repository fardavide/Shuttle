package shuttle.settings.domain.usecase

import org.koin.core.annotation.Factory
import shuttle.settings.domain.SettingsRepository

@Factory
class SetHasEnabledAccessibilityService(
    private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke() {
        settingsRepository.setHasEnabledAccessibilityService()
    }
}
