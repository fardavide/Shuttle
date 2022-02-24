package shuttle.coordinates.data

import android.annotation.SuppressLint
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.soywiz.klock.DateTime
import com.soywiz.klock.Time
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import shuttle.coordinates.domain.CoordinatesRepository
import shuttle.coordinates.domain.model.Coordinates
import shuttle.coordinates.domain.model.Location
import kotlin.time.DurationUnit
import kotlin.time.toDuration

internal class CoordinatesRepositoryImpl(
    private val fusedLocationClient: FusedLocationProviderClient
) : CoordinatesRepository {

    @SuppressLint("MissingPermission")
    private val locationFlow: Flow<Location> = callbackFlow {
        val request = LocationRequest.create()
            .setFastestInterval(RefreshInterval.inWholeMilliseconds)
        val callback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val location = locationResult.lastLocation
                trySend(Location(location.latitude, location.longitude))
            }
        }
        fusedLocationClient.requestLocationUpdates(request, callback, Looper.getMainLooper())
        awaitClose { fusedLocationClient.removeLocationUpdates(callback) }
    }

    private val timeFlow: Flow<Time> = flow {
        while (currentCoroutineContext().isActive) {
            val time = DateTime.now().time
            emit(time)
            delay(RefreshInterval)
        }
    }

    override fun observeCurrentCoordinates(): Flow<Coordinates> =
        combine(locationFlow, timeFlow) { location, time -> Coordinates(location, time) }

    companion object {

        private val RefreshInterval = 1.toDuration(DurationUnit.MINUTES)
    }
}
