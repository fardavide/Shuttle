package shuttle.apps.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import shuttle.apps.domain.model.AppModel
import shuttle.apps.domain.repository.AppsRepository

@Factory
class ObserveAllInstalledApps(
    private val repository: AppsRepository
) {

    operator fun invoke(): Flow<List<AppModel>> = repository.observeAllInstalledApps()
}
