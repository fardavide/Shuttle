package shuttle.database

import shuttle.database.testdata.TestData.AnotherGeoHash
import shuttle.database.testdata.TestData.AnotherTime
import shuttle.database.testdata.TestData.Date
import shuttle.database.testdata.TestData.ExactTime
import shuttle.database.testdata.TestData.FirstAppId
import shuttle.database.testdata.TestData.GeoHash
import shuttle.database.testdata.TestData.RangeEndTime
import shuttle.database.testdata.TestData.RangeStartTime
import shuttle.database.testdata.TestData.SecondAppId
import shuttle.database.testdata.TestData.ThirdAppId
import shuttle.database.testutil.DatabaseTest
import kotlin.test.Test
import kotlin.test.assertContentEquals

class StatQueriesTest : DatabaseTest() {

    private val queries = database.statQueries

    @Test
    fun `returns all stats matching the current time or location`() {
        // given
        val expected = listOf(
            Stat(FirstAppId, GeoHash, Date, AnotherTime),
            Stat(SecondAppId, AnotherGeoHash, Date, ExactTime)
        )
        queries.insertStat(FirstAppId, GeoHash, Date, AnotherTime)
        queries.insertStat(SecondAppId, AnotherGeoHash, Date, ExactTime)
        queries.insertStat(ThirdAppId, AnotherGeoHash, Date, AnotherTime)

        // when
        val result = queries.findAllStatsByGeoHash(RangeStartTime, RangeEndTime, GeoHash).executeAsList()

        // then
        assertContentEquals(expected, result)
    }
}
