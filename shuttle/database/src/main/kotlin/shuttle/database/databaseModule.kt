package shuttle.database

import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.dsl.module
import org.koin.ksp.generated.module

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
}

@Module(
    includes = [
        DatabaseAdaptersModule::class,
        DatabaseQueriesModule::class
    ]
)
@ComponentScan
class DatabaseModule
