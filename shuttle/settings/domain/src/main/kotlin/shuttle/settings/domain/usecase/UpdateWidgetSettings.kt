package shuttle.settings.domain.usecase

import shuttle.settings.domain.SettingsRepository
import shuttle.settings.domain.model.WidgetSettings

class UpdateWidgetSettings(
    private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke(settings: WidgetSettings) {
        settingsRepository.updateWidgetSettings(settings)
    }
}
