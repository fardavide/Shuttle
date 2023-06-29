package shuttle.utils.kotlin

import kotlin.time.Duration

val Duration.inWholeMonths: Int
    get() = inWholeDays.toInt() / 30
