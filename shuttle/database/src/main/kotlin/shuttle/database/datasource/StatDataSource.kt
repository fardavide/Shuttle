package shuttle.database.datasource

import arrow.core.Option
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import shuttle.database.StatQueries
import shuttle.database.model.DatabaseAppId
import shuttle.database.model.DatabaseDate
import shuttle.database.model.DatabaseGeoHash
import shuttle.database.model.DatabaseStat
import shuttle.database.model.DatabaseTime
import shuttle.database.util.suspendTransaction
import shuttle.utils.kotlin.IoDispatcher

interface StatDataSource {

    suspend fun clearAllStatsOlderThan(date: DatabaseDate)

    suspend fun deleteAllCountersFor(appId: DatabaseAppId)

    fun findAllStats(): Flow<List<DatabaseStat>>

    fun findAllStats(
        geoHash: Option<DatabaseGeoHash>,
        startTime: DatabaseTime,
        endTime: DatabaseTime
    ): Flow<List<DatabaseStat>>

    suspend fun insertOpenStats(
        appId: DatabaseAppId,
        date: DatabaseDate,
        geoHash: Option<DatabaseGeoHash>,
        time: DatabaseTime
    )
}

@Factory
internal class RealStatDataSource(
    private val statQueries: StatQueries,
    @Named(IoDispatcher) private val ioDispatcher: CoroutineDispatcher
) : StatDataSource {

    override suspend fun clearAllStatsOlderThan(date: DatabaseDate) {
        statQueries.clearAllStatsOlderThan(date)
    }

    override suspend fun deleteAllCountersFor(appId: DatabaseAppId) {
        statQueries.suspendTransaction(ioDispatcher) {
            deleteStatsForApp(appId)
        }
    }

    override fun findAllStats(): Flow<List<DatabaseStat>> =
        statQueries.findAllStats().asFlow().mapToList(ioDispatcher)

    override fun findAllStats(
        geoHash: Option<DatabaseGeoHash>,
        startTime: DatabaseTime,
        endTime: DatabaseTime
    ): Flow<List<DatabaseStat>> {
        val query = when (val geoHashValue = geoHash.getOrNull()) {
            null -> statQueries.findAllStatsByTime(
                startTime = startTime,
                endTime = endTime
            )

            else -> statQueries.findAllStatsByGeoHashAndTime(
                geoHash = geoHashValue,
                startTime = startTime,
                endTime = endTime
            )
        }
        return query.asFlow().mapToList(ioDispatcher)
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
            geoHash = geoHash.getOrNull(),
            time = time
        )
    }
}
