package shuttle.predictions.domain.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import shuttle.apps.domain.model.SuggestedAppModel
import shuttle.coordinates.domain.model.fold
import shuttle.coordinates.domain.usecase.ObserveCurrentCoordinates
import shuttle.predictions.domain.error.ObserveSuggestedAppsError

@Factory
class ObserveSuggestedApps(
    private val observeCurrentCoordinates: ObserveCurrentCoordinates,
    private val observeSuggestedApps: ObserveSuggestedAppsByCoordinates
) {

    operator fun invoke(takeAtLeast: Int): Flow<Either<ObserveSuggestedAppsError, List<SuggestedAppModel>>> =
        observeCurrentCoordinates().flatMapLatest { coordinatesResult ->
            coordinatesResult.fold(
                ifLeft = { flowOf(ObserveSuggestedAppsError.LocationNotAvailable.left()) },
                ifRight = { coordinates -> observeSuggestedApps(coordinates, takeAtLeast).map { it.right() } }
            )
        }
}

