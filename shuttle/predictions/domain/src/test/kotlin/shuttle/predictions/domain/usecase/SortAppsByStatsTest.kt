package shuttle.predictions.domain.usecase

import com.soywiz.klock.Time
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import shuttle.apps.domain.model.AppId
import shuttle.apps.domain.model.AppModel
import shuttle.apps.domain.model.AppName
import shuttle.predictions.domain.model.Constraints
import shuttle.stats.domain.model.AppStats
import shuttle.stats.domain.model.Location
import shuttle.stats.domain.model.LocationCounter
import shuttle.stats.domain.model.TimeCounter
import shuttle.stats.domain.model.withCount
import kotlin.test.*

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

    companion object TestData {

        private val CurrentLocation = Location(latitude = 10.0, longitude = 20.0)
        private val AnotherLocation = Location(latitude = 21.0, longitude = 31.0)

        private val CurrentTime = Time(hour = 14)
        private val AnotherTime = Time(hour = 6)

        private val TestConstraints = Constraints(
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
