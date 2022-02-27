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
import shuttle.database.model.DatabaseLatitude
import shuttle.database.model.DatabaseLongitude
import shuttle.database.model.DatabaseTime
import shuttle.database.util.suspendTransaction

interface StatDataSource {

    fun findAllStats(
        startLatitude: DatabaseLatitude,
        endLatitude: DatabaseLatitude,
        startLongitude: DatabaseLongitude,
        endLongitude: DatabaseLongitude,
        startTime: DatabaseTime,
        endTime: DatabaseTime
    ): Flow<List<DatabaseAppStat>>

    suspend fun incrementCounter(
        appId: DatabaseAppId,
        latitude: DatabaseLatitude,
        longitude: DatabaseLongitude,
        time: DatabaseTime
    )
}

internal class StatDataSourceImpl(
    private val statQueries: StatQueries,
    private val ioDispatcher: CoroutineDispatcher
): StatDataSource {

    override fun findAllStats(
        startLatitude: DatabaseLatitude,
        endLatitude: DatabaseLatitude,
        startLongitude: DatabaseLongitude,
        endLongitude: DatabaseLongitude,
        startTime: DatabaseTime,
        endTime: DatabaseTime
    ): Flow<List<DatabaseAppStat>> = statQueries.findAllStats(
        startLatitude = startLatitude,
        endLatitude = endLatitude,
        startLongitude = startLongitude,
        endLongitude = endLongitude,
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
        latitude: DatabaseLatitude,
        longitude: DatabaseLongitude,
        time: DatabaseTime
    ) {
        statQueries.suspendTransaction(ioDispatcher) {
            incrementLocationCounter(appId, latitude, longitude)
            incrementTimeCounter(appId, time)
        }
    }

    private fun StatQueries.incrementLocationCounter(
        appId: DatabaseAppId,
        latitude: DatabaseLatitude,
        longitude: DatabaseLongitude
    ) {
        val previousCount = findLocationStat(appId, latitude, longitude)
            .executeAsOneOrNull()
            ?.count
            ?: 0
        insertLocationStat(
            appId = appId,
            latitude = latitude,
            longitude = longitude,
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
}
