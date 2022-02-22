package shuttle.predictions.domain.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.soywiz.klock.Time
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import kotlin.test.Test
import shuttle.apps.domain.error.GenericError
import shuttle.apps.domain.model.AppId
import shuttle.apps.domain.model.AppModel
import shuttle.apps.domain.model.AppName
import shuttle.apps.domain.usecase.GetAllInstalledApps
import shuttle.predictions.domain.model.Constraints
import shuttle.stats.domain.model.AppStats
import shuttle.stats.domain.model.Location
import shuttle.stats.domain.model.LocationCounter
import shuttle.stats.domain.model.TimeCounter
import shuttle.stats.domain.model.withCount
import shuttle.stats.domain.usecase.GetAllAppsStats

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
        val result = getSuggestedApp(TestCostraints)

        // then
        assertEquals(allApps, result)
    }

    @Test
    fun `correctly forwards an error`() = runTest {

        // given
        val error = GenericError().left()
        coEvery { getAllInstalledApps() } returns error

        // when
        val result = getSuggestedApp(TestCostraints)

        // then
        assertEquals(error, result)
    }

    @Test
    fun `correctly sort the apps by current Constraints`() = runTest {

        // when
        getSuggestedApp(TestCostraints)

        // then
        coVerify { sortAppsByStats(AllApps, any(), TestCostraints) }
    }

    companion object TestData {

        private val CurrentLocation = Location(latitude = 10.0, longitude = 20.0)
        private val AnotherLocation = Location(latitude = 21.0, longitude = 31.0)

        private val CurrentTime = Time(hour = 14)
        private val AnotherTime = Time(hour = 6)

        private val TestCostraints = Constraints(
            location = CurrentLocation,
            time = CurrentTime
        )

        val ProtonMail = AppModel(AppId("proton.protonmail"), AppName("ProtonMail"))
        val Shuttle = AppModel(AppId("forface.shuttle"), AppName("Shuttle"))
        val Telegram = AppModel(AppId("telegram"), AppName("Telegram"))

        val AllApps = listOf(
            ProtonMail,
            Shuttle,
            Telegram
        )

        fun buildAppStats(
            app: AppModel,
            locationCounters: List<LocationCounter> = emptyList(),
            timeCounters: List<TimeCounter> = emptyList()
        ) = AppStats(
            app.id,
            locationCounters = locationCounters,
            timeCounters = timeCounters
        )
    }
}
