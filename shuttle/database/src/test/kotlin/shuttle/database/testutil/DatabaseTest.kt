package shuttle.database.testutil

import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import shuttle.database.Database
import shuttle.database.testdata.DatabaseAppIdSample
import shuttle.database.util.suspendTransaction
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

@Suppress("UnnecessaryAbstractClass")
abstract class DatabaseTest {

    private val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
    protected val database = Database(
        driver = driver,
        appAdapter = AppAdapter,
        appBlacklistSettingAdapter = AppBlacklistSettingAdapter,
        lastLocationAdapter = LastLocationAdapter,
        statAdapter = StatAdapter,
        suggestionCacheAdapter = SuggestionCacheAdapter
    )

    @BeforeTest
    fun setup() {
        Database.Schema.create(driver)
    }

    @AfterTest
    fun tearDown() {
        driver.close()
    }

    suspend fun insertApps(count: Int = DatabaseAppIdSample.all().size) {
        database.appQueries.suspendTransaction(UnconfinedTestDispatcher()) {
            repeat(count) { index ->
                val id = DatabaseAppIdSample.all()[index]
                val name = id.value
                insertApp(id, name)
            }
        }
    }
}
