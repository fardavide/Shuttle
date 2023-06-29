package shuttle.settings.domain.usecase

import org.koin.core.annotation.Factory
import shuttle.settings.domain.model.WidgetSettings
import shuttle.settings.domain.repository.SettingsRepository

@Factory
class UpdateWidgetSettings(
    private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke(settings: WidgetSettings) {
        settingsRepository.updateWidgetSettings(settings)
    }
}
