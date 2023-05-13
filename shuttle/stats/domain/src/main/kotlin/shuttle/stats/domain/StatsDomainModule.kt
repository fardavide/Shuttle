package shuttle.stats.domain

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Module
@ComponentScan
class StatsDomainModule {

    @Factory
    @Named(StatsQualifier.Interval.DeleteOldStats.Scheduler.Default)
    fun intervalDeleteOldStatsSchedulerDefault() = 12.toDuration(DurationUnit.HOURS)

    @Factory
    @Named(StatsQualifier.Interval.DeleteOldStats.Scheduler.Flex)
    fun intervalDeleteOldStatsSchedulerFlex() = 1.toDuration(DurationUnit.DAYS)
}

object StatsQualifier {

    object Interval {

        object DeleteOldStats {

            object Scheduler {

                const val Default = "Coordinates Interval Location Scheduler Default"
                const val Flex = "Coordinates Interval Location Scheduler Flex"
            }
        }
    }
}

