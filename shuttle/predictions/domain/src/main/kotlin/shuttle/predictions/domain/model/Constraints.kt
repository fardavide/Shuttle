package shuttle.predictions.domain.model

import com.soywiz.klock.Time
import shuttle.stats.domain.model.AppStats
import shuttle.stats.domain.model.Location

/**
 * A set of parameters for query the [AppStats]
 */
data class Constraints(
    val location: Location,
    val time: Time
)
