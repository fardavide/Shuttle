package shuttle.stats.data

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import shuttle.apps.domain.model.AppId
import shuttle.database.model.DatabaseAppId
import shuttle.database.model.DatabaseAppStat
import shuttle.database.testdata.TestData.FifthAppId
import shuttle.database.testdata.TestData.FirstAppId
import shuttle.database.testdata.TestData.FourthAppId
import shuttle.database.testdata.TestData.SecondAppId
import shuttle.database.testdata.TestData.ThirdAppId
import shuttle.settings.domain.usecase.GetUseCurrentLocationOnly
import kotlin.test.Test
import kotlin.test.assertEquals

class SortAppStatsByCountsTest {

    private val dispatcher = StandardTestDispatcher()
    private val getUseCurrentLocationOnly: GetUseCurrentLocationOnly = mockk {
        coEvery { this@mockk() } returns false
    }
    private val sort = SortAppStatsByCounts(dispatcher, getUseCurrentLocationOnly = getUseCurrentLocationOnly)

    @Test
    fun `empty list returns an empty list`() = runTest(dispatcher) {
        // given
        val input = listOf<DatabaseAppStat>()
        val expected = listOf<AppId>()

        // when
        val result = sort(input)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `by location has high priority when few in a big list`() = runTest(dispatcher) {
        // given
        val input = listOf(
            DatabaseAppStat.ByTime(SecondAppId, 100),
            DatabaseAppStat.ByTime(ThirdAppId, 10),
            DatabaseAppStat.ByTime(FourthAppId, 1),
            DatabaseAppStat.ByTime(FifthAppId, 1_000),
            DatabaseAppStat.ByLocation(FirstAppId, 1)
        )
        val expected = listOf(
            FifthAppId,
            FirstAppId,
            SecondAppId,
            ThirdAppId,
            FourthAppId
        ).toAppsIds()

        // when
        val result = sort(input)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `by location has mid priority when some in a big list`() = runTest(dispatcher) {
        // given
        val input = listOf(
            DatabaseAppStat.ByTime(SecondAppId, 100),
            DatabaseAppStat.ByTime(ThirdAppId, 10),
            DatabaseAppStat.ByTime(FourthAppId, 1),
            DatabaseAppStat.ByLocation(FirstAppId, 1),
            DatabaseAppStat.ByLocation(FifthAppId, 2)
        )
        val expected = listOf(
            SecondAppId,
            FifthAppId,
            ThirdAppId,
            FirstAppId,
            FourthAppId
        ).toAppsIds()

        // when
        val result = sort(input)

        // then
        assertEquals(expected, result)
    }



    @Test
    fun `by location is ignored when they're more than by time`() = runTest(dispatcher) {
        // given
        val input = listOf(
            DatabaseAppStat.ByTime(SecondAppId, 100),
            DatabaseAppStat.ByTime(ThirdAppId, 10),
            DatabaseAppStat.ByLocation(FourthAppId, 1),
            DatabaseAppStat.ByLocation(FirstAppId, 1),
            DatabaseAppStat.ByLocation(FifthAppId, 2)
        )
        val expected = listOf(
            SecondAppId,
            ThirdAppId,
        ).toAppsIds()

        // when
        val result = sort(input)

        // then
        assertEquals(expected, result)
    }

    companion object TestData {

        fun List<DatabaseAppId>.toAppsIds() = map { AppId(it.value) }
    }
}
