package shuttle.settings.domain.usecase

import org.koin.core.annotation.Factory
import shuttle.settings.domain.repository.SettingsRepository

@Factory
class SetIsDataCollectionEnabled(
    private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke(isEnabled: Boolean) {
        settingsRepository.setIsDataCollectionEnabled(isEnabled)
    }
}
