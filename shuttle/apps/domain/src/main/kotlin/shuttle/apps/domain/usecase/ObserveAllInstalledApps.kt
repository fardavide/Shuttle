package shuttle.apps.domain.usecase

import kotlinx.coroutines.flow.Flow
import shuttle.apps.domain.AppsRepository
import shuttle.apps.domain.model.AppModel

class ObserveAllInstalledApps(
    private val repository: AppsRepository
) {

    operator fun invoke(): Flow<List<AppModel>> =
        repository.observeAllInstalledApps()
}
