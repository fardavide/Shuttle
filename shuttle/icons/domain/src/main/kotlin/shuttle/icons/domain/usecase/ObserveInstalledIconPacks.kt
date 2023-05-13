package shuttle.icons.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import shuttle.apps.domain.model.AppModel
import shuttle.apps.domain.repository.AppsRepository

@Factory
class ObserveInstalledIconPacks(
    private val appsRepository: AppsRepository
) {

    operator fun invoke(): Flow<List<AppModel>> = appsRepository.observeInstalledIconPacks()
}
