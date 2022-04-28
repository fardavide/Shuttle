package shuttle.stats.data

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import shuttle.apps.domain.model.AppId
import shuttle.database.model.DatabaseAppId
import shuttle.database.model.DatabaseAppStat
import shuttle.stats.data.model.SortingRatios
import shuttle.stats.data.usecase.GetSortingRatios
import shuttle.stats.data.usecase.SortAppStatsByCounts
import kotlin.test.Test
import kotlin.test.assertEquals

class SortAppStatsByCountsTest {

    private val dispatcher = StandardTestDispatcher()
    private val getSortingRatios: GetSortingRatios = mockk {
        coEvery { this@mockk(any()) } returns SortingRatios(1.0, 1.0)
    }
    private val sort = SortAppStatsByCounts(dispatcher, getSortingRatios = getSortingRatios)

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

    companion object TestData {

        fun List<DatabaseAppId>.toAppsIds() = map { AppId(it.value) }
    }
}
