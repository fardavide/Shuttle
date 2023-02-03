package shuttle.design.model

import android.graphics.drawable.Drawable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import arrow.optics.optics

@optics data class WidgetPreviewUiModel(
    val apps: List<WidgetPreviewAppUiModel>,
    val layout: WidgetLayoutUiModel
) {
    companion object
}

@optics data class WidgetPreviewAppUiModel(
    val name: String,
    val icon: Drawable
) {
    companion object
}

@optics data class WidgetLayoutUiModel(
    val allowTwoLines: Boolean,
    val columnsCount: Int,
    val horizontalSpacing: Dp,
    val iconSize: Dp,
    val rowsCount: Int,
    val textSize: TextUnit,
    val transparency: Int,
    val useMaterialColors: Boolean,
    val verticalSpacing: Dp
) {
    companion object
}
