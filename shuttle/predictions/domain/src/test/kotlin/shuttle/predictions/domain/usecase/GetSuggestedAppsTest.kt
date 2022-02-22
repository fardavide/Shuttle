package shuttle.predictions.domain.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import shuttle.apps.domain.error.GenericError
import shuttle.apps.domain.usecase.GetAllInstalledApps
import shuttle.predictions.domain.testdata.TestData.AllApps
import shuttle.predictions.domain.testdata.TestData.TestConstraints
import shuttle.stats.domain.usecase.GetAllAppsStats
import kotlin.test.Test
import kotlin.test.assertEquals

class GetSuggestedAppsTest {

    private val getAllInstalledApps: GetAllInstalledApps = mockk {
        coEvery { this@mockk() } returns AllApps.right()
    }
    private val getAllAppsStats: GetAllAppsStats = mockk {
        coEvery { this@mockk() } returns Either.Right(emptyList())
    }
    private val sortAppsByStats: SortAppsByStats = mockk {
        coEvery { this@mockk(any(), any(), any()) } answers {
            firstArg()
        }
    }
    private val getSuggestedApp = GetSuggestedApps(getAllInstalledApps, getAllAppsStats, sortAppsByStats)

    @Test
    fun `if no stats, return all the installed app with original soring`() = runTest {

        // given
        val allApps = AllApps.right()
        coEvery { getAllInstalledApps() } returns allApps

        // when
        val result = getSuggestedApp(TestConstraints)

        // then
        assertEquals(allApps, result)
    }

    @Test
    fun `correctly forwards an error`() = runTest {

        // given
        val error = GenericError().left()
        coEvery { getAllInstalledApps() } returns error

        // when
        val result = getSuggestedApp(TestConstraints)

        // then
        assertEquals(error, result)
    }

    @Test
    fun `correctly sort the apps by current Constraints`() = runTest {

        // when
        getSuggestedApp(TestConstraints)

        // then
        coVerify { sortAppsByStats(AllApps, any(), TestConstraints) }
    }
}
