package shuttle.database.testutil

import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import shuttle.database.testdata.TestData.AllAppsIds
import shuttle.database.util.suspendTransaction
import studio.forface.shuttle.database.Database
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

abstract class DatabaseTest {

    private val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
    protected val database = Database(
        driver = driver,
        appAdapter = AppAdapter,
        appBlacklistSettingAdapter = AppBlacklistSettingAdapter,
        lastLocationAdapter = LastLocationAdapter,
        statAdapter = StatAdapter
    )

    @BeforeTest
    fun setup() {
        Database.Schema.create(driver)
    }

    @AfterTest
    fun tearDown() {
        driver.close()
    }

    suspend fun insertApps(count: Int = AllAppsIds.size) {
        database.appQueries.suspendTransaction(UnconfinedTestDispatcher()) {
            repeat(count) { index ->
                val id = AllAppsIds[index]
                val name = id.value
                insertApp(id, name)
            }
        }
    }
}
