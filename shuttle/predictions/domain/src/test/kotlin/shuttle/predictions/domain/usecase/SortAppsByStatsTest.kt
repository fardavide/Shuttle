package shuttle.predictions.domain.usecase

import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import shuttle.predictions.domain.testdata.TestData.AllApps
import shuttle.predictions.domain.testdata.TestData.AnotherLocation
import shuttle.predictions.domain.testdata.TestData.AnotherTime
import shuttle.predictions.domain.testdata.TestData.CurrentLocation
import shuttle.predictions.domain.testdata.TestData.CurrentTime
import shuttle.predictions.domain.testdata.TestData.ProtonMail
import shuttle.predictions.domain.testdata.TestData.Shuttle
import shuttle.predictions.domain.testdata.TestData.Telegram
import shuttle.predictions.domain.testdata.TestData.TestConstraints
import shuttle.predictions.domain.testdata.TestData.buildAppStats
import shuttle.stats.domain.model.withCount
import kotlin.test.Test
import kotlin.test.assertEquals

class SortAppsByStatsTest {

    private val dispatcher = StandardTestDispatcher()
    private val sortAppsByStats = SortAppsByStats(dispatcher)

    @Test
    fun `correctly sort the apps by current Location`() = runTest(dispatcher) {

        // given
        val stats = listOf(
            buildAppStats(
                ProtonMail,
                locationCounters = listOf(
                    CurrentLocation withCount 2,
                    AnotherLocation withCount 6
                )
            ),
            buildAppStats(
                Shuttle,
                locationCounters = listOf(
                    CurrentLocation withCount 4,
                    AnotherLocation withCount 1
                )
            ),
            buildAppStats(
                Telegram,
                locationCounters = listOf(
                    CurrentLocation withCount 1,
                    AnotherLocation withCount 3
                )
            ),
        )
        val expected = listOf(
            Shuttle,
            ProtonMail,
            Telegram
        )

        // when
        val result = sortAppsByStats(AllApps, stats, TestConstraints)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `correctly sort the apps by current Time`() = runTest(dispatcher) {

        // given
        val stats = listOf(
            buildAppStats(
                ProtonMail,
                timeCounters = listOf(
                    CurrentTime withCount 2,
                    AnotherTime withCount 6
                )
            ),
            buildAppStats(
                Telegram,
                timeCounters = listOf(
                    CurrentTime withCount 1,
                    AnotherTime withCount 3
                )
            ),
            buildAppStats(
                Shuttle,
                timeCounters = listOf(
                    CurrentTime withCount 4,
                    AnotherTime withCount 1
                )
            ),
        )
        val expected = listOf(
            Shuttle,
            ProtonMail,
            Telegram
        )

        // when
        val result = sortAppsByStats(AllApps, stats, TestConstraints)

        // then
        assertEquals(expected, result)
    }
}
