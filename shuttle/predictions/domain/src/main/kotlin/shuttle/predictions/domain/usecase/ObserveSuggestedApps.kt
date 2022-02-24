package shuttle.predictions.domain.usecase

import arrow.core.Either
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import shuttle.apps.domain.error.GenericError
import shuttle.apps.domain.model.AppModel
import shuttle.coordinates.domain.usecase.ObserveCurrentCoordinates

class ObserveSuggestedApps(
    private val observeCurrentCoordinates: ObserveCurrentCoordinates,
    private val observeSuggestedApps: ObserveSuggestedAppsByCoordinates
) {

    operator fun invoke(): Flow<Either<GenericError, List<AppModel>>> =
        observeCurrentCoordinates().flatMapLatest { observeSuggestedApps(it) }
}

