package shuttle.coordinates.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import shuttle.database.datasource.LastLocationDataSource
import shuttle.database.model.DatabaseLatitude
import shuttle.database.model.DatabaseLongitude

class LocationBroadcastReceiver : BroadcastReceiver(), KoinComponent {

    private val lastLocationDataSource: LastLocationDataSource by inject()

    @Suppress("RedundantNullableReturnType")
    override fun onReceive(context: Context, intent: Intent?) {
        intent ?: return

        val locationAvailability: LocationAvailability? = LocationAvailability.extractLocationAvailability(intent)
        if (locationAvailability?.isLocationAvailable == false) {
            // TODO: should handle this?
        }

        val locationResult: LocationResult? = LocationResult.extractResult(intent)
        locationResult?.let(::saveLastLocationBlocking)
    }

    private fun saveLastLocationBlocking(result: LocationResult) {
        runBlocking {
            val lastLocation = result.lastLocation
            lastLocationDataSource.insert(
                DatabaseLatitude(lastLocation.latitude),
                DatabaseLongitude(lastLocation.longitude)
            )
        }
    }
}
