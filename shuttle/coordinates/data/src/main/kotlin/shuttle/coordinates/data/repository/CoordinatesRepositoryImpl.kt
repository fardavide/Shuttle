package shuttle.coordinates.data.repository

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import korlibs.time.DateTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import org.koin.core.annotation.Single
import shuttle.coordinates.data.datasource.DateTimeDataSource
import shuttle.coordinates.data.datasource.DeviceLocationDataSource
import shuttle.coordinates.data.mapper.GeoHashMapper
import shuttle.coordinates.data.worker.RefreshLocationScheduler
import shuttle.coordinates.domain.error.LocationError
import shuttle.coordinates.domain.error.LocationNotAvailable
import shuttle.coordinates.domain.model.CoordinatesResult
import shuttle.coordinates.domain.model.GeoHash
import shuttle.coordinates.domain.repository.CoordinatesRepository
import shuttle.database.datasource.LastLocationDataSource
import shuttle.database.model.DatabaseGeoHash
import shuttle.utils.kotlin.mapToUnit

@Single
internal class CoordinatesRepositoryImpl(
    appScope: CoroutineScope,
    private val deviceLocationDataSource: DeviceLocationDataSource,
    private val geoHashMapper: GeoHashMapper,
    private val lastLocationDataSource: LastLocationDataSource,
    private val refreshLocationWorkerScheduler: RefreshLocationScheduler,
    dateTimeDataSource: DateTimeDataSource
) : CoordinatesRepository {

    private val coordinatesSharedFlow: SharedFlow<CoordinatesResult> =
        combine(lastLocationDataSource.find(), dateTimeDataSource.flow) { lastLocation, dateTime ->
            val location = if (lastLocation != null) {
                GeoHash(lastLocation.geoHash.value).right()
            } else {
                LocationNotAvailable.left()
            }
            CoordinatesResult(location, dateTime)
        }
            .onStart { refreshLocationWorkerScheduler.schedule() }
            .shareIn(appScope, SharingStarted.Eagerly, replay = 1)

    override fun observeCurrentCoordinates(): Flow<CoordinatesResult> = coordinatesSharedFlow

    override fun observeCurrentDateTime(): Flow<DateTime> = coordinatesSharedFlow.map { it.dateTime }

    override suspend fun refreshLocation(): Either<LocationError, Unit> =
        deviceLocationDataSource.getLocation().onRight { location ->
            val geoHash = geoHashMapper.toGeoHash(location)
            lastLocationDataSource.insert(DatabaseGeoHash(geoHash.value))
        }.mapToUnit()
}
