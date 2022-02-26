package shuttle.predictions.domain.usecase

import arrow.core.Either
import com.soywiz.klock.Time
import kotlinx.coroutines.flow.Flow
import shuttle.apps.domain.error.GenericError
import shuttle.apps.domain.model.AppModel
import shuttle.coordinates.domain.model.Coordinates
import shuttle.coordinates.domain.model.Location
import shuttle.predictions.domain.model.DefaultValuesSpans
import shuttle.stats.domain.StatsRepository

interface ObserveSuggestedAppsByCoordinates {

    operator fun invoke(coordinates: Coordinates): Flow<Either<GenericError, List<AppModel>>> =
        this(coordinates.location, coordinates.time)

    operator fun invoke(
        location: Location,
        time: Time
    ): Flow<Either<GenericError, List<AppModel>>>
}

internal class ObserveSuggestedAppsByCoordinatesImpl(
    private val locationToLocationRange: LocationToLocationRange,
    private val statsRepository: StatsRepository,
    private val timeToTimeRange: TimeToTimeRange
) : ObserveSuggestedAppsByCoordinates {

    override operator fun invoke(
        location: Location,
        time: Time
    ): Flow<Either<GenericError, List<AppModel>>> {
        val (startLocation, endLocation) = locationToLocationRange(location, DefaultValuesSpans.Location)
        val (startTime, endTime) = with(timeToTimeRange(time, DefaultValuesSpans.Time)) {
            start to endInclusive
        }
        return statsRepository.observeSuggestedApps(
            startLocation = startLocation,
            endLocation = endLocation,
            startTime = startTime,
            endTime = endTime
        )
    }
}
