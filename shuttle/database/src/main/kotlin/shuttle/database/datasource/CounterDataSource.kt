package shuttle.database.datasource

import arrow.core.Option
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import shuttle.database.CounterQueries
import shuttle.database.model.DatabaseAppId
import shuttle.database.model.DatabaseCounterId
import shuttle.database.model.DatabaseGeoHash
import shuttle.database.model.DatabaseHour
import shuttle.database.util.suspendTransaction
import shuttle.utils.kotlin.IoDispatcher

interface CounterDataSource {

    suspend fun deleteAllCountersFor(appId: DatabaseAppId)

    fun findSortedApps(geoHash: Option<DatabaseGeoHash>, time: DatabaseHour): Flow<List<DatabaseAppId>>

    suspend fun incrementCounter(
        appId: DatabaseAppId,
        geoHash: Option<DatabaseGeoHash>,
        time: DatabaseHour
    )
}

@Factory
internal class RealCounterDataSource(
    private val counterQueries: CounterQueries,
    @Named(IoDispatcher) private val ioDispatcher: CoroutineDispatcher
) : CounterDataSource {

    override suspend fun deleteAllCountersFor(appId: DatabaseAppId) {
        counterQueries.suspendTransaction(ioDispatcher) {
            deleteByAppId(appId)
        }
    }

    override fun findSortedApps(
        geoHash: Option<DatabaseGeoHash>,
        time: DatabaseHour
    ): Flow<List<DatabaseAppId>> {
        val flows = allCounterIdsFor(geoHash, time).map { counterId ->
            counterQueries.findSortedApps(counterId) { appId, _ -> appId }
                .asFlow().mapToList(ioDispatcher)
        }
        return combine(flows) { allSortedApps ->
            allSortedApps.toList().flatten().distinct()
        }
    }

    override suspend fun incrementCounter(
        appId: DatabaseAppId,
        geoHash: Option<DatabaseGeoHash>,
        time: DatabaseHour
    ) {
        counterQueries.suspendTransaction(ioDispatcher) {
            for (counterId in allCounterIdsFor(geoHash, time)) {
                counterQueries.incrementCounter(
                    appId = appId,
                    counterId = counterId
                )
            }
        }
    }

    private fun allCounterIdsFor(
        geoHash: Option<DatabaseGeoHash>,
        time: DatabaseHour
    ): List<DatabaseCounterId> {
        return listOfNotNull(
            geoHash.getOrNull()?.let { DatabaseCounterId.ForTimeAndLocation(time, it) },
            geoHash.getOrNull()?.let { DatabaseCounterId.ForLocation(it) },
            DatabaseCounterId.ForTime(time),
            DatabaseCounterId.All
        )
    }
}
