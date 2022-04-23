package shuttle.settings.domain.usecase

import shuttle.settings.domain.SettingsRepository

class UpdateUseCurrentLocationOnly(
    private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke(useCurrentLocationOnly: Boolean) {
        settingsRepository.updateUseCurrentLocationOnly(useCurrentLocationOnly)
    }
}
