package shuttle.database

import shuttle.database.testdata.DatabaseAppIdSample
import shuttle.database.testdata.DatabaseDateSample
import shuttle.database.testdata.DatabaseGeoHashSample
import shuttle.database.testdata.DatabaseTimeSample
import shuttle.database.testutil.DatabaseTest
import kotlin.test.Test
import kotlin.test.assertContentEquals

class StatQueriesTest : DatabaseTest() {

    private val queries = database.statQueries

    @Test
    fun `returns all stats matching the current time or location`() {
        // given
        val expected = listOf(
            Stat(
                appId = DatabaseAppIdSample.Chrome,
                geoHash = DatabaseGeoHashSample.Home,
                date = DatabaseDateSample.Today,
                time = DatabaseTimeSample.Evening
            ),
            Stat(
                appId = DatabaseAppIdSample.CineScout,
                geoHash = DatabaseGeoHashSample.Bennet,
                date = DatabaseDateSample.Today,
                time = DatabaseTimeSample.Noon
            )
        )
        queries.insertStat(
            appId = DatabaseAppIdSample.Chrome,
            geoHash = DatabaseGeoHashSample.Home,
            date = DatabaseDateSample.Today,
            time = DatabaseTimeSample.Evening
        )
        queries.insertStat(
            appId = DatabaseAppIdSample.CineScout,
            geoHash = DatabaseGeoHashSample.Bennet,
            date = DatabaseDateSample.Today,
            time = DatabaseTimeSample.Noon
        )
        queries.insertStat(
            appId = DatabaseAppIdSample.GitHub,
            geoHash = DatabaseGeoHashSample.Bennet,
            date = DatabaseDateSample.Today,
            time = DatabaseTimeSample.Evening
        )

        // when
        val result = queries.findAllStatsByGeoHashAndTime(
            startTime = DatabaseTimeSample.Morning,
            endTime = DatabaseTimeSample.Afternoon,
            geoHash = DatabaseGeoHashSample.Home
        ).executeAsList()

        // then
        assertContentEquals(expected, result)
    }
}
