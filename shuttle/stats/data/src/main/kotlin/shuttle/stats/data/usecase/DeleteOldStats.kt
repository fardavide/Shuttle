package shuttle.stats.data.usecase

import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory
import shuttle.coordinates.domain.usecase.ObserveCurrentDateTime
import shuttle.database.datasource.StatDataSource
import shuttle.settings.domain.usecase.ObserveKeepStatisticsFor
import shuttle.stats.data.mapper.DatabaseDateAndTimeMapper
import shuttle.utils.kotlin.minus

@Factory
internal class DeleteOldStats(
    private val databaseDateAndTimeMapper: DatabaseDateAndTimeMapper,
    private val observeCurrentDateTime: ObserveCurrentDateTime,
    private val observeKeepStatisticsFor: ObserveKeepStatisticsFor,
    private val statDataSource: StatDataSource
) {

    suspend operator fun invoke() {
        val currentDate = observeCurrentDateTime().first().date
        val toKeep = observeKeepStatisticsFor().first().duration

        val databaseDate = databaseDateAndTimeMapper.toDatabaseDate(currentDate - toKeep)
        statDataSource.clearAllStatsOlderThan(date = databaseDate)
    }
}
