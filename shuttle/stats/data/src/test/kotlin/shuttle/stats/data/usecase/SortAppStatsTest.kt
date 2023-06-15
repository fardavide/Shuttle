package shuttle.stats.data.usecase

import arrow.core.left
import arrow.core.right
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import shuttle.apps.domain.testdata.AppIdTestData
import shuttle.coordinates.domain.error.LocationNotAvailable
import shuttle.database.model.DatabaseAppId
import shuttle.database.model.DatabaseDate
import shuttle.database.model.DatabaseGeoHash
import shuttle.database.model.DatabaseStat
import shuttle.database.model.DatabaseTime
import shuttle.database.testdata.DatabaseAppIdSample
import shuttle.database.testdata.DatabaseDateSample
import shuttle.database.testdata.DatabaseGeoHashSample
import shuttle.database.testdata.DatabaseTimeSample

class SortAppStatsTest : AnnotationSpec() {

    private val scheduler = TestCoroutineScheduler()
    private val dispatcher = StandardTestDispatcher(scheduler)
    private val sortAppStats = SortAppStats(
        computationDispatcher = dispatcher
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
                appId = DatabaseAppIdSample.Stocard,
                geoHash = DatabaseGeoHashSample.Bennet
            ),
            buildStat(
                appId = DatabaseAppIdSample.Telegram,
                geoHash = DatabaseGeoHashSample.Home
            ),
            buildStat(
                appId = DatabaseAppIdSample.Telegram,
                geoHash = DatabaseGeoHashSample.Home
            )
        )

        // when
        val result = sortAppStats(
            stats = stats,
            location = DatabaseGeoHashSample.Bennet.right(),
            date = DatabaseDateSample.Today,
            startTime = DatabaseTimeSample.Midnight,
            endTime = DatabaseTimeSample.Midnight,
            takeAtLeast = Int.MAX_VALUE
        )

        // then
        result shouldBe expected
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
                appId = DatabaseAppIdSample.Deliveroo,
                time = DatabaseTimeSample.Noon
            ),
            buildStat(
                appId = DatabaseAppIdSample.Telegram,
                time = DatabaseTimeSample.Morning
            ),
            buildStat(
                appId = DatabaseAppIdSample.Telegram,
                time = DatabaseTimeSample.Evening
            )
        )

        // when
        val result = sortAppStats(
            stats = stats,
            location = LocationNotAvailable.left(),
            date = DatabaseDateSample.Today,
            startTime = DatabaseTimeSample.Noon,
            endTime = DatabaseTimeSample.Noon,
            takeAtLeast = Int.MAX_VALUE
        )

        // then
        result shouldBe expected
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
            buildStat(DatabaseAppIdSample.CineScout, date = DatabaseDateSample.Today),
            buildStat(DatabaseAppIdSample.Gmail, date = DatabaseDateSample.ThreeDaysAgo),
            buildStat(DatabaseAppIdSample.Telegram, date = DatabaseDateSample.Yesterday)
        )

        // when
        val result = sortAppStats(
            stats = stats,
            location = LocationNotAvailable.left(),
            date = DatabaseDateSample.Today,
            startTime = DatabaseTimeSample.Midnight,
            endTime = DatabaseTimeSample.Midnight,
            takeAtLeast = Int.MAX_VALUE
        )

        // then
        result shouldBe expected
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
            buildStat(DatabaseAppIdSample.Chrome),
            buildStat(DatabaseAppIdSample.Chrome),
            buildStat(DatabaseAppIdSample.GitHub),
            buildStat(DatabaseAppIdSample.Telegram),
            buildStat(DatabaseAppIdSample.Telegram),
            buildStat(DatabaseAppIdSample.Telegram)
        )

        // when
        val result = sortAppStats(
            stats = stats,
            location = LocationNotAvailable.left(),
            date = DatabaseDateSample.Today,
            startTime = DatabaseTimeSample.Midnight,
            endTime = DatabaseTimeSample.Midnight,
            takeAtLeast = Int.MAX_VALUE
        )

        // then
        result shouldBe expected
    }

    companion object TestData {

        private fun buildStat(
            appId: DatabaseAppId,
            date: DatabaseDate = DatabaseDateSample.Yesterday,
            geoHash: DatabaseGeoHash? = DatabaseGeoHashSample.Unknown,
            time: DatabaseTime = DatabaseTimeSample.Midnight
        ) = DatabaseStat(
            appId = appId,
            date = date,
            geoHash = geoHash,
            time = time
        )
    }
}
