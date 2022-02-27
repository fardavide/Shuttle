package shuttle.settings.domain.usecase

import kotlinx.coroutines.flow.Flow
import shuttle.apps.domain.usecase.ObserveAllInstalledApps
import shuttle.settings.domain.model.AppBlacklistSetting

class ObserveAppsBlacklistSettings(
    private val observeAllInstalledApps: ObserveAllInstalledApps
) {

    operator fun invoke(): Flow<List<AppBlacklistSetting>> =
        TODO("Not implemented")
}
