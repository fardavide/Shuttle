package shuttle.apps.domain

import arrow.core.Either
import kotlinx.coroutines.flow.Flow
import shuttle.apps.domain.error.GenericError
import shuttle.apps.domain.model.AppModel

interface AppsRepository {

    suspend fun getAllInstalledApps(): Either<GenericError, List<AppModel>>
    fun observeAllInstalledApps(): Flow<Either<GenericError, List<AppModel>>>
}
