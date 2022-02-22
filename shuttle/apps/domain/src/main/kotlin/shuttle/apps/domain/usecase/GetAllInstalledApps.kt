package shuttle.apps.domain.usecase

import arrow.core.Either
import shuttle.apps.domain.AppsRepository
import shuttle.apps.domain.error.GenericError
import shuttle.apps.domain.model.AppModel

class GetAllInstalledApps(
    private val repository: AppsRepository
) {

    suspend operator fun invoke(): Either<GenericError, List<AppModel>> =
        repository.getAllInstalledApps()
}
