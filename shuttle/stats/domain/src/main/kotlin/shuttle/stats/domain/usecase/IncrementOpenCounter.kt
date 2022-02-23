package shuttle.stats.domain.usecase

import com.soywiz.klock.Time
import shuttle.apps.domain.model.AppId
import shuttle.stats.domain.StatsRepository
import shuttle.stats.domain.model.Location

class IncrementOpenCounter(
    private val statsRepository: StatsRepository
) {

    suspend operator fun invoke(appId: AppId, location: Location, time: Time) {
        statsRepository.incrementCounter(
            appId = appId,
            location = location,
            time = time
        )
    }
}
