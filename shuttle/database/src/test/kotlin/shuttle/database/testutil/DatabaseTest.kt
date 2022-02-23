package shuttle.database.testutil

import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import studio.forface.shuttle.database.Database
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

abstract class DatabaseTest {

    private val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
    protected val database = Database(
        driver = driver,
        appAdapter = AppAdapter,
        locationStatAdapter = LocationStatAdapter,
        timeStatAdapter = TimeStatAdapter
    )

    @BeforeTest
    fun setup() {
        Database.Schema.create(driver)
    }

    @AfterTest
    fun tearDown() {
        driver.close()
    }
}
