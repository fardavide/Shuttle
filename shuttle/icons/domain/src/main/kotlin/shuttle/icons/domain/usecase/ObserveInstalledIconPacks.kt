package shuttle.icons.domain.usecase

import kotlinx.coroutines.flow.Flow
import shuttle.apps.domain.AppsRepository
import shuttle.apps.domain.model.AppModel

class ObserveInstalledIconPacks(
    private val appsRepository: AppsRepository
) {

    operator fun invoke(): Flow<List<AppModel>> = appsRepository.observeInstalledIconPacks()
}
