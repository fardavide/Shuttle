package shuttle.settings.domain.usecase

import kotlinx.coroutines.flow.Flow
import shuttle.settings.domain.SettingsRepository

class ObservePrioritizeLocation(
    private val settingsRepository: SettingsRepository
) {

    operator fun invoke(): Flow<Boolean> = settingsRepository.observePrioritizeLocation()
}
