package shuttle.accessibility.usecase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import shuttle.apps.domain.model.AppId
import shuttle.coordinates.domain.model.Coordinates
import shuttle.coordinates.domain.usecase.ObserveCurrentCoordinates
import shuttle.settings.domain.usecase.IsBlacklisted
import shuttle.stats.domain.usecase.IncrementOpenCounterByCoordinates

class IncrementOpenCounterIfNotBlacklisted(
    private val appScope: CoroutineScope,
    private val incrementOpenCounterByCoordinates: IncrementOpenCounterByCoordinates,
    private val isBlacklisted: IsBlacklisted,
    observeCoordinates: ObserveCurrentCoordinates
) {

    private val coordinatesState = observeCoordinates()
        .map(CoordinatesState::Ready)
        .stateIn(appScope, SharingStarted.Eagerly, CoordinatesState.NotReady)

    operator fun invoke(appId: AppId) {
        appScope.launch {
            if (isBlacklisted(appId).not()) {
                val coordinates = coordinatesState.awaitCoordinates()
                incrementOpenCounterByCoordinates(appId, coordinates)
            }
        }
    }

    private suspend fun StateFlow<CoordinatesState>.awaitCoordinates(): Coordinates {
        val value = value
        return if (value is CoordinatesState.Ready) value.coordinates
        else (first { it is CoordinatesState.Ready } as CoordinatesState.Ready).coordinates
    }

    private sealed interface CoordinatesState {

        object NotReady : CoordinatesState
        data class Ready(val coordinates: Coordinates): CoordinatesState
    }
}
