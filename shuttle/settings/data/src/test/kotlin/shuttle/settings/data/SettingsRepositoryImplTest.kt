package shuttle.settings.data

import app.cash.turbine.test
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import shuttle.database.datasource.SettingDataSource
import shuttle.settings.data.util.mockDataStore
import shuttle.settings.domain.model.Dp
import shuttle.settings.domain.model.Sp
import shuttle.settings.domain.model.WidgetSettings
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class SettingsRepositoryImplTest {

    private val migratePreferences: MigratePreferences = mockk(relaxUnitFun = true)
    private val settingDataSource: SettingDataSource = mockk()
    private val repository = SettingsRepositoryImpl(
        dataStore = mockDataStore(),
        migratePreferences = migratePreferences,
        settingDataSource = settingDataSource
    )

    @Test
    fun `saves all the widget settings`() = runTest {
        // given
        val settings = WidgetSettings(
            allowTwoLines = true,
            columnsCount = 20,
            horizontalSpacing = Dp(21),
            iconsSize = Dp(22),
            rowsCount = 23,
            textSize = Sp(24),
            verticalSpacing = Dp(25)
        )
        assertNotEquals(settings.allowTwoLines, WidgetSettings.Default.allowTwoLines)

        // when
        repository.updateWidgetSettings(settings)

        // then
        repository.observeWidgetSettings().test {
            assertEquals(settings, awaitItem())
        }
    }
}
