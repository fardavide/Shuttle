package shuttle.stats.domain.usecase

import com.soywiz.klock.Time
import shuttle.apps.domain.model.AppId
import shuttle.coordinates.domain.model.Coordinates
import shuttle.coordinates.domain.model.Location
import shuttle.settings.domain.usecase.IsBlacklisted
import shuttle.stats.domain.StatsRepository

/**
 * Increments the open counter for a given app, if not blacklisted
 */
class IncrementOpenCounterByCoordinates(
    private val statsRepository: StatsRepository,
    private val isBlacklisted: IsBlacklisted
) {

    suspend operator fun invoke(appId: AppId, coordinates: Coordinates) {
        this(appId, coordinates.location, coordinates.time)
    }

    suspend operator fun invoke(appId: AppId, location: Location, time: Time) {
        if (isBlacklisted(appId).not()) {
            statsRepository.incrementCounter(
                appId = appId,
                location = location,
                time = time
            )
        }
    }
}
