package shuttle.stats.domain.usecase

import shuttle.apps.domain.model.AppId
import shuttle.coordinates.domain.model.CoordinatesResult
import shuttle.stats.domain.StatsRepository

class IncrementOpenCounterByCoordinates(
    private val statsRepository: StatsRepository,
) {

    suspend operator fun invoke(appId: AppId, coordinatesResult: CoordinatesResult) {
        statsRepository.incrementCounter(
            appId = appId,
            location = coordinatesResult.location.orNull(),
            time = coordinatesResult.time
        )
    }
}
