package shuttle.settings.domain.usecase

import arrow.core.Option
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import shuttle.apps.domain.model.AppId
import shuttle.settings.domain.SettingsRepository

@Factory
class ObserveCurrentIconPack(
    private val settingsRepository: SettingsRepository
) {

    operator fun invoke(): Flow<Option<AppId>> = settingsRepository.observeCurrentIconPack()
}
