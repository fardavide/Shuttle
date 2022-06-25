package shuttle.stats.data.usecase

import arrow.core.Option
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import shuttle.coordinates.domain.usecase.ObserveCurrentDateTime
import shuttle.database.FindAllStatsFromLocationAndTimeTables
import shuttle.database.datasource.StatDataSource
import shuttle.database.model.DatabaseDate
import shuttle.database.model.DatabaseTime
import shuttle.stats.data.mapper.DatabaseDateMapper

internal class MigrateStatsToSingleTable(
    private val databaseDateMapper: DatabaseDateMapper,
    private val observeCurrentDateTime: ObserveCurrentDateTime,
    private val statDataSource: StatDataSource
) {

    suspend operator fun invoke() {
        val currentDate = databaseDateMapper.toDatabaseDate(observeCurrentDateTime().first())

        val stats = statDataSource.findAllStatsFromLocationAndTimeTables().first()
        stats.groupBy { it.appIdByLocation ?: it.appIdByTime }.forEach { (appId, stat) ->
            appId to stat.groupBy { it.location }.forEach { (_, statsList) ->
                var dateForStat = currentDate
                statsList.forEach { stat ->
                    insertStat(stat, dateForStat)
                    dateForStat -= 1
                    delay(3)
                }
                delay(30)
            }
            delay(300)
        }
        statDataSource.clearAllStatsFromLocationAndTimeTables()
    }

    private suspend fun insertStat(stat: FindAllStatsFromLocationAndTimeTables, date: DatabaseDate) {
        val appId = stat.appIdByLocation ?: stat.appIdByTime ?: return

        statDataSource.insertOpenStats(
            appId = appId,
            date = date,
            geoHash = Option.fromNullable(stat.location),
            time = stat.time ?: DatabaseTime(minuteOfTheDay = 0)
        )
    }
}
