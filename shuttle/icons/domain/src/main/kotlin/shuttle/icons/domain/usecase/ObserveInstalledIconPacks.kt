package shuttle.icons.domain.usecase

import kotlinx.coroutines.flow.Flow
import shuttle.apps.domain.model.AppModel
import shuttle.apps.domain.repository.AppsRepository

class ObserveInstalledIconPacks(
    private val appsRepository: AppsRepository
) {

    operator fun invoke(): Flow<List<AppModel>> = appsRepository.observeInstalledIconPacks()
}
