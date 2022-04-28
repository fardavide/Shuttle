package shuttle.stats.data.model

import shuttle.database.model.DatabaseAppId
import shuttle.database.model.DatabaseAppStat

internal data class GroupedDatabaseAppStats(
    val byTimeCount: Double,
    val byLocationCount: Double,
    val groupedByAppId: List<Pair<DatabaseAppId, List<DatabaseAppStat>>>
)
