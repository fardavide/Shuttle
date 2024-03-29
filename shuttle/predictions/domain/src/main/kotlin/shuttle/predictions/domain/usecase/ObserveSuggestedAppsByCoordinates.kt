package shuttle.predictions.domain.usecase

import arrow.core.Option
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import shuttle.apps.domain.model.SuggestedAppModel
import shuttle.coordinates.domain.model.Coordinates
import shuttle.predictions.domain.model.DefaultValuesSpans
import shuttle.stats.domain.repository.StatsRepository

interface ObserveSuggestedAppsByCoordinates {

    operator fun invoke(coordinates: Coordinates, takeAtLeast: Int): Flow<List<SuggestedAppModel>>
}

@Factory
internal class RealObserveSuggestedAppsByCoordinates(
    private val statsRepository: StatsRepository,
    private val timeToTimeRange: TimeToTimeRange
) : ObserveSuggestedAppsByCoordinates {

    override operator fun invoke(coordinates: Coordinates, takeAtLeast: Int): Flow<List<SuggestedAppModel>> {
        val (startTime, endTime) = with(timeToTimeRange(coordinates.dateTime.time, DefaultValuesSpans.Time)) {
            start to endInclusive
        }
        return statsRepository.observeSuggestedApps(
            location = Option(coordinates.location),
            date = coordinates.dateTime.date,
            startTime = startTime,
            endTime = endTime,
            takeAtLeast = takeAtLeast
        )
    }
}
