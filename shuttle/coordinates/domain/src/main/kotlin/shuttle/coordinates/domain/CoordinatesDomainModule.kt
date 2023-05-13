package shuttle.coordinates.domain

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Module
@ComponentScan
class CoordinatesDomainModule {

    @Factory
    @Named(CoordinatesQualifier.Interval.Location.Expiration)
    fun intervalLocationExpiration() = 20.toDuration(DurationUnit.MINUTES)

    @Factory
    @Named(CoordinatesQualifier.Interval.Location.FetchTimeout)
    fun intervalLocationFetchTimeout() = 10.toDuration(DurationUnit.SECONDS)

    @Factory
    @Named(CoordinatesQualifier.Interval.Location.MinRefresh)
    fun intervalLocationMinRefresh() = 2.toDuration(DurationUnit.MINUTES)

    @Factory
    @Named(CoordinatesQualifier.Interval.Location.Scheduler.Default)
    fun intervalLocationSchedulerDefault() = 15.toDuration(DurationUnit.MINUTES)

    @Factory
    @Named(CoordinatesQualifier.Interval.Location.Scheduler.Flex)
    fun intervalLocationSchedulerFlex() = 1.toDuration(DurationUnit.HOURS)

    @Factory
    @Named(CoordinatesQualifier.Interval.Time.Refresh)
    fun intervalTimeRefresh() = 1.toDuration(DurationUnit.MINUTES)
}

object CoordinatesQualifier {

    object Interval {

        object Location {

            const val Expiration = "Coordinates Interval Location Expiration"
            const val FetchTimeout = "Coordinates Interval Location Fetch Timeout"
            const val MinRefresh = "Coordinates Interval Location Min Refresh"

            object Scheduler {

                const val Default = "Coordinates Interval Location Scheduler Default"
                const val Flex = "Coordinates Interval Location Scheduler Flex"
            }
        }

        object Time {

            const val Refresh = "Coordinates Interval Time Refresh"
        }
    }
}
