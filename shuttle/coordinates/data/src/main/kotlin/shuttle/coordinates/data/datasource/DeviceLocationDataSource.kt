package shuttle.coordinates.data.datasource

import android.annotation.SuppressLint
import android.location.Location
import arrow.core.Either
import arrow.core.ensure
import arrow.core.handleErrorWith
import com.soywiz.klock.DateTime
import kotlinx.coroutines.flow.first
import shuttle.coordinates.domain.error.LocationError
import shuttle.coordinates.domain.error.LocationError.ExpiredLocation
import kotlin.time.Duration

@SuppressLint("MissingPermission")
internal class DeviceLocationDataSource(
    private val dateTimeSource: DateTimeDataSource,
    private val locationClient: ShuttleLocationClient,
    private val freshLocationMinInterval: Duration,
    private val locationExpiration: Duration
) {

    suspend fun getLocation(): Either<LocationError, Location> {
        val lastLocation = locationClient.getLastLocation()
        val currentTime = dateTimeSource.flow.first()

        return inMinRefreshInterval(lastLocation, currentTime)
            .handleErrorWith { locationClient.getCurrentLocation() }
            .handleErrorWith { notExpired(lastLocation, currentTime) }
    }

    private fun inMinRefreshInterval(
        location: Either<LocationError, Location>,
        currentTime: DateTime
    ): Either<LocationError, Location> =
        location.ensure({ ExpiredLocation }, { isInMinRefreshInterval(it.time, currentTime) })

    private fun isInMinRefreshInterval(locationTime: Long, currentTime: DateTime): Boolean =
        currentTime.unixMillisLong - locationTime < freshLocationMinInterval.inWholeMilliseconds

    private fun notExpired(
        location: Either<LocationError, Location>,
        currentTime: DateTime
    ): Either<LocationError, Location> =
        location.ensure({ ExpiredLocation }, { isExpired(it.time, currentTime).not() })

    private fun isExpired(locationTime: Long, currentTime: DateTime): Boolean =
        currentTime.unixMillisLong - locationTime > locationExpiration.inWholeMilliseconds
}
