package shuttle.stats.data

import arrow.core.right
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
import shuttle.coordinates.domain.model.Location
import shuttle.database.datasource.StatDataSource
import shuttle.database.model.DatabaseAppId
import shuttle.database.model.DatabaseLatitude
import shuttle.database.model.DatabaseLongitude
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
        coEvery { getAllInstalledApps() } returns AllAppsIds.map(::buildAppModel).right()
    }
    private val statDataSource: StatDataSource = mockk()
    private val repository = StatsRepositoryImpl(
        appsRepository = appsRepository,
        statDataSource = statDataSource
    )

    @Test
    fun `returns all the suggested apps plus all the installed apps`() = runTest {
        // given
        everyStatDataSourceObserveMostUsedAppsIds() returns flowOf(listOf(FourthAppId, FifthAppId))
        val expected = listOf(FourthAppId, FifthAppId, FirstAppId, SecondAppId, ThirdAppId)
            .map(::buildAppModel)
            .right()

        // when
        val result = repository.observeSuggestedApps(StartLocation, EndLocation, StartTime, EndTime)
            .first()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `returns all the installed apps if no suggested`() = runTest {
        // given
        everyStatDataSourceObserveMostUsedAppsIds() returns flowOf(emptyList())
        val expected = listOf(FirstAppId, SecondAppId, ThirdAppId, FourthAppId, FifthAppId)
            .map(::buildAppModel)
            .right()

        // when
        val result = repository.observeSuggestedApps(StartLocation, EndLocation, StartTime, EndTime)
            .first()

        // then
        assertEquals(expected, result)
    }

    private fun everyStatDataSourceObserveMostUsedAppsIds() = every {
        statDataSource.observeMostUsedAppsIds(
            DatabaseLatitude(any()),
            DatabaseLatitude(any()),
            DatabaseLongitude(any()),
            DatabaseLongitude(any()),
            DatabaseTime(any()),
            DatabaseTime(any())
        )
    }

    companion object TestData {

        val StartLocation = Location(10.0, 15.0)
        val EndLocation = Location(20.0, 25.0)
        val StartTime = Time(hour = 4)
        val EndTime = Time(hour = 5)

        fun buildAppModel(databaseAppId: DatabaseAppId) = AppModel(
            id = AppId(databaseAppId.value),
            name = AppName(databaseAppId.value)
        )
    }
}

