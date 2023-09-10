package shuttle.accessibility.usecase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.annotation.Single
import shuttle.apps.domain.model.AppId
import shuttle.coordinates.domain.model.CoordinatesResult
import shuttle.coordinates.domain.usecase.ObserveCurrentCoordinates
import shuttle.settings.domain.usecase.IsBlacklisted
import shuttle.stats.domain.usecase.StoreOpenStatsByCoordinates

@Single
class StartStoreIfNotBlacklistAndUpdateWidget(
    private val appScope: CoroutineScope,
    private val isBlacklisted: IsBlacklisted,
    observeCoordinates: ObserveCurrentCoordinates,
    private val storeOpenStatsByCoordinates: StoreOpenStatsByCoordinates,
    private val updateWidget: UpdateWidget
) {

    private val coordinatesState = observeCoordinates()
        .map(CoordinatesState::Ready)
        .stateIn(appScope, SharingStarted.Eagerly, CoordinatesState.NotReady)

    operator fun invoke(appId: AppId) {
        appScope.launch {
            if (isBlacklisted(appId).not()) {
                val coordinates = coordinatesState.awaitCoordinates()
                storeOpenStatsByCoordinates(appId, coordinates)
                updateWidget()
            }
        }
    }

    private suspend fun StateFlow<CoordinatesState>.awaitCoordinates(): CoordinatesResult {
        val value = value
        return if (value is CoordinatesState.Ready) value.coordinatesResult
        else (first { it is CoordinatesState.Ready } as CoordinatesState.Ready).coordinatesResult
    }

    private sealed interface CoordinatesState {

        data object NotReady : CoordinatesState
        data class Ready(val coordinatesResult: CoordinatesResult) : CoordinatesState
    }
}
