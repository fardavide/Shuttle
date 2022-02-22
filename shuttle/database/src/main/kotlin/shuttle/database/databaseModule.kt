package shuttle.database

import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import org.koin.dsl.module
import studio.forface.shuttle.database.Database

val databaseModule = module {

    single<SqlDriver> { AndroidSqliteDriver(context = get(), schema = Database.Schema) }
}
