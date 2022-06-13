package shuttle.database.datasource

import arrow.core.Option
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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

    fun findAllStats(
        geoHash: Option<DatabaseGeoHash>,
        startTime: DatabaseTime,
        endTime: DatabaseTime
    ): Flow<List<Stat>>

    @Deprecated("Use findAllStats", ReplaceWith("findAllStats(geoHash, startTime, endTime)"))
    fun findAllStatsOld(
        geoHash: DatabaseGeoHash,
        startTime: DatabaseTime,
        endTime: DatabaseTime
    ): Flow<List<DatabaseAppStat>>

    suspend fun insertOpenStats(
        appId: DatabaseAppId,
        geoHash: Option<DatabaseGeoHash>,
        date: DatabaseDate,
        time: DatabaseTime
    )

    suspend fun deleteAllCountersFor(appId: DatabaseAppId)
}

internal class StatDataSourceImpl(
    private val statQueries: StatQueries,
    private val ioDispatcher: CoroutineDispatcher
): StatDataSource {

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

    override fun findAllStatsOld(
        geoHash: DatabaseGeoHash,
        startTime: DatabaseTime,
        endTime: DatabaseTime
    ): Flow<List<DatabaseAppStat>> = statQueries.findAllStatsOld(
        geoHash = geoHash,
        startTime = startTime,
        endTime = endTime
    ).asFlow().mapToList(ioDispatcher).map(::toAppStats)

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
        geoHash: Option<DatabaseGeoHash>,
        date: DatabaseDate,
        time: DatabaseTime
    ) {
        statQueries.insertStat(
            appId = appId,
            geoHash = geoHash.orNull(),
            date = date,
            time = time
        )
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
