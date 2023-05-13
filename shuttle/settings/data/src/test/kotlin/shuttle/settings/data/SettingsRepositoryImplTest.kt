package shuttle.settings.data

import app.cash.turbine.test
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import shuttle.database.datasource.SettingDataSource
import shuttle.settings.data.util.mockDataStore
import shuttle.settings.domain.model.Dp
import shuttle.settings.domain.model.Sp
import shuttle.settings.domain.model.WidgetSettings

class SettingsRepositoryImplTest : AnnotationSpec() {

    private val settingDataSource: SettingDataSource = mockk()
    private val repository = SettingsRepositoryImpl(
        dataStoreProvider = ::mockDataStore,
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
            transparency = 15,
            useMaterialColors = false,
            verticalSpacing = Dp(25)
        )
        settings.allowTwoLines shouldNotBe WidgetSettings.Default.allowTwoLines

        // when
        repository.updateWidgetSettings(settings)

        // then
        repository.observeWidgetSettings().test {
            awaitItem() shouldBe settings
        }
    }
}
