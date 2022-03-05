package shuttle.settings.domain.usecase

import kotlinx.coroutines.flow.Flow
import shuttle.settings.domain.SettingsRepository
import shuttle.settings.domain.model.WidgetSettings

class ObserveWidgetSettings(
    private val settingsRepository: SettingsRepository
) {

    operator fun invoke(): Flow<WidgetSettings> =
        settingsRepository.observeWidgetSettings()
}
