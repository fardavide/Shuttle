package shuttle.database

import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import kotlinx.coroutines.Dispatchers
import org.koin.core.scope.Scope
import org.koin.dsl.module
import shuttle.database.adapter.databaseAdaptersModule
import shuttle.database.datasource.StatDataSource
import shuttle.database.datasource.StatDataSourceImpl
import studio.forface.shuttle.database.Database

val databaseModule = module {

    single<SqlDriver> { AndroidSqliteDriver(context = get(), schema = Database.Schema, name = "shuttle.db") }
    single {
        val driver: SqlDriver = get()
        Database(driver = driver, appAdapter = get(), locationStatAdapter = get(), timeStatAdapter = get())
    }

    factory { database.statQueries }

    factory<StatDataSource> { StatDataSourceImpl(statQueries = get(), ioDispatcher = Dispatchers.IO) }
} + databaseAdaptersModule

private val Scope.database get() = get<Database>()
