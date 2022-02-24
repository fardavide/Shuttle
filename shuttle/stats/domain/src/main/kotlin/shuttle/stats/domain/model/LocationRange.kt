package shuttle.stats.domain.model

import shuttle.coordinates.domain.model.Location

data class LocationRange(
    val start: Location,
    val end: Location
)
