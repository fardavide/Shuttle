package shuttle.coordinates.data.datasource

import android.annotation.SuppressLint
import android.location.Location
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout
import shuttle.coordinates.domain.error.LocationError
import kotlin.coroutines.resume
import kotlin.time.Duration

internal class ShuttleLocationClient(
    private val freshLocationTimeout: Duration,
    private val fusedLocationClient: FusedLocationProviderClient
) {

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): Either<LocationError, Location> =
        fusedLocationClient.getCurrentLocation(
            LocationRequest.PRIORITY_HIGH_ACCURACY,
            CancellationTokenSource().token
        ).tryGetWithTimeout()

    @SuppressLint("MissingPermission")
    suspend fun getLastLocation(): Either<LocationError, Location> =
        fusedLocationClient.lastLocation.tryGet()

    private suspend fun Task<Location?>.tryGet(): Either<LocationError, Location> =
        suspendCancellableCoroutine { continuation ->
            addOnSuccessListener { location ->
                val either = location?.right() ?: LocationError.NoCachedLocation.left()
                continuation.resume(either)
            }
            addOnFailureListener { continuation.resume(LocationError.MissingPermissions.left()) }
            addOnCanceledListener(continuation::cancel)
        }

    private suspend fun Task<Location?>.tryGetWithTimeout(): Either<LocationError, Location> =
        try {
            withTimeout(freshLocationTimeout) {
                suspendCancellableCoroutine { continuation ->
                    addOnSuccessListener { location ->
                        val either = location?.right() ?: LocationError.NoCachedLocation.left()
                        continuation.resume(either)
                    }
                    addOnFailureListener { continuation.resume(LocationError.MissingPermissions.left()) }
                    addOnCanceledListener(continuation::cancel)
                }
            }
        } catch (ignored: TimeoutCancellationException) {
            LocationError.Timeout.left()
        }
}
