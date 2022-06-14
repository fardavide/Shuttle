package shuttle.stats.data.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import shuttle.apps.domain.model.AppId
import shuttle.coordinates.domain.usecase.ObserveCurrentDateTime
import shuttle.database.Stat
import shuttle.stats.data.mapper.DatabaseDateMapper

internal class SortAppStats(
    private val computationDispatcher: CoroutineDispatcher,
    private val databaseDateMapper: DatabaseDateMapper,
    private val observeCurrentDateTime: ObserveCurrentDateTime
) {

    suspend operator fun invoke(stats: Collection<Stat>): List<AppId> = withContext(computationDispatcher) {
        val currentDayAsDatabaseData = databaseDateMapper.toDatabaseDate(observeCurrentDateTime().first())
        val groupedByAppId = stats.groupBy { it.appId }.toList()

        groupedByAppId.sortedBy { (_, stats) ->
            stats.sumOf { (currentDayAsDatabaseData - it.date).dayNumber } +
                stats.count()
        }.map { AppId(it.first.value) }
    }
}
