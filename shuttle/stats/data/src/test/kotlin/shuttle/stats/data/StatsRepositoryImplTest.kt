package shuttle.stats.data

import com.soywiz.klock.Time
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import shuttle.apps.domain.AppsRepository
import shuttle.apps.domain.model.AppId
import shuttle.apps.domain.model.AppModel
import shuttle.apps.domain.model.AppName
import shuttle.apps.domain.model.SuggestedAppModel
import shuttle.coordinates.domain.model.GeoHash
import shuttle.database.datasource.StatDataSource
import shuttle.database.model.DatabaseAppId
import shuttle.database.model.DatabaseAppStat
import shuttle.database.model.DatabaseGeoHash
import shuttle.database.model.DatabaseTime
import shuttle.database.testdata.TestData.AllAppsIds
import shuttle.database.testdata.TestData.FifthAppId
import shuttle.database.testdata.TestData.FirstAppId
import shuttle.database.testdata.TestData.FourthAppId
import shuttle.database.testdata.TestData.SecondAppId
import shuttle.database.testdata.TestData.ThirdAppId
import kotlin.test.Test
import kotlin.test.assertEquals

class StatsRepositoryImplTest {

    private val appsRepository: AppsRepository = mockk {
        coEvery { observeNotBlacklistedApps() } returns flowOf(AllAppsIds.map(::buildAppModel))
    }
    private val statDataSource: StatDataSource = mockk()
    private val sortAppStatsByCounts: SortAppStatsByCounts = mockk {
        coEvery { this@mockk(any()) } answers {
            val stats = firstArg<List<DatabaseAppStat>>()
            stats.map { AppId(it.appId.value) }
        }
    }
    private val repository = StatsRepositoryImpl(
        appsRepository = appsRepository,
        statDataSource = statDataSource,
        sortAppStatsByCounts = sortAppStatsByCounts
    )

    @Test
    fun `returns all the suggested apps plus all the installed apps`() = runTest {
        // given
        everyStatDataSourceFindAllStats() returns flowOf(listOf(FourthAppId, FifthAppId).map(::buildLocationAppStats))
        val expected = listOf(
            FirstAppId.suggested(isSuggested = false),
            SecondAppId.suggested(isSuggested = false),
            ThirdAppId.suggested(isSuggested = false),
            FourthAppId.suggested(isSuggested = true),
            FifthAppId.suggested(isSuggested = true)
        )

        // when
        val result = repository.observeSuggestedApps(GeoHash, StartTime, EndTime)
            .first()
            .sortedBy { it.id.value }

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `returns all the installed apps if no suggested`() = runTest {
        // given
        everyStatDataSourceFindAllStats() returns flowOf(emptyList())
        val expected = listOf(
            FirstAppId.suggested(isSuggested = false),
            SecondAppId.suggested(isSuggested = false),
            ThirdAppId.suggested(isSuggested = false),
            FourthAppId.suggested(isSuggested = false),
            FifthAppId.suggested(isSuggested = false)
        )

        // when
        val result = repository.observeSuggestedApps(GeoHash, StartTime, EndTime)
            .first()
            .sortedBy { it.id.value }

        // then
        assertEquals(expected, result)
    }

    private fun everyStatDataSourceFindAllStats() = every {
        statDataSource.findAllStats(
            DatabaseGeoHash(any()),
            DatabaseTime(any()),
            DatabaseTime(any())
        )
    }

    companion object TestData {

        val GeoHash = GeoHash("1234")
        val StartTime = Time(hour = 4)
        val EndTime = Time(hour = 5)

        fun buildAppModel(databaseAppId: DatabaseAppId) = AppModel(
            id = AppId(databaseAppId.value),
            name = AppName(databaseAppId.value)
        )

        fun DatabaseAppId.suggested(isSuggested: Boolean): SuggestedAppModel =
            buildSuggestedAppModel(databaseAppId = this, isSuggested = isSuggested)

        fun buildSuggestedAppModel(databaseAppId: DatabaseAppId, isSuggested: Boolean) = SuggestedAppModel(
            id = AppId(databaseAppId.value),
            name = AppName(databaseAppId.value),
            isSuggested = isSuggested
        )

        fun buildLocationAppStats(databaseAppId: DatabaseAppId) = DatabaseAppStat.ByLocation(
            appId = databaseAppId,
            count = 0
        )
    }
}

