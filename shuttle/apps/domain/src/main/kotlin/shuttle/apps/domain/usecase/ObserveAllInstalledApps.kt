package shuttle.apps.domain.usecase

import arrow.core.Either
import kotlinx.coroutines.flow.Flow
import shuttle.apps.domain.AppsRepository
import shuttle.apps.domain.error.GenericError
import shuttle.apps.domain.model.AppModel

class ObserveAllInstalledApps(
    private val repository: AppsRepository
) {

    operator fun invoke(): Flow<Either<GenericError, List<AppModel>>> =
        repository.observeAllInstalledApps()
}
