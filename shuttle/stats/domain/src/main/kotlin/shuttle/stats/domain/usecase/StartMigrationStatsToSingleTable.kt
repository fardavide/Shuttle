package shuttle.stats.domain.usecase

import shuttle.stats.domain.StatsRepository

class StartMigrationStatsToSingleTable(
    private val statsRepository: StatsRepository
) {

    operator fun invoke() {
        statsRepository.startMigrationStatsToSingleTable()
    }
}
