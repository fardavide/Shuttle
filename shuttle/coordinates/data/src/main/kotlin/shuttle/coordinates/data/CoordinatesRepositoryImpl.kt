package shuttle.coordinates.data

import arrow.core.left
import arrow.core.right
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import shuttle.coordinates.data.datasource.DeviceLocationDataSource
import shuttle.coordinates.data.datasource.TimeDataSource
import shuttle.coordinates.data.mapper.GeoHashMapper
import shuttle.coordinates.data.worker.RefreshLocationWorker
import shuttle.coordinates.domain.CoordinatesRepository
import shuttle.coordinates.domain.error.LocationNotAvailable
import shuttle.coordinates.domain.model.CoordinatesResult
import shuttle.coordinates.domain.model.GeoHash
import shuttle.database.datasource.LastLocationDataSource
import shuttle.database.model.DatabaseGeoHash

internal class CoordinatesRepositoryImpl(
    appScope: CoroutineScope,
    private val deviceLocationDataSource: DeviceLocationDataSource,
    private val geoHashMapper: GeoHashMapper,
    private val lastLocationDataSource: LastLocationDataSource,
    private val refreshLocationWorkerScheduler: RefreshLocationWorker.Scheduler,
    timeDataSource: TimeDataSource
) : CoordinatesRepository {

    private val coordinatesSharedFlow: SharedFlow<CoordinatesResult> =
        combine(lastLocationDataSource.find(), timeDataSource.timeFlow) { lastLocation, time ->
            val location = if (lastLocation != null) {
                GeoHash(lastLocation.geoHash.value).right()
            } else {
                LocationNotAvailable.left()
            }
            CoordinatesResult(location, time)
        }
            .onStart { refreshLocationWorkerScheduler.schedule() }
            .shareIn(appScope, SharingStarted.Eagerly, replay = 1)

    override fun observeCurrentCoordinates(): Flow<CoordinatesResult> =
        coordinatesSharedFlow

    override suspend fun refreshLocation() {
        val location = deviceLocationDataSource.getLocation()
        val geoHash = geoHashMapper.toGeoHash(location)
        lastLocationDataSource.insert(DatabaseGeoHash(geoHash.value))
    }
}
