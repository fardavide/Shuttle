package shuttle.predictions.domain.usecase

import arrow.core.Either
import com.soywiz.klock.Time
import com.soywiz.klock.TimeSpan
import com.soywiz.klock.plus
import kotlinx.coroutines.flow.Flow
import shuttle.apps.domain.error.GenericError
import shuttle.apps.domain.model.AppModel
import shuttle.coordinates.domain.model.Coordinates
import shuttle.coordinates.domain.model.Location
import shuttle.predictions.domain.model.DefaultValuesSpans
import shuttle.stats.domain.StatsRepository

interface ObserveSuggestedApps {

    operator fun invoke(coordinates: Coordinates): Flow<Either<GenericError, List<AppModel>>> =
        this(coordinates.location, coordinates.time)

    operator fun invoke(
        location: Location,
        time: Time
    ): Flow<Either<GenericError, List<AppModel>>>
}

internal class ObserveSuggestedAppsImpl(
    private val locationToLocationRange: LocationToLocationRange,
    private val statsRepository: StatsRepository
) : ObserveSuggestedApps {

    override operator fun invoke(
        location: Location,
        time: Time
    ): Flow<Either<GenericError, List<AppModel>>> {
        val (startLocation, endLocation) = locationToLocationRange(location, DefaultValuesSpans.Location)
        return statsRepository.observeSuggestedApps(
            startLocation = startLocation,
            endLocation = endLocation,
            startTime = time - DefaultValuesSpans.Time / 2,
            endTime = time + DefaultValuesSpans.Time / 2
        )
    }
}

operator fun Time.minus(span: TimeSpan) = Time(this.encoded - span)
