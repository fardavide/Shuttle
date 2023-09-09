package shuttle.settings.domain.usecase

import org.koin.core.annotation.Factory
import shuttle.settings.domain.repository.SettingsRepository

@Factory
class SetUseExperimentalAppSorting(
    private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke(useExperimentalAppSorting: Boolean) {
        settingsRepository.setUseExperimentalAppSorting(useExperimentalAppSorting)
    }
}
