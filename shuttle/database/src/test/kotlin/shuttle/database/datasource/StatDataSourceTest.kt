package shuttle.database.datasource

import app.cash.turbine.test
import arrow.core.Option
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import shuttle.database.Stat
import shuttle.database.testdata.TestData.Date
import shuttle.database.testdata.TestData.ExactTime
import shuttle.database.testdata.TestData.FirstAppId
import shuttle.database.testdata.TestData.GeoHash
import shuttle.database.testdata.TestData.RangeEndTime
import shuttle.database.testdata.TestData.RangeStartTime
import shuttle.database.testutil.DatabaseTest
import kotlin.test.Test
import kotlin.test.assertEquals

class StatDataSourceTest : DatabaseTest() {

    private val dispatcher = StandardTestDispatcher()
    private val queries = spyk(database.statQueries)
    private val dataSource = StatDataSourceImpl(
        statQueries = queries,
        ioDispatcher = dispatcher
    )

    @Test
    fun `store stat correctly`() = runTest(dispatcher) {
        // when
        dataSource.insertOpenStats(
            appId = FirstAppId,
            date = Date,
            geoHash = Option(GeoHash),
            time = ExactTime
        )

        // then
        verify { queries.insertStat(FirstAppId, GeoHash, Date, ExactTime) }
    }

    @Test
    fun `store and find many stats`() = runTest(dispatcher) {
        // given
        val expected = listOf(
            Stat(FirstAppId, date = Date, geoHash = GeoHash, time = ExactTime),
            Stat(FirstAppId, date = Date, geoHash = GeoHash, time = RangeStartTime),
            Stat(FirstAppId, date = Date, geoHash = GeoHash, time = RangeEndTime)
        )

        // when
        dataSource.insertOpenStats(
            appId = FirstAppId,
            date = Date,
            geoHash = Option(GeoHash),
            time = ExactTime
        )
        dataSource.insertOpenStats(
            appId = FirstAppId,
            date = Date,
            geoHash = Option(GeoHash),
            time = RangeStartTime
        )
        dataSource.insertOpenStats(
            appId = FirstAppId,
            date = Date,
            geoHash = Option(GeoHash),
            time = RangeEndTime
        )

        // then
        dataSource.findAllStats(
            geoHash = Option(GeoHash),
            startTime = RangeStartTime,
            endTime = RangeEndTime
        ).test {

            assertEquals(expected, awaitItem())
        }
    }
}
