package shuttle.coordinates.data.datasource

import android.annotation.SuppressLint
import android.location.Location
import arrow.core.Either
import arrow.core.ensure
import arrow.core.handleErrorWith
import arrow.core.left
import arrow.core.right
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import com.soywiz.klock.DateTime
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout
import shuttle.coordinates.domain.error.LocationError
import shuttle.coordinates.domain.error.LocationError.ExpiredLocation
import shuttle.coordinates.domain.error.LocationError.MissingPermissions
import shuttle.coordinates.domain.error.LocationError.NoCachedLocation
import kotlin.coroutines.resume
import kotlin.time.Duration

@SuppressLint("MissingPermission")
internal class DeviceLocationDataSource(
    private val fusedLocationClient: FusedLocationProviderClient,
    private val freshLocationTimeout: Duration,
    private val locationExpiration: Duration
) {

    suspend fun getLocation(): Either<LocationError, Location> {
        return fusedLocationClient.getCurrentLocation(
            LocationRequest.PRIORITY_HIGH_ACCURACY,
            CancellationTokenSource().token
        ).tryGetWithTimeout().handleErrorWith {
            notExpired(fusedLocationClient.lastLocation.tryGet())
        }
    }

    private fun notExpired(location: Either<LocationError, Location>): Either<LocationError, Location> =
        location.ensure({ ExpiredLocation }, { isExpired(it.time).not() })

    private fun isExpired(locationTime: Long): Boolean =
        DateTime.nowUnixLong() - locationTime > locationExpiration.inWholeMilliseconds

    private suspend fun Task<Location?>.tryGet(): Either<LocationError, Location> =
        suspendCancellableCoroutine { continuation ->
            addOnSuccessListener { location ->
                val either = location?.right() ?: NoCachedLocation.left()
                continuation.resume(either)
            }
            addOnFailureListener { continuation.resume(MissingPermissions.left()) }
            addOnCanceledListener(continuation::cancel)
        }

    private suspend fun Task<Location?>.tryGetWithTimeout(): Either<LocationError, Location> =
        try {
            withTimeout(freshLocationTimeout) {
                suspendCancellableCoroutine { continuation ->
                    addOnSuccessListener { location ->
                        val either = location?.right() ?: NoCachedLocation.left()
                        continuation.resume(either)
                    }
                    addOnFailureListener { continuation.resume(MissingPermissions.left()) }
                    addOnCanceledListener(continuation::cancel)
                }
            }
        } catch (ignored: TimeoutCancellationException) {
            LocationError.Timeout.left()
        }
}
