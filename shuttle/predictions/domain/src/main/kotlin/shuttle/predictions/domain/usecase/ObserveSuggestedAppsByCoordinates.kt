package shuttle.predictions.domain.usecase

import arrow.core.Either
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import shuttle.apps.domain.model.AppModel
import shuttle.coordinates.domain.model.Coordinates
import shuttle.predictions.domain.error.ObserveSuggestedAppsError
import shuttle.predictions.domain.model.DefaultValuesSpans
import shuttle.stats.domain.StatsRepository

interface ObserveSuggestedAppsByCoordinates {

    operator fun invoke(coordinates: Coordinates): Flow<Either<ObserveSuggestedAppsError.DataError, List<AppModel>>>
}

internal class ObserveSuggestedAppsByCoordinatesImpl(
    private val locationToLocationRange: LocationToLocationRange,
    private val statsRepository: StatsRepository,
    private val timeToTimeRange: TimeToTimeRange
) : ObserveSuggestedAppsByCoordinates {

    override operator fun invoke(coordinates: Coordinates): Flow<Either<ObserveSuggestedAppsError.DataError, List<AppModel>>> {
        val (startLocation, endLocation) = locationToLocationRange(coordinates.location, DefaultValuesSpans.Location)
        val (startTime, endTime) = with(timeToTimeRange(coordinates.time, DefaultValuesSpans.Time)) {
            start to endInclusive
        }
        return statsRepository.observeSuggestedApps(
            startLocation = startLocation,
            endLocation = endLocation,
            startTime = startTime,
            endTime = endTime
        ).map { it.mapLeft { ObserveSuggestedAppsError.DataError } }
    }
}
