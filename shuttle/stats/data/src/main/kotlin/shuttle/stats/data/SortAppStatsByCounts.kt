package shuttle.stats.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import shuttle.apps.domain.model.AppId
import shuttle.database.model.DatabaseAppStat

class SortAppStatsByCounts(
    private val computationDispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(stats: Collection<DatabaseAppStat>): List<AppId> = withContext(computationDispatcher) {
        stats.groupBy { it.appId }.toList()
            .sortedByDescending { (_, stats) ->
                stats.sumOf { stat ->
                    when (stat) {
                        is DatabaseAppStat.ByLocation -> stat.count * 100_000
                        is DatabaseAppStat.ByTime -> stat.count
                    }
                }
            }.map { AppId(it.first.value) }
    }
}
