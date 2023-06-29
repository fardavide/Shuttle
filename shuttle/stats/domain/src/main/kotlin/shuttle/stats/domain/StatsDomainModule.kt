package shuttle.stats.domain

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

@Module
@ComponentScan
class StatsDomainModule {

    @Factory
    @Named(StatsQualifier.Interval.DeleteOldStats.Scheduler.Default)
    fun intervalDeleteOldStatsSchedulerDefault() = 12.hours

    @Factory
    @Named(StatsQualifier.Interval.DeleteOldStats.Scheduler.Flex)
    fun intervalDeleteOldStatsSchedulerFlex() = 1.days
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

