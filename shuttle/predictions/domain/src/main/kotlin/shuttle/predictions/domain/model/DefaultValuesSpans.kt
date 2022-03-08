package shuttle.predictions.domain.model

import com.soywiz.klock.TimeSpan
import com.soywiz.klock.hours

/**
 * We need some ranges in order to query App Stats with some flexibility.
 * For example, if now is 4:30pm, we want to query data also for 4:20pm or 4:35pm.
 * Here we define the spans for these values
 */
object DefaultValuesSpans {

    val Time: TimeSpan = 1.hours
}
