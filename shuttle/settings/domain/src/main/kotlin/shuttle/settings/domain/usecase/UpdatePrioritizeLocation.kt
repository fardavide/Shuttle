package shuttle.settings.domain.usecase

import shuttle.settings.domain.SettingsRepository

class UpdatePrioritizeLocation(
    private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke(prioritizeLocation: Boolean) {
        settingsRepository.updatePrioritizeLocation(prioritizeLocation)
    }
}
