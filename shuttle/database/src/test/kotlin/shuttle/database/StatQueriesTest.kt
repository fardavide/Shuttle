package shuttle.database

import kotlinx.coroutines.test.runTest
import shuttle.database.testutil.AllAppsIds
import shuttle.database.testutil.DatabaseTest
import shuttle.database.testutil.ExactLatitude
import shuttle.database.testutil.ExactLongitude
import shuttle.database.testutil.ExactTime
import shuttle.database.testutil.FifthAppId
import shuttle.database.testutil.FirstAppId
import shuttle.database.testutil.FourthAppId
import shuttle.database.testutil.RangeEndLatitude
import shuttle.database.testutil.RangeEndLongitude
import shuttle.database.testutil.RangeEndTime
import shuttle.database.testutil.RangeStartLatitude
import shuttle.database.testutil.RangeStartLongitude
import shuttle.database.testutil.RangeStartTime
import shuttle.database.testutil.SecondAppId
import shuttle.database.testutil.ThirdAppId
import kotlin.test.Test
import kotlin.test.assertEquals

class StatQueriesTest : DatabaseTest() {

    private val queries = database.statQueries

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
