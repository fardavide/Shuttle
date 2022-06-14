package shuttle.stats.data.usecase

import arrow.core.Option
import arrow.core.none
import com.soywiz.klock.DateTime
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import shuttle.coordinates.domain.usecase.ObserveCurrentDateTime
import shuttle.database.FindAllStatsFromLocationAndTimeTables
import shuttle.database.datasource.StatDataSource
import shuttle.database.model.DatabaseAppId
import shuttle.database.model.DatabaseDate
import shuttle.database.model.DatabaseGeoHash
import shuttle.database.model.DatabaseTime
import shuttle.database.testdata.TestData.ExactTime
import shuttle.database.testdata.TestData.GeoHash
import shuttle.database.testdata.TestData.RangeStartTime
import shuttle.stats.data.mapper.DatabaseDateMapper
import kotlin.test.Test

class MigrateStatsToSingleTableTest {

    private val databaseDateMapper: DatabaseDateMapper = mockk {
        every { toDatabaseDate(DateTime.EPOCH) } returns CurrentDate
    }
    private val observeCurrentDateTime: ObserveCurrentDateTime = mockk {
        every { this@mockk() } returns flowOf(DateTime.EPOCH)
    }
    private val statDataSource: StatDataSource = mockk(relaxUnitFun = true) {
        every { findAllStatsFromLocationAndTimeTables() } returns flowOf(emptyList())
    }

    private val migrateStatsToSingleTable = MigrateStatsToSingleTable(
        databaseDateMapper = databaseDateMapper,
        observeCurrentDateTime = observeCurrentDateTime,
        statDataSource = statDataSource
    )

    @Test
    fun `all stats are migrated`() = runTest {
        // given
        val stats = listOf(
            buildLocationStat(Shuttle, GeoHash),
            buildTimeStat(Shuttle, RangeStartTime),
            buildTimeStat(Shuttle, ExactTime),
            buildTimeStat(Telegram, ExactTime)
        )
        every { statDataSource.findAllStatsFromLocationAndTimeTables() } returns flowOf(stats)

        // when
        migrateStatsToSingleTable()

        // then
        coVerify {
            statDataSource.insertOpenStats(Shuttle, CurrentDate, Option(GeoHash), ZeroTime)
            statDataSource.insertOpenStats(Shuttle, CurrentDate, none(), RangeStartTime)
            statDataSource.insertOpenStats(Shuttle, CurrentDate - 1, none(), ExactTime)
            statDataSource.insertOpenStats(Telegram, CurrentDate, none(), ExactTime)
        }
    }

    @Test
    fun `deletes location and time stat tables`() = runTest {
        // when
        migrateStatsToSingleTable()

        // then
        coVerify { statDataSource.clearAllStatsFromLocationAndTimeTables() }
    }

    companion object TestData {

        private val CurrentDate = DatabaseDate(dayNumber = 100)
        private val ZeroTime = DatabaseTime(minuteOfTheDay = 0)

        private val Shuttle = DatabaseAppId("Shuttle")
        private val Telegram = DatabaseAppId("Telegram")

        private fun buildLocationStat(
            appId: DatabaseAppId,
            geoHash: DatabaseGeoHash
        ) = FindAllStatsFromLocationAndTimeTables(
            appIdByLocation = appId,
            appIdByTime = null,
            location = geoHash,
            time = null
        )

        private fun buildTimeStat(
            appId: DatabaseAppId,
            time: DatabaseTime
        ) = FindAllStatsFromLocationAndTimeTables(
            appIdByLocation = null,
            appIdByTime = appId,
            location = null,
            time = time
        )
    }
}
