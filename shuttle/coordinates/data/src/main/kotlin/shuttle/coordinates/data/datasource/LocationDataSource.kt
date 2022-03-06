package shuttle.coordinates.data.datasource

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import arrow.core.Either
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import shuttle.coordinates.data.mapper.GeoHashMapper
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import shuttle.coordinates.data.receiver.LocationBroadcastReceiver
import shuttle.coordinates.data.model.GeoHas
import shuttle.coordinates.domain.error.LocationNotAvailable
import shuttle.coordinates.domain.model.GeoHash
import kotlin.time.Duration

@SuppressLint("MissingPermission")
internal class LocationDataSource(
    context: Context,
    fusedLocationClient: FusedLocationProviderClient,
    minRefreshInterval: Duration,
    defaultRefreshInterval: Duration,
    maxRefreshInterval: Duration
) {

    val locationFlow: Flow<Either<LocationNotAvailable, GeoHash>>
        get() = locationMutableSharedFlow.asSharedFlow()

    internal val locationMutableSharedFlow: MutableSharedFlow<Either<LocationNotAvailable, GeoHash>> =
        MutableSharedFlow(replay = 1)

    init {
        val request = LocationRequest.create()
            .setFastestInterval(minRefreshInterval.inWholeMilliseconds)
            .setInterval(defaultRefreshInterval.inWholeMilliseconds)
            .setMaxWaitTime(maxRefreshInterval.inWholeMilliseconds)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            Intent(context, LocationBroadcastReceiver::class.java),
            flags
        )
        fusedLocationClient.requestLocationUpdates(request, pendingIntent)
    }
}
