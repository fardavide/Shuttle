package shuttle.database.datasource

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOneNotNull
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import shuttle.database.LastLocation
import shuttle.database.LastLocationQueries
import shuttle.database.model.DatabaseGeoHash
import shuttle.database.util.suspendTransaction

interface LastLocationDataSource {

    fun find(): Flow<LastLocation?>

    suspend fun insert(geoHash: DatabaseGeoHash)
}

internal class LastLocationDataSourceImpl(
    private val lastLocationQueries: LastLocationQueries,
    private val ioDispatcher: CoroutineDispatcher
): LastLocationDataSource {

    override fun find(): Flow<LastLocation?> =
        lastLocationQueries.find().asFlow().mapToOneNotNull(ioDispatcher)

    override suspend fun insert(geoHash: DatabaseGeoHash) {
        lastLocationQueries.suspendTransaction(ioDispatcher) {
            insert(geoHash)
        }
    }
}
