package shuttle.design.model

import android.graphics.drawable.Drawable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

data class WidgetPreviewUiModel(
    val apps: List<WidgetPreviewAppUiModel>,
    val layout: WidgetLayoutUiModel
)

data class WidgetPreviewAppUiModel(
    val name: String,
    val icon: Drawable
)

data class WidgetLayoutUiModel(
    val rowsCount: Int,
    val columnsCount: Int,
    val iconSize: Dp,
    val horizontalSpacing: Dp,
    val verticalSpacing: Dp,
    val textSize: TextUnit,
    val allowTwoLines: Boolean
)
