package shuttle.coordinates.data

import arrow.core.left
import arrow.core.right
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import shuttle.coordinates.data.datasource.DeviceLocationDataSource
import shuttle.coordinates.data.datasource.TimeDataSource
import shuttle.coordinates.domain.CoordinatesRepository
import shuttle.coordinates.domain.error.LocationNotAvailable
import shuttle.coordinates.domain.model.CoordinatesResult
import shuttle.coordinates.domain.model.Location
import shuttle.database.datasource.LastLocationDataSource

internal class CoordinatesRepositoryImpl(
    appScope: CoroutineScope,
    private val deviceLocationDataSource: DeviceLocationDataSource,
    lastLocationDataSource: LastLocationDataSource,
    timeDataSource: TimeDataSource
) : CoordinatesRepository {

    private val coordinatesSharedFlow =
        combine(lastLocationDataSource.find(), timeDataSource.timeFlow) { lastLocation, time ->
            val location = if (lastLocation != null) {
                Location(latitude = lastLocation.latitude.value, longitude = lastLocation.longitude.value).right()
            } else {
                LocationNotAvailable.left()
            }
            CoordinatesResult(location, time)
        }
            .onStart { deviceLocationDataSource.subscribe() }
            .shareIn(appScope, SharingStarted.Eagerly, replay = 1)

    override fun observeCurrentCoordinates(): Flow<CoordinatesResult> =
        coordinatesSharedFlow
}
