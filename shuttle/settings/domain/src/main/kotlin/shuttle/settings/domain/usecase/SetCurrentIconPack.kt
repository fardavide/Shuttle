package shuttle.settings.domain.usecase

import arrow.core.Option
import org.koin.core.annotation.Factory
import shuttle.apps.domain.model.AppId
import shuttle.settings.domain.SettingsRepository

@Factory
class SetCurrentIconPack(
    private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke(iconPackId: Option<AppId>) {
        settingsRepository.setCurrentIconPack(iconPackId)
    }
}
