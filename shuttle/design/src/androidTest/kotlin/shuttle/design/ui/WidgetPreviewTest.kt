package shuttle.design.ui

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toDrawable
import androidx.test.core.app.ApplicationProvider
import shuttle.design.model.WidgetLayoutUiModel
import shuttle.design.model.WidgetPreviewAppUiModel
import shuttle.design.model.WidgetPreviewUiModel
import shuttle.test.compose.ComposeTest
import shuttle.test.compose.runComposeTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
internal class WidgetPreviewTest {

    @Test
    fun givenAListOfApps_whenTheGridHasMoreSlotsThanTheApps_thenAddsEmptySpacesWithoutCrashing() = runComposeTest {
        // given
        val previewApps = buildPreviewApps(count = 2)

        // when
        val widgetSettings = buildWidgetSettings(rowsCount = 2, columnsCount = 2)
        setContentWithState(previewApps, widgetSettings)

        // then doesn't crash
    }

    private fun ComposeTest.setContentWithState(
        previewApps: List<WidgetPreviewAppUiModel>,
        widgetSettings: WidgetLayoutUiModel
    ) {
        setContent { WidgetPreview(WidgetPreviewUiModel(previewApps, widgetSettings)) }
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
        ) = WidgetLayoutUiModel(
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
