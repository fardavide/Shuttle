package shuttle.coordinates.data.datasource

import android.annotation.SuppressLint
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import com.soywiz.klock.DateTime
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.time.Duration

@SuppressLint("MissingPermission")
internal class DeviceLocationDataSource(
    private val fusedLocationClient: FusedLocationProviderClient,
    private val locationExpiration: Duration
) {

    suspend fun getLocation(): Location {
        val lastLocation = fusedLocationClient.lastLocation.get()
        return if (isExpired(lastLocation.time)) {
            fusedLocationClient.getCurrentLocation(
                LocationRequest.PRIORITY_HIGH_ACCURACY,
                CancellationTokenSource().token
            ).get()
        } else {
            lastLocation
        }
    }

    private fun isExpired(locationTime: Long): Boolean =
        DateTime.nowUnixLong() - locationTime > locationExpiration.inWholeMilliseconds

    private suspend fun Task<Location>.get() = suspendCancellableCoroutine<Location> { continuation ->
        addOnSuccessListener(continuation::resume)
        addOnFailureListener(continuation::resumeWithException)
        addOnCanceledListener(continuation::cancel)
    }

}
