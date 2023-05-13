package shuttle.stats.domain.usecase

import org.koin.core.annotation.Factory
import shuttle.stats.domain.repository.StatsRepository

@Factory
class StartDeleteOldStats(
    private val statsRepository: StatsRepository
) {

    operator fun invoke() {
        statsRepository.startDeleteOldStats()
    }
}
