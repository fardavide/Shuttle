package shuttle.stats.domain.usecase

import shuttle.stats.domain.model.Location
import shuttle.stats.domain.model.LocationRange

/**
 * Converts [Location] to [LocationRange], using meters as span
 */
class LocationToLocationRange(
    private val metersToLatitude: MetersToLatitude,
    private val metersToLongitude: MetersToLongitude
) {

    operator fun invoke(location: Location, metersSpan: Int): LocationRange {
        val metersOffset = metersSpan / 2
        val latitudeOffset = metersToLatitude(metersOffset)
        val longitudeOffset = metersToLongitude(metersOffset, location.latitude)
        return LocationRange(
            start = Location(
                latitude = location.latitude - latitudeOffset,
                longitude = location.longitude - longitudeOffset
            ),
            end = Location(
                latitude = location.latitude + latitudeOffset,
                longitude = location.longitude + longitudeOffset
            )
        )
    }
}
