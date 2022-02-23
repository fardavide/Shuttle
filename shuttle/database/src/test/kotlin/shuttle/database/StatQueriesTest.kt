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
    fun `one time and one location, with exact values, for every app`() = runTest {
        // given
        insertApps(3)
        val expected = listOf(SecondAppId, FirstAppId, ThirdAppId)

        // when
        with(queries) {
            insertTimeStat(FirstAppId, time = ExactTime, count = 5)
            insertLocationStat(FirstAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 4)
            insertTimeStat(SecondAppId, time = ExactTime, count = 6)
            insertLocationStat(SecondAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 8)
            insertTimeStat(ThirdAppId, time = ExactTime, count = 2)
            insertLocationStat(ThirdAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 1)

            val result = findMostUsedAppsIds(
                latitude = ExactLatitude,
                latitude_ = ExactLatitude,
                longitude = ExactLongitude,
                longitude_ = ExactLongitude,
                time = ExactTime,
                time_ = ExactTime
            ).executeAsList()

            // then
            assertEquals(expected, result)
        }
    }

    @Test
    fun `one or two of time and location, with exact values, for every app`() = runTest {
        // given
        insertApps(3)
        val expected = listOf(SecondAppId, FirstAppId, ThirdAppId)

        // when
        with(queries) {
            insertTimeStat(FirstAppId, time = ExactTime, count = 5)
            insertLocationStat(SecondAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 8)
            insertTimeStat(ThirdAppId, time = ExactTime, count = 2)
            insertLocationStat(ThirdAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 1)

            val result = findMostUsedAppsIds(
                latitude = ExactLatitude,
                latitude_ = ExactLatitude,
                longitude = ExactLongitude,
                longitude_ = ExactLongitude,
                time = ExactTime,
                time_ = ExactTime
            ).executeAsList()

            // then
            assertEquals(expected, result)
        }
    }

    @Test
    fun `one or zero of time and location, with exact values, for every app`() = runTest {
        // given
        insertApps(3)
        val expected = listOf(SecondAppId, ThirdAppId, FirstAppId)

        // when
        with(queries) {
            insertLocationStat(SecondAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 8)
            insertTimeStat(ThirdAppId, time = ExactTime, count = 2)

            val result = findMostUsedAppsIds(
                latitude = ExactLatitude,
                latitude_ = ExactLatitude,
                longitude = ExactLongitude,
                longitude_ = ExactLongitude,
                time = ExactTime,
                time_ = ExactTime
            ).executeAsList()

            // then
            assertEquals(expected, result)
        }
    }

    @Test
    fun `one time and one location, with range values, for every app`() = runTest {
        // given
        insertApps(3)
        val expected = listOf(SecondAppId, FirstAppId, ThirdAppId)

        // when
        with(queries) {
            insertTimeStat(FirstAppId, time = ExactTime, count = 5)
            insertLocationStat(FirstAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 4)
            insertTimeStat(SecondAppId, time = ExactTime, count = 6)
            insertLocationStat(SecondAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 8)
            insertTimeStat(ThirdAppId, time = ExactTime, count = 2)
            insertLocationStat(ThirdAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 1)

            val result = findMostUsedAppsIds(
                latitude = RangeStartLatitude,
                latitude_ = RangeEndLatitude,
                longitude = RangeStartLongitude,
                longitude_ = RangeEndLongitude,
                time = RangeStartTime,
                time_ = RangeEndTime
            ).executeAsList()

            // then
            assertEquals(expected, result)
        }
    }

    @Test
    fun `one or two of time and location, with range values, for every app`() = runTest {
        // given
        insertApps(3)
        val expected = listOf(SecondAppId, FirstAppId, ThirdAppId)

        // when
        with(queries) {
            insertTimeStat(FirstAppId, time = ExactTime, count = 5)
            insertLocationStat(SecondAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 8)
            insertTimeStat(ThirdAppId, time = ExactTime, count = 2)
            insertLocationStat(ThirdAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 1)

            val result = findMostUsedAppsIds(
                latitude = RangeStartLatitude,
                latitude_ = RangeEndLatitude,
                longitude = RangeStartLongitude,
                longitude_ = RangeEndLongitude,
                time = RangeStartTime,
                time_ = RangeEndTime
            ).executeAsList()

            // then
            assertEquals(expected, result)
        }
    }

    @Test
    fun `one or zero of time and location, with range values, for every app`() = runTest {
        // given
        insertApps(3)
        val expected = listOf(SecondAppId, ThirdAppId, FirstAppId)

        // when
        with(queries) {
            insertLocationStat(SecondAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 8)
            insertTimeStat(ThirdAppId, time = ExactTime, count = 2)

            val result = findMostUsedAppsIds(
                latitude = RangeStartLatitude,
                latitude_ = RangeEndLatitude,
                longitude = RangeStartLongitude,
                longitude_ = RangeEndLongitude,
                time = RangeStartTime,
                time_ = RangeEndTime
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

        const val ExactTime = 100L
        const val RangeStartTime = 90L
        const val RangeEndTime = 110L
        const val ExactLatitude = 20.0
        const val RangeStartLatitude = 10.0
        const val RangeEndLatitude = 30.0
        const val ExactLongitude = 30.0
        const val RangeStartLongitude = 20.0
        const val RangeEndLongitude = 40.0

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
