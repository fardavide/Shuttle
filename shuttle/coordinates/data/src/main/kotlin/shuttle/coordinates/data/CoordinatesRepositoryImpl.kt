package shuttle.coordinates.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.shareIn
import shuttle.coordinates.data.datasource.LocationDataSource
import shuttle.coordinates.data.datasource.TimeDataSource
import shuttle.coordinates.domain.CoordinatesRepository
import shuttle.coordinates.domain.model.CoordinatesResult

internal class CoordinatesRepositoryImpl(
    appScope: CoroutineScope,
    locationDataSource: LocationDataSource,
    timeDataSource: TimeDataSource
) : CoordinatesRepository {

    private val coordinatesSharedFlow =
        combine(locationDataSource.locationFlow, timeDataSource.timeFlow) { location, time ->
            CoordinatesResult(location, time)
        }.shareIn(appScope, SharingStarted.Eagerly, replay = 1)

    override fun observeCurrentCoordinates(): Flow<CoordinatesResult> =
        coordinatesSharedFlow
}
