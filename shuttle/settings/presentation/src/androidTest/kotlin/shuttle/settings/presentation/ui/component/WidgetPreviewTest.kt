package shuttle.settings.presentation.ui.component

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toDrawable
import androidx.test.core.app.ApplicationProvider
import org.junit.Rule
import shuttle.settings.presentation.model.WidgetPreviewAppUiModel
import shuttle.settings.presentation.model.WidgetSettingsUiModel
import shuttle.test.compose.createComposeTestRule
import kotlin.test.Test

internal class WidgetPreviewTest {

    @get:Rule
    val composeTestRule = createComposeTestRule()

    @Test
    fun givenAListOfApps_whenTheGridHasMoreSlotsThanTheApps_thenAddsEmptySpacesWithoutCrashing() {
        // given
        val previewApps = buildPreviewApps(count = 2)

        // when
        val widgetSettings = buildWidgetSettings(rowsCount = 2, columnsCount = 2)
        setContentWithState(previewApps, widgetSettings)

        // then doesn't crash
    }

    private fun setContentWithState(
        previewApps: List<WidgetPreviewAppUiModel>,
        widgetSettings: WidgetSettingsUiModel
    ) {
        composeTestRule.setContent { WidgetPreview(previewApps, widgetSettings) }
    }

    private companion object TestData {

        val EmptyDrawable = Bitmap
            .createBitmap(1, 1, Bitmap.Config.ARGB_8888)
            .toDrawable(ApplicationProvider.getApplicationContext<Context>().resources)

        val PreviewApp = WidgetPreviewAppUiModel(
            name = "Shuttle",
            icon = EmptyDrawable
        )

        fun buildPreviewApps(count: Int): List<WidgetPreviewAppUiModel> =
            (0 until count).map { PreviewApp }

        fun buildWidgetSettings(
            rowsCount: Int = 1,
            columnsCount: Int = 1
        ) = WidgetSettingsUiModel(
            rowsCount = rowsCount,
            columnsCount = columnsCount,
            iconSize = 1.dp,
            horizontalSpacing = 1.dp,
            verticalSpacing = 1.dp,
            textSize = 1.sp,
            allowTwoLines = false
        )
    }
}
