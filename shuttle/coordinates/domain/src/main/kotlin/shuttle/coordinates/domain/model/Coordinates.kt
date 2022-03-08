package shuttle.coordinates.domain.model

import com.soywiz.klock.Time

data class Coordinates(
    val location: GeoHash,
    val time: Time
)

