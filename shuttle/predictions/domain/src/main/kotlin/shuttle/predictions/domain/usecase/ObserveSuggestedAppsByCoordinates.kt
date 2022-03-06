package shuttle.predictions.domain.usecase

import kotlinx.coroutines.flow.Flow
import shuttle.apps.domain.model.SuggestedAppModel
import shuttle.coordinates.domain.model.Coordinates
import shuttle.predictions.domain.model.DefaultValuesSpans
import shuttle.stats.domain.StatsRepository

interface ObserveSuggestedAppsByCoordinates {

    operator fun invoke(
        coordinates: Coordinates
    ): Flow<List<SuggestedAppModel>>
}

internal class ObserveSuggestedAppsByCoordinatesImpl(
    private val locationToLocationRange: LocationToLocationRange,
    private val statsRepository: StatsRepository,
    private val timeToTimeRange: TimeToTimeRange
) : ObserveSuggestedAppsByCoordinates {

    override operator fun invoke(
        coordinates: Coordinates
    ): Flow<List<SuggestedAppModel>> {
        val (startLocation, endLocation) = locationToLocationRange(coordinates.location, DefaultValuesSpans.Location)
        val (startTime, endTime) = with(timeToTimeRange(coordinates.time, DefaultValuesSpans.Time)) {
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
