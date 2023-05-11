package shuttle.coordinates.domain.model

import korlibs.time.DateTime

data class Coordinates(
    val location: GeoHash,
    val dateTime: DateTime
)

