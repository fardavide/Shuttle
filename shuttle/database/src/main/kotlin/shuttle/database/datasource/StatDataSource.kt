package shuttle.database.datasource

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import shuttle.database.StatQueries
import shuttle.database.model.DatabaseAppId
import shuttle.database.model.DatabaseLatitude
import shuttle.database.model.DatabaseLongitude
import shuttle.database.model.DatabaseTime
import shuttle.database.util.suspendTransaction

interface StatDataSource {

    fun observeMostUsedAppsIds(
        startLatitude: DatabaseLatitude,
        endLatitude: DatabaseLatitude,
        startLongitude: DatabaseLongitude,
        endLongitude: DatabaseLongitude,
        startTime: DatabaseTime,
        endTime: DatabaseTime
    ): Flow<List<DatabaseAppId>>

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

    override fun observeMostUsedAppsIds(
        startLatitude: DatabaseLatitude,
        endLatitude: DatabaseLatitude,
        startLongitude: DatabaseLongitude,
        endLongitude: DatabaseLongitude,
        startTime: DatabaseTime,
        endTime: DatabaseTime
    ): Flow<List<DatabaseAppId>> = statQueries.findMostUsedAppsIds(
        startLatitude = startLatitude,
        endLatitude = endLatitude,
        startLongitude = startLongitude,
        endLongitude = endLongitude,
        startTime = startTime,
        endTime = endTime
    ).asFlow().mapToList(ioDispatcher)

    override suspend fun incrementCounter(
        appId: DatabaseAppId,
        latitude: DatabaseLatitude,
        longitude: DatabaseLongitude,
        time: DatabaseTime
    ) {
        statQueries.suspendTransaction(ioDispatcher) {
            insertApp(appId)
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
