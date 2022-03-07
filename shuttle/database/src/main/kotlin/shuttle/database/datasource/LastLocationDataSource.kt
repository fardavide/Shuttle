package shuttle.database.datasource

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOneNotNull
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import shuttle.database.LastLocation
import shuttle.database.LastLocationQueries
import shuttle.database.model.DatabaseLatitude
import shuttle.database.model.DatabaseLongitude
import shuttle.database.util.suspendTransaction

interface LastLocationDataSource {

    fun find(): Flow<LastLocation?>

    suspend fun insert(latitude: DatabaseLatitude, longitude: DatabaseLongitude)
}

internal class LastLocationDataSourceImpl(
    private val lastLocationQueries: LastLocationQueries,
    private val ioDispatcher: CoroutineDispatcher
): LastLocationDataSource {

    override fun find(): Flow<LastLocation?> =
        lastLocationQueries.find().asFlow().mapToOneNotNull(ioDispatcher)

    override suspend fun insert(latitude: DatabaseLatitude, longitude: DatabaseLongitude) {
        lastLocationQueries.suspendTransaction(ioDispatcher) {
            insert(latitude, longitude)
        }
    }
}
