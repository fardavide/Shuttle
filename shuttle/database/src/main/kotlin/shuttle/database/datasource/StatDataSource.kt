package shuttle.database.datasource

import arrow.core.Option
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import shuttle.database.Stat
import shuttle.database.StatQueries
import shuttle.database.model.DatabaseAppId
import shuttle.database.model.DatabaseDate
import shuttle.database.model.DatabaseGeoHash
import shuttle.database.model.DatabaseTime
import shuttle.database.util.suspendTransaction
import shuttle.utils.kotlin.IoDispatcher

interface StatDataSource {

    suspend fun clearAllStatsOlderThan(date: DatabaseDate)

    suspend fun deleteAllCountersFor(appId: DatabaseAppId)

    fun findAllStats(): Flow<List<Stat>>

    fun findAllStats(
        geoHash: Option<DatabaseGeoHash>,
        startTime: DatabaseTime,
        endTime: DatabaseTime
    ): Flow<List<Stat>>

    suspend fun insertOpenStats(
        appId: DatabaseAppId,
        date: DatabaseDate,
        geoHash: Option<DatabaseGeoHash>,
        time: DatabaseTime
    )
}

@Factory
internal class StatDataSourceImpl(
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

    override fun findAllStats(): Flow<List<Stat>> = statQueries.findAllStats().asFlow().mapToList(ioDispatcher)

    override fun findAllStats(
        geoHash: Option<DatabaseGeoHash>,
        startTime: DatabaseTime,
        endTime: DatabaseTime
    ): Flow<List<Stat>> {
        val geoHashValue = geoHash.orNull()
        val query =
            if (geoHashValue == null) {
                statQueries.findAllStatsByTime(
                    startTime = startTime,
                    endTime = endTime
                )
            } else {
                statQueries.findAllStatsByGeoHashAndTime(
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
            geoHash = geoHash.orNull(),
            time = time
        )
    }
}
