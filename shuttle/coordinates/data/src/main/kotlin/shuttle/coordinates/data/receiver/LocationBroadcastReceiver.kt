package shuttle.coordinates.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import arrow.core.left
import arrow.core.right
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationResult
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import shuttle.coordinates.data.datasource.LocationDataSource
import shuttle.coordinates.domain.error.LocationNotAvailable
import shuttle.coordinates.domain.model.Location

class LocationBroadcastReceiver : BroadcastReceiver(), KoinComponent {

    private val locationDataSource: LocationDataSource by inject()

    @Suppress("RedundantNullableReturnType")
    override fun onReceive(context: Context, intent: Intent?) {
        intent ?: return

        val locationAvailability: LocationAvailability? = LocationAvailability.extractLocationAvailability(intent)
        if (locationAvailability?.isLocationAvailable == false) {
            emitLocationNotAvailable()
        }

        val locationResult: LocationResult? = LocationResult.extractResult(intent)
        locationResult
            ?.let(::emitLocation) ?:
            emitLocationNotAvailable()
    }

    private fun emitLocation(result: LocationResult) {
        val lastLocation = result.lastLocation
        val location = Location(lastLocation.latitude, lastLocation.longitude)
        locationDataSource.locationMutableSharedFlow.tryEmit(location.right())
    }

    private fun emitLocationNotAvailable() {
        locationDataSource.locationMutableSharedFlow.tryEmit(LocationNotAvailable.left())
    }
}
