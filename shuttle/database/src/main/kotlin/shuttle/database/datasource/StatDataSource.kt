package shuttle.database.datasource

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import shuttle.database.FindAllStats
import shuttle.database.StatQueries
import shuttle.database.model.DatabaseAppId
import shuttle.database.model.DatabaseAppStat
import shuttle.database.model.DatabaseGeoHash
import shuttle.database.model.DatabaseTime
import shuttle.database.util.suspendTransaction

interface StatDataSource {

    fun findAllStats(
        geoHash: DatabaseGeoHash,
        startTime: DatabaseTime,
        endTime: DatabaseTime
    ): Flow<List<DatabaseAppStat>>

    suspend fun incrementCounter(
        appId: DatabaseAppId,
        geoHash: DatabaseGeoHash?,
        time: DatabaseTime
    )

    suspend fun deleteAllCountersFor(appId: DatabaseAppId)
}

internal class StatDataSourceImpl(
    private val statQueries: StatQueries,
    private val ioDispatcher: CoroutineDispatcher
): StatDataSource {

    override fun findAllStats(
        geoHash: DatabaseGeoHash,
        startTime: DatabaseTime,
        endTime: DatabaseTime
    ): Flow<List<DatabaseAppStat>> = statQueries.findAllStats(
        geoHash = geoHash,
        startTime = startTime,
        endTime = endTime
    ).asFlow().mapToList(ioDispatcher).map(::toAppStats)

    private fun toAppStats(items: List<FindAllStats>) = items.map(::toAppStat)

    private fun toAppStat(item: FindAllStats): DatabaseAppStat = when {
        item.appIdByLocation != null -> {
            DatabaseAppStat.ByLocation(item.appIdByLocation, requireNotNull(item.countByLocation).toInt())
        }
        item.appIdByTime != null -> {
            DatabaseAppStat.ByTime(item.appIdByTime, requireNotNull(item.countByTime).toInt())
        }
        else -> throw AssertionError("item cannot be parsed: $item")
    }

    override suspend fun incrementCounter(
        appId: DatabaseAppId,
        geoHash: DatabaseGeoHash?,
        time: DatabaseTime
    ) {
        statQueries.suspendTransaction(ioDispatcher) {
            geoHash?.let {
                incrementLocationCounter(appId, it)
            }
            incrementTimeCounter(appId, time)
        }
    }

    private fun StatQueries.incrementLocationCounter(
        appId: DatabaseAppId,
        geoHash: DatabaseGeoHash
    ) {
        val previousCount = findLocationStat(appId, geoHash)
            .executeAsOneOrNull()
            ?.count
            ?: 0
        insertLocationStat(
            appId = appId,
            geoHash = geoHash,
            count = previousCount + 1
        )
    }

    private fun StatQueries.incrementTimeCounter(appId: DatabaseAppId, time: DatabaseTime) {
        val previousCount = findTimeStat(appId, time)
            .executeAsOneOrNull()
            ?.count
            ?: 0
        insertTimeStat(
            appId = appId,
            time = time,
            count = previousCount + 1
        )
    }

    override suspend fun deleteAllCountersFor(appId: DatabaseAppId) {
        statQueries.suspendTransaction(ioDispatcher) {
            deleteLocationStatsForApp(appId)
            deleteTimeStatsForApp(appId)
        }
    }
}
