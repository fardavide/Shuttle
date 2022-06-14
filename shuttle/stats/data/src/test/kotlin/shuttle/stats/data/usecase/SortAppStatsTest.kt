package shuttle.stats.data.usecase

import com.soywiz.klock.DateTime
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import shuttle.apps.domain.model.AppId
import shuttle.coordinates.domain.usecase.ObserveCurrentDateTime
import shuttle.database.Stat
import shuttle.database.model.DatabaseAppId
import shuttle.database.model.DatabaseDate
import shuttle.database.model.DatabaseTime
import shuttle.stats.data.mapper.DatabaseDateMapper
import kotlin.test.Test
import kotlin.test.assertEquals

class SortAppStatsTest {

    private val scheduler = TestCoroutineScheduler()
    private val dispatcher = StandardTestDispatcher(scheduler)
    private val databaseDateMapper: DatabaseDateMapper = mockk {
        every { toDatabaseDate(dateTime = DateTime.EPOCH) } returns CurrentDatabaseDate
    }
    private val observeCurrentDateTime: ObserveCurrentDateTime = mockk {
        every { this@mockk() } returns flowOf(DateTime.EPOCH)
    }

    private val sortAppStats = SortAppStats(
        computationDispatcher = dispatcher,
        databaseDateMapper = databaseDateMapper,
        observeCurrentDateTime = observeCurrentDateTime
    )

    @Test
    fun `gives more priority to recent stats`() = runTest(dispatcher) {
        // given
        val expected = listOf(
            Telegram,
            Mail,
            Shuttle
        ).map(::AppId)
        val telegram = buildStat(Telegram, dayNumber = 99)
        val mail = buildStat(Mail, dayNumber = 98)
        val shuttle = buildStat(Shuttle, dayNumber = 97)

        // when
        val result = sortAppStats(listOf(mail, telegram, shuttle))

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `sorts by stars count`() = runTest(dispatcher) {
        // given
        val expected = listOf(
            Telegram,
            Mail
        ).map(::AppId)
        val firstTelegram = buildStat(Telegram, minuteOfTheDay = 1)
        val secondTelegram = buildStat(Telegram, minuteOfTheDay = 2)
        val mail = buildStat(Mail)

        // when
        val result = sortAppStats(listOf(firstTelegram, secondTelegram, mail))

        // then
        assertEquals(expected, result)
    }

    companion object TestData {

        private val CurrentDatabaseDate = DatabaseDate(100)

        private const val Mail = "Mail"
        private const val Shuttle = "Shuttle"
        private const val Telegram = "Telegram"

        private fun buildStat(appId: String, dayNumber: Int = 100, minuteOfTheDay: Int = 0) = Stat(
            appId = DatabaseAppId(appId),
            date = DatabaseDate(dayNumber),
            geoHash = null,
            time = DatabaseTime(minuteOfTheDay)
        )
    }
}
