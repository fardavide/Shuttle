package shuttle.stats.data

import arrow.core.some
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import shuttle.apps.domain.AppsRepository
import shuttle.apps.domain.model.AppId
import shuttle.apps.domain.testdata.AppModelTestData
import shuttle.apps.domain.testdata.SuggestedAppModelTestData
import shuttle.apps.domain.testdata.not
import shuttle.coordinates.domain.testdata.DateTestData
import shuttle.coordinates.domain.testdata.GeoHashTestData
import shuttle.coordinates.domain.testdata.TimeTestData
import shuttle.database.datasource.StatDataSource
import shuttle.database.model.DatabaseStat
import shuttle.database.testdata.DatabaseAppIdSample
import shuttle.database.testdata.DatabaseStatTestData
import shuttle.stats.data.mapper.DatabaseDateAndTimeMapper
import shuttle.stats.data.usecase.SortAppStats
import shuttle.stats.data.worker.DeleteOldStatsWorker
import kotlin.test.Test
import kotlin.test.assertEquals

class StatsRepositoryImplTest {

    private val appsRepository: AppsRepository = mockk {
        coEvery { observeNotBlacklistedApps() } returns flowOf(AppModelTestData.all())
    }
    private val deleteOldStatsScheduler: DeleteOldStatsWorker.Scheduler = mockk(relaxUnitFun = true)
    private val statDataSource: StatDataSource = mockk()
    private val sortAppStats: SortAppStats = mockk {
        coEvery {
            this@mockk(
                stats = any(),
                location = any(),
                date = any(),
                startTime = any(),
                endTime = any()
            )
        } answers {
            val stats = firstArg<List<DatabaseStat>>()
            stats.map { AppId(it.appId.value) }
        }
    }
    private val repository = StatsRepositoryImpl(
        appsRepository = appsRepository,
        databaseDateAndTimeMapper = DatabaseDateAndTimeMapper(),
        deleteOldStatsScheduler = deleteOldStatsScheduler,
        statDataSource = statDataSource,
        sortAppStats = sortAppStats
    )

    @Test
    fun `returns all the suggested apps plus all the installed apps`() = runTest {
        // given
        val suggestedApps = listOf(
            DatabaseStatTestData.buildDatabaseStat(DatabaseAppIdSample.Telegram),
            DatabaseStatTestData.buildDatabaseStat(DatabaseAppIdSample.Chrome),
            DatabaseStatTestData.buildDatabaseStat(DatabaseAppIdSample.CineScout)
        )
        every { statDataSource.findAllStats() } returns flowOf(suggestedApps)
        val expected = listOf(
            SuggestedAppModelTestData.Chrome,
            SuggestedAppModelTestData.CineScout,
            SuggestedAppModelTestData.GitHub.not(),
            SuggestedAppModelTestData.Gmail.not(),
            SuggestedAppModelTestData.MovieBase.not(),
            SuggestedAppModelTestData.Shuttle.not(),
            SuggestedAppModelTestData.Slack.not(),
            SuggestedAppModelTestData.Telegram,
            SuggestedAppModelTestData.YouTube.not()
        )

        // when
        val result = repository.observeSuggestedApps(
            location = GeoHashTestData.Home.some(),
            date = DateTestData.Today,
            startTime = TimeTestData.Midnight,
            endTime = TimeTestData.Midnight
        )
            .first()
            .sortedBy { it.id.value }

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `returns all the installed apps if no suggested`() = runTest {
        // given
        every { statDataSource.findAllStats() } returns flowOf(emptyList())
        val expected = SuggestedAppModelTestData.all().map { model -> model.not() }

        // when
        val result = repository.observeSuggestedApps(
            location = GeoHashTestData.Home.some(),
            date = DateTestData.Today,
            startTime = TimeTestData.Midnight,
            endTime = TimeTestData.Midnight
        )
            .first()
            .sortedBy { it.id.value }

        // then
        assertEquals(expected, result)
    }
}

