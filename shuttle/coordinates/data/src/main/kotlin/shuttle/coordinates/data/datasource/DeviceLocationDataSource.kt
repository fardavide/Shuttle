package shuttle.coordinates.data.datasource

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import arrow.core.Either
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import shuttle.coordinates.data.receiver.LocationBroadcastReceiver
import kotlin.time.Duration

@SuppressLint("MissingPermission")
internal class DeviceLocationDataSource(
    private val context: Context,
    private val fusedLocationClient: FusedLocationProviderClient,
    private val minRefreshInterval: Duration,
    private val defaultRefreshInterval: Duration,
    private val maxRefreshInterval: Duration
) {

    fun subscribe() {
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
