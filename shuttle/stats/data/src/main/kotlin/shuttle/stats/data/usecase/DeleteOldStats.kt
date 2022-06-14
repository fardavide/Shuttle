package shuttle.stats.data.usecase

import kotlinx.coroutines.flow.first
import shuttle.coordinates.domain.usecase.ObserveCurrentDateTime
import shuttle.database.datasource.StatDataSource
import shuttle.stats.data.mapper.DatabaseDateMapper

internal class DeleteOldStats(
    private val databaseDateMapper: DatabaseDateMapper,
    private val observeCurrentDateTime: ObserveCurrentDateTime,
    private val statDataSource: StatDataSource
) {

    suspend operator fun invoke() {
        statDataSource
        val currentDate = databaseDateMapper.toDatabaseDate(observeCurrentDateTime().first())

        statDataSource.clearAllStatsOlderThan(currentDate - 90)
    }
}
