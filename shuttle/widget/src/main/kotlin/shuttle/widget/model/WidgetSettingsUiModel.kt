package shuttle.widget.model

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

internal data class WidgetSettingsUiModel(
    val columnsCount: Int,
    val horizontalSpacing: Dp,
    val iconSize: Dp,
    val maxLines: Int,
    val rowsCount: Int,
    val textSize: TextUnit,
    val transparency: Float,
    val useMaterialColors: Boolean,
    val verticalSpacing: Dp
)
