package shuttle.stats.data.usecase

import kotlinx.coroutines.flow.first
import shuttle.coordinates.domain.usecase.ObserveCurrentDateTime
import shuttle.database.datasource.StatDataSource
import shuttle.stats.data.mapper.DatabaseDateAndTimeMapper

internal class DeleteOldStats(
    private val databaseDateAndTimeMapper: DatabaseDateAndTimeMapper,
    private val observeCurrentDateTime: ObserveCurrentDateTime,
    private val statDataSource: StatDataSource
) {

    suspend operator fun invoke() {
        statDataSource
        val currentDate = databaseDateAndTimeMapper.toDatabaseDate(observeCurrentDateTime().first().date)

        statDataSource.clearAllStatsOlderThan(date = currentDate - DaysToKeep)
    }

    companion object {

        private const val DaysToKeep = 90
    }
}
