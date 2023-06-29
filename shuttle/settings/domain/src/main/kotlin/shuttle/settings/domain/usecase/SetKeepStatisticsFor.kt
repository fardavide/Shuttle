package shuttle.settings.domain.usecase

import org.koin.core.annotation.Factory
import shuttle.settings.domain.model.KeepStatisticsFor
import shuttle.settings.domain.repository.SettingsRepository

@Factory
class SetKeepStatisticsFor(
    private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke(keepStatisticsFor: KeepStatisticsFor) {
        settingsRepository.setKeepStatisticsFor(keepStatisticsFor)
    }
}
