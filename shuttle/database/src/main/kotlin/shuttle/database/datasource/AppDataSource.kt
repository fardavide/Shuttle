package shuttle.database.datasource

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import shuttle.database.App
import shuttle.database.AppQueries
import shuttle.database.util.suspendTransaction

interface AppDataSource {

    fun findAllApps(): Flow<List<App>>

    suspend fun insert(apps: List<App>)
    suspend fun delete(apps: List<App>)
}

@Factory
internal class AppDataSourceImpl(
    private val appQueries: AppQueries,
    private val ioDispatcher: CoroutineDispatcher
) : AppDataSource {

    override fun findAllApps(): Flow<List<App>> = appQueries.findAllApps().asFlow().mapToList(ioDispatcher)

    override suspend fun insert(apps: List<App>) {
        appQueries.suspendTransaction(ioDispatcher) {
            for (app in apps) {
                insertApp(app.id, app.name)
            }
        }
    }

    override suspend fun delete(apps: List<App>) {
        appQueries.suspendTransaction(ioDispatcher) {
            deleteApps(apps.map { it.id })
        }
    }
}
