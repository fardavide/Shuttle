package shuttle.database.datasource

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOneNotNull
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import shuttle.database.LastLocation
import shuttle.database.LastLocationQueries
import shuttle.database.model.DatabaseGeoHash
import shuttle.database.util.suspendTransaction
import shuttle.utils.kotlin.IoDispatcher

interface LastLocationDataSource {

    fun find(): Flow<LastLocation?>

    suspend fun insert(geoHash: DatabaseGeoHash)
}

@Factory
internal class LastLocationDataSourceImpl(
    private val lastLocationQueries: LastLocationQueries,
    @Named(IoDispatcher) private val ioDispatcher: CoroutineDispatcher
) : LastLocationDataSource {

    override fun find(): Flow<LastLocation?> =
        lastLocationQueries.find().asFlow().mapToOneNotNull(ioDispatcher)

    override suspend fun insert(geoHash: DatabaseGeoHash) {
        lastLocationQueries.suspendTransaction(ioDispatcher) {
            insert(geoHash)
        }
    }
}
