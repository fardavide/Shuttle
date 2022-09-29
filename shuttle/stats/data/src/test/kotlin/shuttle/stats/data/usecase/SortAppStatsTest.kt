package shuttle.stats.data.usecase

import arrow.core.left
import arrow.core.right
import com.soywiz.klock.DateTime
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import shuttle.apps.domain.testdata.AppIdTestData
import shuttle.coordinates.domain.error.LocationNotAvailable
import shuttle.coordinates.domain.model.CoordinatesResult
import shuttle.coordinates.domain.usecase.ObserveCurrentCoordinates
import shuttle.database.model.DatabaseStat
import shuttle.database.model.DatabaseAppId
import shuttle.database.model.DatabaseDate
import shuttle.database.model.DatabaseGeoHash
import shuttle.database.model.DatabaseTime
import shuttle.database.testdata.DatabaseAppIdTestData
import shuttle.database.testdata.DatabaseDateTestData
import shuttle.database.testdata.DatabaseGeoHashTestData
import shuttle.database.testdata.DatabaseTimeTestData
import shuttle.stats.data.mapper.DatabaseDateAndTimeMapper
import shuttle.stats.data.model.DatabaseDateAndTime
import kotlin.test.Test
import kotlin.test.assertEquals

class SortAppStatsTest {

    private val scheduler = TestCoroutineScheduler()
    private val dispatcher = StandardTestDispatcher(scheduler)
    private val databaseDateAndTimeMapper: DatabaseDateAndTimeMapper = mockk {
        every { toDatabaseDateAndTime(dateTime = any()) } returns DatabaseDateAndTime(
            date = DatabaseDateTestData.Today,
            time = DatabaseTimeTestData.Midnight
        )
    }
    private val observeCurrentCoordinates: ObserveCurrentCoordinates = mockk {
        every { this@mockk() } returns flowOf(
            CoordinatesResult(
                location = LocationNotAvailable.left(),
                dateTime = DateTime.EPOCH
            )
        )
    }

    private val sortAppStats = SortAppStats(
        computationDispatcher = dispatcher,
        databaseDateAndTimeMapper = databaseDateAndTimeMapper,
        observeCurrentCoordinates = observeCurrentCoordinates
    )

    @Test
    fun `prioritizes stats from the same location`() = runTest(dispatcher) {
        // given
        val expected = listOf(
            AppIdTestData.Stocard,
            AppIdTestData.Telegram
        )
        val stats = listOf(
            buildStat(
                appId = DatabaseAppIdTestData.Stocard,
                geoHash = DatabaseGeoHashTestData.Bennet
            ),
            buildStat(
                appId = DatabaseAppIdTestData.Telegram,
                geoHash = DatabaseGeoHashTestData.Home
            ),
            buildStat(
                appId = DatabaseAppIdTestData.Telegram,
                geoHash = DatabaseGeoHashTestData.Home
            )
        )

        // when
        val result = sortAppStats(
            stats = stats,
            location = DatabaseGeoHashTestData.Bennet.right(),
            date = DatabaseDateTestData.Today,
            startTime = DatabaseTimeTestData.Midnight,
            endTime = DatabaseTimeTestData.Midnight
        )

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `prioritizes stats from the same time`() = runTest(dispatcher) {
        // given
        val expected = listOf(
            AppIdTestData.Deliveroo,
            AppIdTestData.Telegram
        )
        val stats = listOf(
            buildStat(
                appId = DatabaseAppIdTestData.Deliveroo,
                time = DatabaseTimeTestData.Noon
            ),
            buildStat(
                appId = DatabaseAppIdTestData.Telegram,
                time = DatabaseTimeTestData.Morning
            ),
            buildStat(
                appId = DatabaseAppIdTestData.Telegram,
                time = DatabaseTimeTestData.Evening
            )
        )

        // when
        val result = sortAppStats(
            stats = stats,
            location = LocationNotAvailable.left(),
            date = DatabaseDateTestData.Today,
            startTime = DatabaseTimeTestData.Noon,
            endTime = DatabaseTimeTestData.Noon
        )

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `prioritizes recent stats`() = runTest(dispatcher) {
        // given
        val expected = listOf(
            AppIdTestData.CineScout,
            AppIdTestData.Telegram,
            AppIdTestData.Gmail
        )
        val stats = listOf(
            buildStat(DatabaseAppIdTestData.CineScout, date = DatabaseDateTestData.Today),
            buildStat(DatabaseAppIdTestData.Gmail, date = DatabaseDateTestData.ThreeDaysAgo),
            buildStat(DatabaseAppIdTestData.Telegram, date = DatabaseDateTestData.Yesterday)
        )

        // when
        val result = sortAppStats(
            stats = stats,
            location = LocationNotAvailable.left(),
            date = DatabaseDateTestData.Today,
            startTime = DatabaseTimeTestData.Midnight,
            endTime = DatabaseTimeTestData.Midnight
        )

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `sorts by stats count`() = runTest(dispatcher) {
        // given
        val expected = listOf(
            AppIdTestData.Telegram,
            AppIdTestData.Chrome,
            AppIdTestData.GitHub
        )
        val stats = listOf(
            buildStat(DatabaseAppIdTestData.Chrome),
            buildStat(DatabaseAppIdTestData.Chrome),
            buildStat(DatabaseAppIdTestData.GitHub),
            buildStat(DatabaseAppIdTestData.Telegram),
            buildStat(DatabaseAppIdTestData.Telegram),
            buildStat(DatabaseAppIdTestData.Telegram)
        )

        // when
        val result = sortAppStats(
            stats = stats,
            location = LocationNotAvailable.left(),
            date = DatabaseDateTestData.Today,
            startTime = DatabaseTimeTestData.Midnight,
            endTime = DatabaseTimeTestData.Midnight
        )

        // then
        assertEquals(expected, result)
    }

    companion object TestData {

        private fun buildStat(
            appId: DatabaseAppId,
            date: DatabaseDate = DatabaseDateTestData.Yesterday,
            geoHash: DatabaseGeoHash? = DatabaseGeoHashTestData.Unknown,
            time: DatabaseTime = DatabaseTimeTestData.Midnight
        ) = DatabaseStat(
            appId = appId,
            date = date,
            geoHash = geoHash,
            time = time
        )
    }
}
