package shuttle.utils.kotlin

import korlibs.time.Date
import korlibs.time.DateTime
import korlibs.time.TimeSpan
import korlibs.time.minus
import kotlin.time.Duration

val Duration.inWholeMonths: Int
    get() = inWholeDays.toInt() / 30

operator fun Date.minus(duration: Duration): Date = minus(duration.toTimeSpan())
operator fun DateTime.minus(duration: Duration): DateTime = minus(duration.toTimeSpan())

fun Duration.toTimeSpan(): TimeSpan = TimeSpan(inWholeMilliseconds.toDouble())
