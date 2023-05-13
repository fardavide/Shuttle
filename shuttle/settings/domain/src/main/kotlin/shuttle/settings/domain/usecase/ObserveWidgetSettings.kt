package shuttle.settings.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import shuttle.settings.domain.SettingsRepository
import shuttle.settings.domain.model.WidgetSettings

@Factory
class ObserveWidgetSettings(
    private val settingsRepository: SettingsRepository
) {

    operator fun invoke(): Flow<WidgetSettings> = settingsRepository.observeWidgetSettings()
}
