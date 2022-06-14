package shuttle.database.datasource

import arrow.core.Option
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import shuttle.database.FindAllStatsFromLocationAndTimeTables
import shuttle.database.FindAllStatsOld
import shuttle.database.Stat
import shuttle.database.StatQueries
import shuttle.database.model.DatabaseAppId
import shuttle.database.model.DatabaseAppStat
import shuttle.database.model.DatabaseDate
import shuttle.database.model.DatabaseGeoHash
import shuttle.database.model.DatabaseTime
import shuttle.database.util.suspendTransaction

interface StatDataSource {

    suspend fun clearAllStatsFromLocationAndTimeTables()

    suspend fun deleteAllCountersFor(appId: DatabaseAppId)

    fun findAllStats(
        geoHash: Option<DatabaseGeoHash>,
        startTime: DatabaseTime,
        endTime: DatabaseTime
    ): Flow<List<Stat>>

    fun findAllStatsFromLocationAndTimeTables(): Flow<List<FindAllStatsFromLocationAndTimeTables>>

    suspend fun insertOpenStats(
        appId: DatabaseAppId,
        date: DatabaseDate,
        geoHash: Option<DatabaseGeoHash>,
        time: DatabaseTime
    )
}

internal class StatDataSourceImpl(
    private val statQueries: StatQueries,
    private val ioDispatcher: CoroutineDispatcher
): StatDataSource {

    override suspend fun clearAllStatsFromLocationAndTimeTables() {
        statQueries.clearAllStatsFromLocationTable()
        statQueries.clearAllStatsFromTimeTable()
    }

    override suspend fun deleteAllCountersFor(appId: DatabaseAppId) {
        statQueries.suspendTransaction(ioDispatcher) {
            deleteLocationStatsForApp(appId)
            deleteTimeStatsForApp(appId)
        }
    }

    override fun findAllStats(
        geoHash: Option<DatabaseGeoHash>,
        startTime: DatabaseTime,
        endTime: DatabaseTime
    ): Flow<List<Stat>> {
        val geoHashValue = geoHash.orNull()
        val query =
            if (geoHashValue == null) {
                statQueries.findAllStats(
                    startTime = startTime,
                    endTime = endTime
                )
            }else {
                statQueries.findAllStatsByGeoHash(
                    geoHash = geoHashValue,
                    startTime = startTime,
                    endTime = endTime
                )
            }
        return query.asFlow().mapToList(ioDispatcher)
    }

    override fun findAllStatsFromLocationAndTimeTables(): Flow<List<FindAllStatsFromLocationAndTimeTables>> =
        statQueries.findAllStatsFromLocationAndTimeTables().asFlow().mapToList(ioDispatcher)

    private fun toAppStats(items: List<FindAllStatsOld>) = items.map(::toAppStat)

    private fun toAppStat(item: FindAllStatsOld): DatabaseAppStat = when {
        item.appIdByLocation != null -> {
            DatabaseAppStat.ByLocation(item.appIdByLocation, requireNotNull(item.countByLocation).toInt())
        }
        item.appIdByTime != null -> {
            DatabaseAppStat.ByTime(item.appIdByTime, requireNotNull(item.countByTime).toInt())
        }
        else -> throw AssertionError("item cannot be parsed: $item")
    }

    override suspend fun insertOpenStats(
        appId: DatabaseAppId,
        date: DatabaseDate,
        geoHash: Option<DatabaseGeoHash>,
        time: DatabaseTime
    ) {
        statQueries.insertStat(
            appId = appId,
            date = date,
            geoHash = geoHash.orNull(),
            time = time
        )
    }
}
