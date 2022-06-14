package shuttle.coordinates.domain.model

import com.soywiz.klock.DateTime

data class Coordinates(
    val location: GeoHash,
    val dateTime: DateTime
)

