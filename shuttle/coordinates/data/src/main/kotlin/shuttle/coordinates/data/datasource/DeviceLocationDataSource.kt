package shuttle.coordinates.data.datasource

import android.annotation.SuppressLint
import android.location.Location
import arrow.core.Either
import arrow.core.ensure
import arrow.core.handleError
import arrow.core.left
import arrow.core.right
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import com.soywiz.klock.DateTime
import kotlinx.coroutines.suspendCancellableCoroutine
import shuttle.coordinates.data.datasource.LocationError.ExpiredLocation
import shuttle.coordinates.data.datasource.LocationError.NoCachedLocation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.time.Duration

@SuppressLint("MissingPermission")
internal class DeviceLocationDataSource(
    private val fusedLocationClient: FusedLocationProviderClient,
    private val locationExpiration: Duration
) {

    suspend fun getLocation(): Either<LocationError, Location> {
        val lastLocation = fusedLocationClient.lastLocation.tryGet()
        return notExpired(lastLocation).handleError {
            fusedLocationClient.getCurrentLocation(
                LocationRequest.PRIORITY_HIGH_ACCURACY,
                CancellationTokenSource().token
            ).get()
        }
    }

    private fun notExpired(location: Either<NoCachedLocation, Location>): Either<LocationError, Location> =
        location.ensure({ ExpiredLocation }, { isExpired(it.time).not() })

    private fun isExpired(locationTime: Long): Boolean =
        DateTime.nowUnixLong() - locationTime > locationExpiration.inWholeMilliseconds

    private suspend fun Task<Location?>.tryGet(): Either<NoCachedLocation, Location> =
        suspendCancellableCoroutine { continuation ->
            addOnSuccessListener { location ->
                val either = location?.right() ?: NoCachedLocation.left()
                continuation.resume(either)
            }
            addOnFailureListener(continuation::resumeWithException)
            addOnCanceledListener(continuation::cancel)
        }

    private suspend fun Task<Location>.get(): Location =
        suspendCancellableCoroutine { continuation ->
            addOnSuccessListener(continuation::resume)
            addOnFailureListener(continuation::resumeWithException)
            addOnCanceledListener(continuation::cancel)
        }
}

sealed interface LocationError {
    object NoCachedLocation: LocationError
    object ExpiredLocation: LocationError
}
