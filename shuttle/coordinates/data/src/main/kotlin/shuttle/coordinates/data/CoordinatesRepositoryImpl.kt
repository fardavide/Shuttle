package shuttle.coordinates.data

import android.annotation.SuppressLint
import android.os.Looper
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.soywiz.klock.DateTime
import com.soywiz.klock.Time
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.isActive
import shuttle.coordinates.domain.CoordinatesRepository
import shuttle.coordinates.domain.error.LocationNotAvailable
import shuttle.coordinates.domain.model.CoordinatesResult
import shuttle.coordinates.domain.model.Location
import kotlin.time.DurationUnit
import kotlin.time.toDuration

internal class CoordinatesRepositoryImpl(
    private val fusedLocationClient: FusedLocationProviderClient,
    appScope: CoroutineScope
) : CoordinatesRepository {

    @SuppressLint("MissingPermission")
    private val locationFlow: Flow<Either<LocationNotAvailable, Location>> = callbackFlow {
        val request = LocationRequest.create()
            .setInterval(LocationRefreshInterval.inWholeMilliseconds)
            .setFastestInterval(LocationBackoffInterval.inWholeMilliseconds)
        val callback = object : LocationCallback() {

            override fun onLocationAvailability(locationAvailability: LocationAvailability) {
                if (locationAvailability.isLocationAvailable.not()) {
                    trySend(LocationNotAvailable.left())
                }
            }
            override fun onLocationResult(locationResult: LocationResult) {
                val location = locationResult.lastLocation
                trySend(Location(location.latitude, location.longitude).right())
            }
        }
        fusedLocationClient.requestLocationUpdates(request, callback, Looper.getMainLooper())
        awaitClose { fusedLocationClient.removeLocationUpdates(callback) }
    }

    private val timeFlow: Flow<Time> = flow {
        while (currentCoroutineContext().isActive) {
            val time = DateTime.now().time
            emit(time)
            delay(TimeRefreshInterval)
        }
    }

    private val coordinatesSharedFlow =
        combine(locationFlow, timeFlow) { location, time -> CoordinatesResult(location, time) }
            .shareIn(appScope, SharingStarted.Eagerly)

    override fun observeCurrentCoordinates(): Flow<CoordinatesResult> =
        coordinatesSharedFlow

    companion object {

        private val LocationRefreshInterval = 10.toDuration(DurationUnit.MINUTES)
        private val LocationBackoffInterval = 10.toDuration(DurationUnit.SECONDS)
        private val TimeRefreshInterval = 1.toDuration(DurationUnit.MINUTES)
    }
}
