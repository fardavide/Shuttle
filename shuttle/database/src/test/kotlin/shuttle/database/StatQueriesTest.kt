package shuttle.database

import kotlinx.coroutines.test.runTest
import shuttle.database.testdata.TestData.ExactLatitude
import shuttle.database.testdata.TestData.ExactLongitude
import shuttle.database.testdata.TestData.ExactTime
import shuttle.database.testdata.TestData.FirstAppId
import shuttle.database.testdata.TestData.RangeEndLatitude
import shuttle.database.testdata.TestData.RangeEndLongitude
import shuttle.database.testdata.TestData.RangeEndTime
import shuttle.database.testdata.TestData.RangeMidFirstTime
import shuttle.database.testdata.TestData.RangeMidSecondTime
import shuttle.database.testdata.TestData.RangeStartLatitude
import shuttle.database.testdata.TestData.RangeStartLongitude
import shuttle.database.testdata.TestData.RangeStartTime
import shuttle.database.testdata.TestData.SecondAppId
import shuttle.database.testdata.TestData.ThirdAppId
import shuttle.database.testutil.DatabaseTest
import kotlin.test.Test
import kotlin.test.assertEquals

class StatQueriesTest : DatabaseTest() {

    private val queries = database.statQueries

    @Test
    fun `update location stat for same value`() = runTest {
        // given
        val expected = 13L
        queries.insertLocationStat(FirstAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 2)
        queries.insertLocationStat(FirstAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = expected)

        // when
        val result = queries.findLocationStat(FirstAppId, latitude = ExactLatitude, longitude = ExactLongitude)
            .executeAsOne()
            .count

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `update time stat for same value`() = runTest {
        // given
        val expected = 13L
        queries.insertTimeStat(FirstAppId, time = ExactTime, count = 2)
        queries.insertTimeStat(FirstAppId, time = ExactTime, count = expected)

        // when
        val result = queries.findTimeStat(FirstAppId, time = ExactTime)
            .executeAsOne()
            .count

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `does not update location stat for different latitude`() = runTest {
        // given
        val expectedFirst = 13L
        val expectedSecond = 2L
        queries.insertLocationStat(FirstAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = expectedFirst)
        queries.insertLocationStat(FirstAppId, latitude = RangeEndLatitude, longitude = ExactLongitude, count = expectedSecond)

        // when
        val resultFirst = queries.findLocationStat(FirstAppId, latitude = ExactLatitude, longitude = ExactLongitude)
            .executeAsOne()
            .count
        val resultSecond = queries.findLocationStat(FirstAppId, latitude = RangeEndLatitude, longitude = ExactLongitude)
            .executeAsOne()
            .count

        // then
        assertEquals(expectedFirst, resultFirst)
        assertEquals(expectedSecond, resultSecond)
    }

    @Test
    fun `does not update location stat for different longitude`() = runTest {
        // given
        val expectedFirst = 13L
        val expectedSecond = 2L
        queries.insertLocationStat(FirstAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = expectedFirst)
        queries.insertLocationStat(FirstAppId, latitude = ExactLatitude, longitude = RangeEndLongitude, count = expectedSecond)

        // when
        val resultFirst = queries.findLocationStat(FirstAppId, latitude = ExactLatitude, longitude = ExactLongitude)
            .executeAsOne()
            .count
        val resultSecond = queries.findLocationStat(FirstAppId, latitude = ExactLatitude, longitude = RangeEndLongitude)
            .executeAsOne()
            .count

        // then
        assertEquals(expectedFirst, resultFirst)
        assertEquals(expectedSecond, resultSecond)
    }

    @Test
    fun `does not update time stat for different value`() = runTest {
        // given
        val expected = 13L
        queries.insertTimeStat(FirstAppId, time = ExactTime, count = expected)
        queries.insertTimeStat(FirstAppId, time = RangeEndTime, count = 2)

        // when
        val result = queries.findTimeStat(FirstAppId, time = ExactTime)
            .executeAsOne()
            .count

        // then
        assertEquals(expected, result)
    }

//    @Test
//    fun `sort by location first, then by time`() = runTest {
//        // given
//        insertApps(5)
//        val expected = listOf(FifthAppId, FourthAppId, ThirdAppId, SecondAppId, FirstAppId)
//
//        // when
//        with(queries) {
//            insertTimeStat(FirstAppId, time = ExactTime, count = 5)
//            insertTimeStat(SecondAppId, time = ExactTime, count = 6)
//            insertTimeStat(ThirdAppId, time = ExactTime, count = 2)
//            insertLocationStat(ThirdAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 2)
//            insertTimeStat(FourthAppId, time = ExactTime, count = 4)
//            insertLocationStat(FourthAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 2)
//            insertTimeStat(FifthAppId, time = ExactTime, count = 10)
//            insertLocationStat(FifthAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 3)
//
//            val result = findMostUsedAppsIds(
//                startLatitude = ExactLatitude,
//                endLatitude = ExactLatitude,
//                startLongitude = ExactLongitude,
//                endLongitude = ExactLongitude,
//                startTime = ExactTime,
//                endTime = ExactTime
//            ).executeAsList()
//
//            // then
//            assertEquals(expected, result)
//        }
//    }

//    @Test
//    fun `sort by location only`() = runTest {
//        // given
//        insertApps(5)
//        val expected = listOf(FifthAppId, FourthAppId, SecondAppId, FirstAppId, ThirdAppId)
//
//        // when
//        with(queries) {
//            insertLocationStat(FirstAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 2)
//            insertLocationStat(SecondAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 3)
//            insertLocationStat(ThirdAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 1)
//            insertLocationStat(FourthAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 4)
//            insertLocationStat(FifthAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 5)
//
//            val result = findMostUsedAppsIds(
//                startLatitude = ExactLatitude,
//                endLatitude = ExactLatitude,
//                startLongitude = ExactLongitude,
//                endLongitude = ExactLongitude,
//                startTime = ExactTime,
//                endTime = ExactTime
//            ).executeAsList()
//
//            // then
//            assertEquals(expected, result)
//        }
//    }

//    @Test
//    fun `sort by time only`() = runTest {
//        // given
//        insertApps(5)
//        val expected = listOf(FifthAppId, FourthAppId, SecondAppId, FirstAppId, ThirdAppId)
//
//        // when
//        with(queries) {
//            insertTimeStat(FirstAppId, time = ExactTime, count = 2)
//            insertTimeStat(SecondAppId, time = ExactTime, count = 3)
//            insertTimeStat(ThirdAppId, time = ExactTime, count = 1)
//            insertTimeStat(FourthAppId, time = ExactTime, count = 4)
//            insertTimeStat(FifthAppId, time = ExactTime, count = 5)
//
//            val result = findMostUsedAppsIds(
//                startLatitude = ExactLatitude,
//                endLatitude = ExactLatitude,
//                startLongitude = ExactLongitude,
//                endLongitude = ExactLongitude,
//                startTime = ExactTime,
//                endTime = ExactTime
//            ).executeAsList()
//
//            // then
//            assertEquals(expected, result)
//        }
//    }

//    @Test
//    fun `one time and one location, with exact values, for every app`() = runTest {
//        // given
//        insertApps(3)
//        val expected = listOf(SecondAppId, FirstAppId, ThirdAppId)
//
//        // when
//        with(queries) {
//            insertTimeStat(FirstAppId, time = ExactTime, count = 5)
//            insertLocationStat(FirstAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 4)
//            insertTimeStat(SecondAppId, time = ExactTime, count = 6)
//            insertLocationStat(SecondAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 8)
//            insertTimeStat(ThirdAppId, time = ExactTime, count = 2)
//            insertLocationStat(ThirdAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 1)
//
//            val result = findMostUsedAppsIds(
//                startLatitude = ExactLatitude,
//                endLatitude = ExactLatitude,
//                startLongitude = ExactLongitude,
//                endLongitude = ExactLongitude,
//                startTime = ExactTime,
//                endTime = ExactTime
//            ).executeAsList()
//
//            // then
//            assertEquals(expected, result)
//        }
//    }

//    @Test
//    fun `one or two of time and location, with exact values, for every app`() = runTest {
//        // given
//        insertApps(3)
//        val expected = listOf(SecondAppId, ThirdAppId, FirstAppId)
//
//        // when
//        with(queries) {
//            insertTimeStat(FirstAppId, time = ExactTime, count = 5)
//            insertLocationStat(SecondAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 8)
//            insertTimeStat(ThirdAppId, time = ExactTime, count = 2)
//            insertLocationStat(ThirdAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 1)
//
//            val result = findMostUsedAppsIds(
//                startLatitude = ExactLatitude,
//                endLatitude = ExactLatitude,
//                startLongitude = ExactLongitude,
//                endLongitude = ExactLongitude,
//                startTime = ExactTime,
//                endTime = ExactTime
//            ).executeAsList()
//
//            // then
//            assertEquals(expected, result)
//        }
//    }

//    @Test
//    fun `one or zero of time and location, with exact values, for every app`() = runTest {
//        // given
//        insertApps(3)
//        val expected = listOf(SecondAppId, ThirdAppId, FirstAppId)
//
//        // when
//        with(queries) {
//            insertLocationStat(SecondAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 8)
//            insertTimeStat(ThirdAppId, time = ExactTime, count = 2)
//
//            val result = findMostUsedAppsIds(
//                startLatitude = ExactLatitude,
//                endLatitude = ExactLatitude,
//                startLongitude = ExactLongitude,
//                endLongitude = ExactLongitude,
//                startTime = ExactTime,
//                endTime = ExactTime
//            ).executeAsList()
//
//            // then
//            assertEquals(expected, result)
//        }
//    }

//    @Test
//    fun `one time and one location, with range values, for every app`() = runTest {
//        // given
//        insertApps(3)
//        val expected = listOf(SecondAppId, FirstAppId, ThirdAppId)
//
//        // when
//        with(queries) {
//            insertTimeStat(FirstAppId, time = ExactTime, count = 5)
//            insertLocationStat(FirstAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 4)
//            insertTimeStat(SecondAppId, time = ExactTime, count = 6)
//            insertLocationStat(SecondAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 8)
//            insertTimeStat(ThirdAppId, time = ExactTime, count = 2)
//            insertLocationStat(ThirdAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 1)
//
//            val result = findMostUsedAppsIds(
//                startLatitude = RangeStartLatitude,
//                endLatitude = RangeEndLatitude,
//                startLongitude = RangeStartLongitude,
//                endLongitude = RangeEndLongitude,
//                startTime = RangeStartTime,
//                endTime = RangeEndTime
//            ).executeAsList()
//
//            // then
//            assertEquals(expected, result)
//        }
//    }

//    @Test
//    fun `one or two of time and location, with range values, for every app`() = runTest {
//        // given
//        insertApps(3)
//        val expected = listOf(SecondAppId, ThirdAppId, FirstAppId)
//
//        // when
//        with(queries) {
//            insertTimeStat(FirstAppId, time = ExactTime, count = 5)
//            insertLocationStat(SecondAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 8)
//            insertTimeStat(ThirdAppId, time = ExactTime, count = 2)
//            insertLocationStat(ThirdAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 1)
//
//            val result = findMostUsedAppsIds(
//                startLatitude = RangeStartLatitude,
//                endLatitude = RangeEndLatitude,
//                startLongitude = RangeStartLongitude,
//                endLongitude = RangeEndLongitude,
//                startTime = RangeStartTime,
//                endTime = RangeEndTime
//            ).executeAsList()
//
//            // then
//            assertEquals(expected, result)
//        }
//    }

//    @Test
//    fun `one or zero of time and location, with range values, for every app`() = runTest {
//        // given
//        insertApps(3)
//        val expected = listOf(SecondAppId, ThirdAppId, FirstAppId)
//
//        // when
//        with(queries) {
//            insertLocationStat(SecondAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 8)
//            insertTimeStat(ThirdAppId, time = ExactTime, count = 2)
//
//            val result = findMostUsedAppsIds(
//                startLatitude = RangeStartLatitude,
//                endLatitude = RangeEndLatitude,
//                startLongitude = RangeStartLongitude,
//                endLongitude = RangeEndLongitude,
//                startTime = RangeStartTime,
//                endTime = RangeEndTime
//            ).executeAsList()
//
//            // then
//            assertEquals(expected, result)
//        }
//    }

    @Test
    fun `find all stats test`() = runTest {
        // given
        val expected = listOf(
            FindAllStats(null, FirstAppId, countByLocation = 0, countByTime = 4),
            FindAllStats(null, SecondAppId, countByLocation = 0, countByTime = 5),
            FindAllStats(null, ThirdAppId, countByLocation = 0, countByTime = 3),
            FindAllStats(FirstAppId, null, countByLocation = 5, countByTime = 0),
            FindAllStats(SecondAppId, null, countByLocation = 2, countByTime = 0),
            FindAllStats(ThirdAppId, null, countByLocation = 5, countByTime = 0),
        )

        // when
        with(queries) {
            // First App: 3 + 2 = 5
            insertLocationStat(FirstAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 3)
            insertLocationStat(FirstAppId, latitude = RangeStartLatitude, longitude = ExactLongitude, count = 2)
            // Second App: 2 * 1 = 2
            insertLocationStat(SecondAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 2)
            // Third App: 5 * 1 = 5
            insertLocationStat(ThirdAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 1)
            insertLocationStat(ThirdAppId, latitude = RangeStartLatitude, longitude = ExactLongitude, count = 1)
            insertLocationStat(ThirdAppId, latitude = RangeEndLatitude, longitude = ExactLongitude, count = 1)
            insertLocationStat(ThirdAppId, latitude = ExactLatitude, longitude = RangeStartLongitude, count = 1)
            insertLocationStat(ThirdAppId, latitude = ExactLatitude, longitude = RangeEndLongitude, count = 1)

            // First App: 2 * 2 = 4
            insertTimeStat(FirstAppId, time = ExactTime, count = 2)
            insertTimeStat(FirstAppId, time = RangeStartTime, count = 2)
            // Second App: 5 * 1 = 5
            insertTimeStat(SecondAppId, time = ExactTime, count = 1)
            insertTimeStat(SecondAppId, time = RangeStartTime, count = 1)
            insertTimeStat(SecondAppId, time = RangeEndTime, count = 1)
            insertTimeStat(SecondAppId, time = RangeMidFirstTime, count = 1)
            insertTimeStat(SecondAppId, time = RangeMidSecondTime, count = 1)
            // Third App: 3 * 1 = 3
            insertTimeStat(ThirdAppId, time = ExactTime, count = 3)

            val result = findAllStats(
                startLatitude = RangeStartLatitude,
                endLatitude = RangeEndLatitude,
                startLongitude = RangeStartLongitude,
                endLongitude = RangeEndLongitude,
                startTime = RangeStartTime,
                endTime = RangeEndTime
            ).executeAsList()

            // then
            assertEquals(expected, result)
        }
    }

//    @Test
//    fun `find all stats test 2`() = runTest {
//        // given
//        val expected = listOf(
//            FindAllStats2(null, FirstAppId, countByLocation = 0, countByTime = 4),
//            FindAllStats2(null, SecondAppId, countByLocation = 0, countByTime = 5),
//            FindAllStats2(null, ThirdAppId, countByLocation = 0, countByTime = 3),
//            FindAllStats2(FirstAppId, null, countByLocation = 5, countByTime = 0),
//            FindAllStats2(SecondAppId, null, countByLocation = 2, countByTime = 0),
//            FindAllStats2(ThirdAppId, null, countByLocation = 5, countByTime = 0),
//        )
//
//        // when
//        with(queries) {
//            // First App: 3 + 2 = 5
//            insertLocationStat(FirstAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 3)
//            insertLocationStat(FirstAppId, latitude = RangeStartLatitude, longitude = ExactLongitude, count = 2)
//            // Second App: 2 * 1 = 2
//            insertLocationStat(SecondAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 2)
//            // Third App: 5 * 1 = 5
//            insertLocationStat(ThirdAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 1)
//            insertLocationStat(ThirdAppId, latitude = RangeStartLatitude, longitude = ExactLongitude, count = 1)
//            insertLocationStat(ThirdAppId, latitude = RangeEndLatitude, longitude = ExactLongitude, count = 1)
//            insertLocationStat(ThirdAppId, latitude = ExactLatitude, longitude = RangeStartLongitude, count = 1)
//            insertLocationStat(ThirdAppId, latitude = ExactLatitude, longitude = RangeEndLongitude, count = 1)
//
//            // First App: 2 * 2 = 4
//            insertTimeStat(FirstAppId, time = ExactTime, count = 2)
//            insertTimeStat(FirstAppId, time = RangeStartTime, count = 2)
//            // Second App: 5 * 1 = 5
//            insertTimeStat(SecondAppId, time = ExactTime, count = 1)
//            insertTimeStat(SecondAppId, time = RangeStartTime, count = 1)
//            insertTimeStat(SecondAppId, time = RangeEndTime, count = 1)
//            insertTimeStat(SecondAppId, time = RangeMidFirstTime, count = 1)
//            insertTimeStat(SecondAppId, time = RangeMidSecondTime, count = 1)
//            // Third App: 3 * 1 = 3
//            insertTimeStat(ThirdAppId, time = ExactTime, count = 3)
//
//            val result = findAllStats2(
//                startLatitude = RangeStartLatitude,
//                endLatitude = RangeEndLatitude,
//                startLongitude = RangeStartLongitude,
//                endLongitude = RangeEndLongitude,
//                startTime = RangeStartTime,
//                endTime = RangeEndTime
//            ).executeAsList()
//
//            // then
//            assertEquals(expected, result)
//        }
//    }

//    @Test
//    fun `correctly sorts by more location entries in the range`() = runTest {
//        // given
//        val expected = listOf(
//            FindMostUsedAppsIdsTest(SecondAppId, null, countByLocation = 5.0, countByTime = null),
//            FindMostUsedAppsIdsTest(FirstAppId, null, countByLocation = 4.0, countByTime = null),
//            FindMostUsedAppsIdsTest(ThirdAppId, null, countByLocation = 3.0, countByTime = null)
//        )
//
//        // when
//        with(queries) {
//            // First App: 2 * 2 = 4
//            insertLocationStat(FirstAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 2)
//            insertLocationStat(FirstAppId, latitude = RangeStartLatitude, longitude = ExactLongitude, count = 2)
//            // Second App: 5 * 1 = 5
//            insertLocationStat(SecondAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 1)
//            insertLocationStat(SecondAppId, latitude = RangeStartLatitude, longitude = ExactLongitude, count = 1)
//            insertLocationStat(SecondAppId, latitude = RangeEndLatitude, longitude = ExactLongitude, count = 1)
//            insertLocationStat(SecondAppId, latitude = ExactLatitude, longitude = RangeStartLongitude, count = 1)
//            insertLocationStat(SecondAppId, latitude = ExactLatitude, longitude = RangeEndLongitude, count = 1)
//            // Third App: 3 * 1 = 3
//            insertLocationStat(ThirdAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 3)
//
//            val result = findMostUsedAppsIdsTest(
//                startLatitude = RangeStartLatitude,
//                endLatitude = RangeEndLatitude,
//                startLongitude = RangeStartLongitude,
//                endLongitude = RangeEndLongitude,
//                startTime = RangeStartTime,
//                endTime = RangeEndTime
//            ).executeAsList()
//
//            // then
//            assertEquals(expected, result)
//        }
//    }

//    @Test
//    fun `correctly sorts by more time entries in the range`() = runTest {
//        // given
//        val expected = listOf(
//            FindMostUsedAppsIdsTest(null, SecondAppId, countByLocation = null, countByTime = 5.0),
//            FindMostUsedAppsIdsTest(null, FirstAppId, countByLocation = null, countByTime = 4.0),
//            FindMostUsedAppsIdsTest(null, ThirdAppId, countByLocation = null, countByTime = 3.0)
//        )
//
//        // when
//        with(queries) {
//            // First App: 2 * 2 = 4
//            insertTimeStat(FirstAppId, time = ExactTime, count = 2)
//            insertTimeStat(FirstAppId, time = RangeStartTime, count = 2)
//            // Second App: 5 * 1 = 5
//            insertTimeStat(SecondAppId, time = ExactTime, count = 1)
//            insertTimeStat(SecondAppId, time = RangeStartTime, count = 1)
//            insertTimeStat(SecondAppId, time = RangeEndTime, count = 1)
//            insertTimeStat(SecondAppId, time = RangeMidFirstTime, count = 1)
//            insertTimeStat(SecondAppId, time = RangeMidSecondTime, count = 1)
//            // Third App: 3 * 1 = 3
//            insertTimeStat(ThirdAppId, time = ExactTime, count = 3)
//
//            val result = findMostUsedAppsIdsTest(
//                startLatitude = RangeStartLatitude,
//                endLatitude = RangeEndLatitude,
//                startLongitude = RangeStartLongitude,
//                endLongitude = RangeEndLongitude,
//                startTime = RangeStartTime,
//                endTime = RangeEndTime
//            ).executeAsList()
//
//            // then
//            assertEquals(expected, result)
//        }
//    }

//    @Test
//    fun `correctly sorts by more location and time entries in the range`() = runTest {
//        // given
//        val expected = listOf(
//            FindMostUsedAppsIdsTest(FirstAppId, FirstAppId, countByLocation = 5.0, countByTime = 4.0),
//            FindMostUsedAppsIdsTest(ThirdAppId, ThirdAppId, countByLocation = 5.0, countByTime = 3.0),
//            FindMostUsedAppsIdsTest(SecondAppId, SecondAppId, countByLocation = 2.0, countByTime = 5.0),
//        )
//
//        // when
//        with(queries) {
//            // First App: 3 + 2 = 5
//            insertLocationStat(FirstAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 3)
//            insertLocationStat(FirstAppId, latitude = RangeStartLatitude, longitude = ExactLongitude, count = 2)
//            // Second App: 2 * 1 = 2
//            insertLocationStat(SecondAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 2)
//            // Third App: 5 * 1 = 5
//            insertLocationStat(ThirdAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 1)
//            insertLocationStat(ThirdAppId, latitude = RangeStartLatitude, longitude = ExactLongitude, count = 1)
//            insertLocationStat(ThirdAppId, latitude = RangeEndLatitude, longitude = ExactLongitude, count = 1)
//            insertLocationStat(ThirdAppId, latitude = ExactLatitude, longitude = RangeStartLongitude, count = 1)
//            insertLocationStat(ThirdAppId, latitude = ExactLatitude, longitude = RangeEndLongitude, count = 1)
//
//            // First App: 2 * 2 = 4
//            insertTimeStat(FirstAppId, time = ExactTime, count = 2)
//            insertTimeStat(FirstAppId, time = RangeStartTime, count = 2)
//            // Second App: 5 * 1 = 5
//            insertTimeStat(SecondAppId, time = ExactTime, count = 1)
//            insertTimeStat(SecondAppId, time = RangeStartTime, count = 1)
//            insertTimeStat(SecondAppId, time = RangeEndTime, count = 1)
//            insertTimeStat(SecondAppId, time = RangeMidFirstTime, count = 1)
//            insertTimeStat(SecondAppId, time = RangeMidSecondTime, count = 1)
//            // Third App: 3 * 1 = 3
//            insertTimeStat(ThirdAppId, time = ExactTime, count = 3)
//
//            val result = findMostUsedAppsIdsTest(
//                startLatitude = RangeStartLatitude,
//                endLatitude = RangeEndLatitude,
//                startLongitude = RangeStartLongitude,
//                endLongitude = RangeEndLongitude,
//                startTime = RangeStartTime,
//                endTime = RangeEndTime
//            ).executeAsList()
//
//            // then
//            assertEquals(expected, result)
//        }
//    }

}
