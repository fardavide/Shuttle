package shuttle.database

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module(
    includes = [
        DatabaseAdaptersModule::class,
        DatabaseQueriesModule::class
    ]
)
@ComponentScan
class DatabaseModule {

    @Single
    fun sqlDriver(context: Context): SqlDriver =
        AndroidSqliteDriver(context = context, schema = Database.Schema, name = "shuttle.db")

    @Single
    fun database(
        sqlDriver: SqlDriver,
        appAdapter: App.Adapter,
        appBlacklistSettingsAdapter: AppBlacklistSetting.Adapter,
        counterAdapter: Counter.Adapter,
        lastLocationAdapter: LastLocation.Adapter,
        statAdapter: Stat.Adapter,
        suggestionCacheAdapter: SuggestionCache.Adapter
    ) = Database(
        driver = sqlDriver,
        appAdapter = appAdapter,
        appBlacklistSettingAdapter = appBlacklistSettingsAdapter,
        counterAdapter = counterAdapter,
        lastLocationAdapter = lastLocationAdapter,
        statAdapter = statAdapter,
        suggestionCacheAdapter = suggestionCacheAdapter
    )
}
