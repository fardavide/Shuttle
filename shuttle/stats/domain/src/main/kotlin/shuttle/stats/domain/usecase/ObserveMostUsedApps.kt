package shuttle.stats.domain.usecase

import arrow.core.Either
import com.soywiz.klock.Time
import com.soywiz.klock.TimeSpan
import com.soywiz.klock.plus
import kotlinx.coroutines.flow.Flow
import shuttle.apps.domain.error.GenericError
import shuttle.apps.domain.model.AppModel
import shuttle.stats.domain.StatsRepository
import shuttle.stats.domain.model.DefaultValuesSpans
import shuttle.stats.domain.model.Location

class ObserveMostUsedApps(
    private val locationToLocationRange: LocationToLocationRange,
    private val statsRepository: StatsRepository
) {

    operator fun invoke(
        location: Location,
        time: Time
    ): Flow<Either<GenericError, List<AppModel>>> {
        val (startLocation, endLocation) = locationToLocationRange(location, DefaultValuesSpans.Location)
        return statsRepository.observeMostUsedApps(
            startLocation = startLocation,
            endLocation = endLocation,
            startTime = time - DefaultValuesSpans.Time / 2,
            endTime = time + DefaultValuesSpans.Time / 2
        )
    }
}

operator fun Time.minus(span: TimeSpan) = Time(this.encoded - span)
