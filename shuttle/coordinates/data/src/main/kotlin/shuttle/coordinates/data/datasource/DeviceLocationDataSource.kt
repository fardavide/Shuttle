package shuttle.coordinates.data.datasource

import android.annotation.SuppressLint
import android.location.Location
import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.recover
import arrow.core.right
import korlibs.time.DateTime
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import shuttle.coordinates.domain.CoordinatesQualifier
import shuttle.coordinates.domain.error.LocationError
import shuttle.coordinates.domain.error.LocationError.ExpiredLocation
import shuttle.performance.LocationTracer
import kotlin.time.Duration

@Factory
@SuppressLint("MissingPermission")
internal class DeviceLocationDataSource(
    private val dateTimeSource: DateTimeDataSource,
    @Named(CoordinatesQualifier.Interval.Location.MinRefresh)
    private val freshLocationMinInterval: Duration,
    private val locationClient: ShuttleLocationClient,
    @Named(CoordinatesQualifier.Interval.Location.Expiration)
    private val locationExpiration: Duration,
    private val tracer: LocationTracer
) {

    suspend fun getLocation(): Either<LocationError, Location> {
        val lastLocation = tracer.last { locationClient.getLastLocation() }
        val currentTime = dateTimeSource.flow.first()

        return inMinRefreshInterval(lastLocation, currentTime)
            .recover { tracer.fresh { locationClient.getCurrentLocation().bind() } }
            .recover { notExpired(lastLocation, currentTime).bind() }
    }

    private fun inMinRefreshInterval(
        location: Either<LocationError, Location>,
        currentTime: DateTime
    ): Either<LocationError, Location> = location.flatMap { locationValue ->
        if (isInMinRefreshInterval(locationValue.time, currentTime)) locationValue.right()
        else ExpiredLocation.left()
    }

    private fun isInMinRefreshInterval(locationTime: Long, currentTime: DateTime): Boolean =
        currentTime.unixMillisLong - locationTime < freshLocationMinInterval.inWholeMilliseconds

    private fun notExpired(
        location: Either<LocationError, Location>,
        currentTime: DateTime
    ): Either<LocationError, Location> = location.flatMap { locationValue ->
        if (isExpired(locationValue.time, currentTime)) ExpiredLocation.left() else locationValue.right()
    }

    private fun isExpired(locationTime: Long, currentTime: DateTime): Boolean =
        currentTime.unixMillisLong - locationTime > locationExpiration.inWholeMilliseconds
}
