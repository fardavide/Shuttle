package shuttle.stats.domain.model

import com.soywiz.klock.Time

sealed interface StatsCounter {

    val count: Count
}

data class LocationCounter(
    val location: Location,
    override val count: Count
): StatsCounter

infix fun Location.withCount(count: Count) = LocationCounter(this, count)
infix fun Location.withCount(count: Int) = LocationCounter(this, Count(count))

data class TimeCounter(
    val time: Time,
    override val count: Count
): StatsCounter

infix fun Time.withCount(count: Count) = TimeCounter(this, count)
infix fun Time.withCount(count: Int) = TimeCounter(this, Count(count))

