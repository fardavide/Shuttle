package shuttle.settings.domain.usecase

import arrow.core.Option
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import shuttle.apps.domain.AppsRepository
import shuttle.apps.domain.model.AppModel
import shuttle.settings.domain.SettingsRepository

class ObserveCurrentIconPack(
    private val appsRepository: AppsRepository,
    private val settingsRepository: SettingsRepository
) {

    operator fun invoke(): Flow<Option<AppModel>> =
        settingsRepository.observeCurrentIconPack().map { appIdOption ->
            appIdOption.flatMap { appsRepository.getApp(it).orNone() }
        }
}
