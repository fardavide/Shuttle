package shuttle.settings.domain.usecase

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import shuttle.apps.domain.model.AppId
import shuttle.apps.domain.model.AppModel
import shuttle.apps.domain.model.AppName
import shuttle.settings.domain.model.AppBlacklistSetting
import kotlin.test.Test

@RunWith(Parameterized::class)
class SearchAppsBlacklistSettingsTest(
    @Suppress("unused") private val testName: String,
    private val query: String,
    private val expected: List<AppBlacklistSetting>
) {

    private val observeAppsBlacklistSettings: ObserveAppsBlacklistSettings = mockk {
        every { this@mockk() } returns flowOf(AllAppBlacklistSettings)
    }
    private val search = SearchAppsBlacklistSettings(observeAppsBlacklistSettings)

    @Test
    fun `search on resulting flow`() = runTest {
        search(query).test {
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `search on another flow`() = runTest {
        search().test {
            assertEquals(AllAppBlacklistSettings, awaitItem())
            if (expected != AllAppBlacklistSettings) {
                search(query)
                assertEquals(expected, awaitItem())
            }
        }
    }

    data class Params(
        val testName: String,
        val query: String,
        val expected: List<AppBlacklistSetting>
    )

    companion object TestData {

        private val ShuttleApp = AppBlacklistSetting(
            app = buildAppModel("Shuttle"),
            inBlacklist = false
        )
        private val TelegramApp = AppBlacklistSetting(
            app = buildAppModel("Telegram"),
            inBlacklist = false
        )
        private val TwitchApp = AppBlacklistSetting(
            app = buildAppModel("Twitch"),
            inBlacklist = false
        )
        private val TwitterApp = AppBlacklistSetting(
            app = buildAppModel("Twitter"),
            inBlacklist = false
        )
        private val AllAppBlacklistSettings = listOf(
            ShuttleApp,
            TelegramApp,
            TwitchApp,
            TwitterApp
        )

        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data() = listOf(

            Params(
                testName = "empty query",
                query = "",
                expected = AllAppBlacklistSettings
            ),

            Params(
                testName = "'Tw' query",
                query = "Tw",
                expected = listOf(TwitchApp, TwitterApp)
            ),

            Params(
                testName = "'le' query",
                query = "le",
                expected = listOf(ShuttleApp, TelegramApp)
            ),

            Params(
                testName = "different case query",
                query = "SHU",
                expected = listOf(ShuttleApp)
            ),

            Params(
                testName = "no matching query",
                query = "not matching",
                expected = emptyList()
            )

        ).map { arrayOf(it.testName, it.query, it.expected) }

        private fun buildAppModel(name: String) = AppModel(
            AppId(name),
            AppName(name)
        )
    }
}
