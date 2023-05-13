package shuttle.database

import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import kotlinx.coroutines.Dispatchers
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.dsl.module
import org.koin.ksp.generated.module
import shuttle.database.adapter.databaseAdaptersModule
import shuttle.database.datasource.AppDataSource
import shuttle.database.datasource.AppDataSourceImpl
import shuttle.database.datasource.LastLocationDataSource
import shuttle.database.datasource.LastLocationDataSourceImpl
import shuttle.database.datasource.SettingDataSource
import shuttle.database.datasource.SettingDataSourceImpl
import shuttle.database.datasource.StatDataSource
import shuttle.database.datasource.StatDataSourceImpl

val databaseModule = module {
    includes(DatabaseModule().module)

    single<SqlDriver> { AndroidSqliteDriver(context = get(), schema = Database.Schema, name = "shuttle.db") }
    single {
        val driver: SqlDriver = get()
        Database(
            driver = driver,
            appAdapter = get(),
            appBlacklistSettingAdapter = get(),
            lastLocationAdapter = get(),
            statAdapter = get()
        )
    }

    factory<AppDataSource> { AppDataSourceImpl(appQueries = get(), ioDispatcher = Dispatchers.IO) }
    factory<LastLocationDataSource> {
        LastLocationDataSourceImpl(
            lastLocationQueries = get(),
            ioDispatcher = Dispatchers.IO
        )
    }
    factory<SettingDataSource> {
        SettingDataSourceImpl(
            appBlacklistSettingQueries = get(),
            ioDispatcher = Dispatchers.IO
        )
    }
    factory<StatDataSource> { StatDataSourceImpl(statQueries = get(), ioDispatcher = Dispatchers.IO) }
} + databaseAdaptersModule

@Module(
    includes = [
        DatabaseQueriesModule::class
    ]
)
@ComponentScan
class DatabaseModule
