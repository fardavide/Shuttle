package shuttle.settings.domain.usecase

import arrow.core.Option
import shuttle.apps.domain.model.AppId
import shuttle.settings.domain.SettingsRepository

class SetCurrentIconPack(
    private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke(iconPackId: Option<AppId>) {
        settingsRepository.setCurrentIconPack(iconPackId)
    }
}
