package shuttle.stats.domain.usecase

import shuttle.stats.domain.StatsRepository

class StartDeleteOldStats(
    private val statsRepository: StatsRepository
) {

    operator fun invoke() {
        statsRepository.startDeleteOldStats()
    }
}
