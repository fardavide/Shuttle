package shuttle.database.datasource

import app.cash.turbine.test
import arrow.core.Option
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import shuttle.database.Stat
import shuttle.database.StatQueries
import shuttle.database.testdata.DatabaseAppIdSample
import shuttle.database.testdata.DatabaseDateSample
import shuttle.database.testdata.DatabaseGeoHashSample
import shuttle.database.testdata.DatabaseTimeSample
import shuttle.database.testutil.DatabaseTest
import kotlin.test.Test
import kotlin.test.assertEquals

class StatDataSourceTest : DatabaseTest() {

    private val dispatcher = StandardTestDispatcher()
    private val queries: StatQueries = spyk(database.statQueries)
    private val dataSource = StatDataSourceImpl(
        statQueries = queries,
        ioDispatcher = dispatcher
    )

    @Test
    fun `store stat correctly`() = runTest(dispatcher) {
        // when
        dataSource.insertOpenStats(
            appId = DatabaseAppIdSample.Chrome,
            date = DatabaseDateSample.Today,
            geoHash = Option(DatabaseGeoHashSample.Home),
            time = DatabaseTimeSample.Noon
        )

        // then
        verify {
            queries.insertStat(
                appId = DatabaseAppIdSample.Chrome,
                geoHash = DatabaseGeoHashSample.Home,
                date = DatabaseDateSample.Today,
                time = DatabaseTimeSample.Noon
            )
        }
    }

    @Test
    fun `store and find many stats`() = runTest(dispatcher) {
        // given
        val expected = listOf(
            Stat(
                DatabaseAppIdSample.Chrome,
                date = DatabaseDateSample.Today,
                geoHash = DatabaseGeoHashSample.Home,
                time = DatabaseTimeSample.Noon
            ),
            Stat(
                DatabaseAppIdSample.Chrome,
                date = DatabaseDateSample.Today,
                geoHash = DatabaseGeoHashSample.Home,
                time = DatabaseTimeSample.Morning
            ),
            Stat(
                DatabaseAppIdSample.Chrome,
                date = DatabaseDateSample.Today,
                geoHash = DatabaseGeoHashSample.Home,
                time = DatabaseTimeSample.Afternoon
            )
        )

        // when
        dataSource.insertOpenStats(
            appId = DatabaseAppIdSample.Chrome,
            date = DatabaseDateSample.Today,
            geoHash = Option(DatabaseGeoHashSample.Home),
            time = DatabaseTimeSample.Noon
        )
        dataSource.insertOpenStats(
            appId = DatabaseAppIdSample.Chrome,
            date = DatabaseDateSample.Today,
            geoHash = Option(DatabaseGeoHashSample.Home),
            time = DatabaseTimeSample.Morning
        )
        dataSource.insertOpenStats(
            appId = DatabaseAppIdSample.Chrome,
            date = DatabaseDateSample.Today,
            geoHash = Option(DatabaseGeoHashSample.Home),
            time = DatabaseTimeSample.Afternoon
        )

        // then
        dataSource.findAllStats(
            geoHash = Option(DatabaseGeoHashSample.Home),
            startTime = DatabaseTimeSample.Morning,
            endTime = DatabaseTimeSample.Afternoon
        ).test {
            assertEquals(expected, awaitItem())
        }
    }
}
