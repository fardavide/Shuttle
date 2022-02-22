package shuttle.predictions.domain.model

import com.soywiz.klock.Time
import shuttle.stats.domain.model.Location

data class Constraints(
    val location: Location,
    val time: Time
)
