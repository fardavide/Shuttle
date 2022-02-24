package shuttle.database

import kotlinx.coroutines.test.runTest
import shuttle.database.testdata.TestData.AllAppsIds
import shuttle.database.testdata.TestData.ExactLatitude
import shuttle.database.testdata.TestData.ExactLongitude
import shuttle.database.testdata.TestData.ExactTime
import shuttle.database.testdata.TestData.FifthAppId
import shuttle.database.testdata.TestData.FirstAppId
import shuttle.database.testdata.TestData.FourthAppId
import shuttle.database.testdata.TestData.RangeEndLatitude
import shuttle.database.testdata.TestData.RangeEndLongitude
import shuttle.database.testdata.TestData.RangeEndTime
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

    @Test
    fun `sort by location first, then by time`() = runTest {
        // given
        insertApps(5)
        val expected = listOf(FifthAppId, FourthAppId, ThirdAppId, SecondAppId, FirstAppId)

        // when
        with(queries) {
            insertTimeStat(FirstAppId, time = ExactTime, count = 5)
            insertTimeStat(SecondAppId, time = ExactTime, count = 6)
            insertTimeStat(ThirdAppId, time = ExactTime, count = 2)
            insertLocationStat(ThirdAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 2)
            insertTimeStat(FourthAppId, time = ExactTime, count = 4)
            insertLocationStat(FourthAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 2)
            insertTimeStat(FifthAppId, time = ExactTime, count = 10)
            insertLocationStat(FifthAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 3)

            val result = findMostUsedAppsIds(
                startLatitude = ExactLatitude,
                endLatitude = ExactLatitude,
                startLongitude = ExactLongitude,
                endLongitude = ExactLongitude,
                startTime = ExactTime,
                endTime = ExactTime
            ).executeAsList()

            // then
            assertEquals(expected, result)
        }
    }

    @Test
    fun `sort by location only`() = runTest {
        // given
        insertApps(5)
        val expected = listOf(FifthAppId, FourthAppId, SecondAppId, FirstAppId, ThirdAppId)

        // when
        with(queries) {
            insertLocationStat(FirstAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 2)
            insertLocationStat(SecondAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 3)
            insertLocationStat(ThirdAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 1)
            insertLocationStat(FourthAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 4)
            insertLocationStat(FifthAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 5)

            val result = findMostUsedAppsIds(
                startLatitude = ExactLatitude,
                endLatitude = ExactLatitude,
                startLongitude = ExactLongitude,
                endLongitude = ExactLongitude,
                startTime = ExactTime,
                endTime = ExactTime
            ).executeAsList()

            // then
            assertEquals(expected, result)
        }
    }

    @Test
    fun `sort by time only`() = runTest {
        // given
        insertApps(5)
        val expected = listOf(FifthAppId, FourthAppId, SecondAppId, FirstAppId, ThirdAppId)

        // when
        with(queries) {
            insertTimeStat(FirstAppId, time = ExactTime, count = 2)
            insertTimeStat(SecondAppId, time = ExactTime, count = 3)
            insertTimeStat(ThirdAppId, time = ExactTime, count = 1)
            insertTimeStat(FourthAppId, time = ExactTime, count = 4)
            insertTimeStat(FifthAppId, time = ExactTime, count = 5)

            val result = findMostUsedAppsIds(
                startLatitude = ExactLatitude,
                endLatitude = ExactLatitude,
                startLongitude = ExactLongitude,
                endLongitude = ExactLongitude,
                startTime = ExactTime,
                endTime = ExactTime
            ).executeAsList()

            // then
            assertEquals(expected, result)
        }
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
                startLatitude = ExactLatitude,
                endLatitude = ExactLatitude,
                startLongitude = ExactLongitude,
                endLongitude = ExactLongitude,
                startTime = ExactTime,
                endTime = ExactTime
            ).executeAsList()

            // then
            assertEquals(expected, result)
        }
    }

    @Test
    fun `one or two of time and location, with exact values, for every app`() = runTest {
        // given
        insertApps(3)
        val expected = listOf(SecondAppId, ThirdAppId, FirstAppId)

        // when
        with(queries) {
            insertTimeStat(FirstAppId, time = ExactTime, count = 5)
            insertLocationStat(SecondAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 8)
            insertTimeStat(ThirdAppId, time = ExactTime, count = 2)
            insertLocationStat(ThirdAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 1)

            val result = findMostUsedAppsIds(
                startLatitude = ExactLatitude,
                endLatitude = ExactLatitude,
                startLongitude = ExactLongitude,
                endLongitude = ExactLongitude,
                startTime = ExactTime,
                endTime = ExactTime
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
                startLatitude = ExactLatitude,
                endLatitude = ExactLatitude,
                startLongitude = ExactLongitude,
                endLongitude = ExactLongitude,
                startTime = ExactTime,
                endTime = ExactTime
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

    @Test
    fun `one or two of time and location, with range values, for every app`() = runTest {
        // given
        insertApps(3)
        val expected = listOf(SecondAppId, ThirdAppId, FirstAppId)

        // when
        with(queries) {
            insertTimeStat(FirstAppId, time = ExactTime, count = 5)
            insertLocationStat(SecondAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 8)
            insertTimeStat(ThirdAppId, time = ExactTime, count = 2)
            insertLocationStat(ThirdAppId, latitude = ExactLatitude, longitude = ExactLongitude, count = 1)

            val result = findMostUsedAppsIds(
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

    private suspend fun insertApps(count: Int? = null) {
        repeat(count ?: AllAppsIds.size) { index ->
            queries.insertApp(AllAppsIds[index])
        }
    }
}
