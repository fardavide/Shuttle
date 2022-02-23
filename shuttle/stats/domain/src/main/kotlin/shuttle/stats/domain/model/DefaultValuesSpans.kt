package shuttle.stats.domain.model

import com.soywiz.klock.TimeSpan
import com.soywiz.klock.minutes

/**
 * We need some ranges in order to query [AppStats] with some flexibility.
 * For example, if now is 4:30pm, we want to query data also for 4:20pm or 4:35pm.
 * Here we define the spans for these values
 */
object DefaultValuesSpans {

    val Location = 200 // meters

    val Time: TimeSpan = 30.minutes
}
