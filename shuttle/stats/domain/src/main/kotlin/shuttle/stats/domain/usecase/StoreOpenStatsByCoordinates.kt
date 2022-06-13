package shuttle.stats.domain.usecase

import shuttle.apps.domain.model.AppId
import shuttle.coordinates.domain.model.CoordinatesResult
import shuttle.stats.domain.StatsRepository

class StoreOpenStatsByCoordinates(
    private val statsRepository: StatsRepository
) {

    suspend operator fun invoke(appId: AppId, coordinatesResult: CoordinatesResult) {
        statsRepository.storeOpenStats(
            appId = appId,
            location = coordinatesResult.location.orNone(),
            time = coordinatesResult.dateTime.time,
            date = coordinatesResult.dateTime.date
        )
    }
}
