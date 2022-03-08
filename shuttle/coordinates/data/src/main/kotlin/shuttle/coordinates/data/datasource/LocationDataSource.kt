package shuttle.coordinates.data.datasource

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
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import shuttle.coordinates.data.mapper.GeoHashMapper
import shuttle.coordinates.domain.error.LocationNotAvailable
import kotlin.time.Duration

internal class LocationDataSource(
    private val backoffInterval: Duration,
    private val geoHashMapper: GeoHashMapper,
    private val fusedLocationClient: FusedLocationProviderClient,
    private val refreshInterval: Duration
) {

    @SuppressLint("MissingPermission")
    val locationFlow: Flow<Either<LocationNotAvailable, shuttle.coordinates.domain.model.GeoHash>> = callbackFlow {
        val request = LocationRequest.create()
            .setInterval(refreshInterval.inWholeMilliseconds)
            .setFastestInterval(backoffInterval.inWholeMilliseconds)
        val callback = object : LocationCallback() {

            override fun onLocationAvailability(locationAvailability: LocationAvailability) {
                if (locationAvailability.isLocationAvailable.not()) {
                    trySend(LocationNotAvailable.left())
                }
            }
            override fun onLocationResult(locationResult: LocationResult) {
                val location = locationResult.lastLocation
                trySend(geoHashMapper.toGeoHash(location).right())
            }
        }
        fusedLocationClient.requestLocationUpdates(request, callback, Looper.getMainLooper())
        awaitClose { fusedLocationClient.removeLocationUpdates(callback) }
    }
}
