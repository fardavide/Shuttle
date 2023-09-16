package shuttle.settings.domain.usecase

import org.koin.core.annotation.Factory
import shuttle.settings.domain.repository.SettingsRepository

@Factory
class ObserveDidShowConsents(
    private val settingsRepository: SettingsRepository
) {

    operator fun invoke() = settingsRepository.observeDidShowConsents()
}
