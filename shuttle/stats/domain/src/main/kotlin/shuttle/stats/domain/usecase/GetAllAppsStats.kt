package shuttle.stats.domain.usecase

import arrow.core.Either
import shuttle.apps.domain.error.GenericError
import shuttle.stats.domain.model.AppStats

class GetAllAppsStats {

    operator fun invoke(): Either<GenericError, Collection<AppStats>> =
        TODO("Not implemented")
}
