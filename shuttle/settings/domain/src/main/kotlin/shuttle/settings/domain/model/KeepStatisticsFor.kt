package shuttle.settings.domain.model

import shuttle.utils.kotlin.inWholeMonths
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

enum class KeepStatisticsFor(val duration: Duration) {
    OneMonth(30.days),
    TwoMonths(60.days),
    ThreeMonths(90.days),
    SixMonths(180.days),
    Forever(Duration.INFINITE);

    fun toMonths(): Int = duration.inWholeMonths

    companion object {
        
        val Default = ThreeMonths

        fun fromMonths(months: Int): KeepStatisticsFor = when {
            months <= 1 -> OneMonth
            months <= 2 -> TwoMonths
            months <= 3 -> ThreeMonths
            months <= 6 -> SixMonths
            months <= 10 -> SixMonths
            else -> Forever
        }
    }
}
