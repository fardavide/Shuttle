package shuttle.database

import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import kotlinx.coroutines.test.runTest
import studio.forface.shuttle.database.Database
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class StatQueriesTest {

    private val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
    private val queries = Database(driver).statQueries

    @BeforeTest
    fun setup() {
        Database.Schema.create(driver)
    }

    @AfterTest
    fun tearDown() {
        driver.close()
    }


    @Test
    fun `one time and one location, for every app`() = runTest {
        // given
        insertApps(3)
        val expected = listOf(SecondAppId, FirstAppId, ThirdAppId)

        // when
        with(queries) {
            insertTimeStat(FirstAppId, time = Time, count = 5)
            insertLocationStat(FirstAppId, latitude = Latitude, longitude = Longitude, count = 4)
            insertTimeStat(SecondAppId, time = Time, count = 6)
            insertLocationStat(SecondAppId, latitude = Latitude, longitude = Longitude, count = 8)
            insertTimeStat(ThirdAppId, time = Time, count = 2)
            insertLocationStat(ThirdAppId, latitude = Latitude, longitude = Longitude, count = 1)

            val result = findMostUsedAppsIds(
                latitude = Latitude,
                latitude_ = Latitude,
                longitude = Longitude,
                longitude_ = Longitude,
                time = Time,
                time_ = Time
            ).executeAsList()

            // then
            assertEquals(expected, result)
        }
    }

    @Test
    fun `one or two of time and location, for every app`() = runTest {
        // given
        insertApps(3)
        val expected = listOf(SecondAppId, FirstAppId, ThirdAppId)

        // when
        with(queries) {
            insertTimeStat(FirstAppId, time = Time, count = 5)
            insertLocationStat(SecondAppId, latitude = Latitude, longitude = Longitude, count = 8)
            insertTimeStat(ThirdAppId, time = Time, count = 2)
            insertLocationStat(ThirdAppId, latitude = Latitude, longitude = Longitude, count = 1)

            val result = findMostUsedAppsIds(
                latitude = Latitude,
                latitude_ = Latitude,
                longitude = Longitude,
                longitude_ = Longitude,
                time = Time,
                time_ = Time
            ).executeAsList()

            // then
            assertEquals(expected, result)
        }
    }

    @Test
    fun `one or zero of time and location, for every app`() = runTest {
        // given
        insertApps(3)
        val expected = listOf(SecondAppId, ThirdAppId, FirstAppId)

        // when
        with(queries) {
            insertLocationStat(SecondAppId, latitude = Latitude, longitude = Longitude, count = 8)
            insertTimeStat(ThirdAppId, time = Time, count = 2)

            val result = findMostUsedAppsIds(
                latitude = Latitude,
                latitude_ = Latitude,
                longitude = Longitude,
                longitude_ = Longitude,
                time = Time,
                time_ = Time
            ).executeAsList()

            // then
            assertEquals(expected, result)
        }
    }

    private suspend fun insertApps(count: Int? = null) {
        repeat(count ?: AllAppsIds.size) { index ->
            queries.insertApp(AllAppsIds[index])
        }
    }

    companion object TestData {

        const val Time = 100L
        const val Latitude = 20.0
        const val Longitude = 30.0

        const val FirstAppId = "app 1"
        const val SecondAppId = "app 2"
        const val ThirdAppId = "app 3"
        const val FourthAppId = "app 4"
        const val FifthAppId = "app 5"

        val AllAppsIds = listOf(
            FirstAppId,
            SecondAppId,
            ThirdAppId,
            FourthAppId,
            FifthAppId
        )
    }
}
