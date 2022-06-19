package shuttle.predictions.presentation.model

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

data class WidgetSettingsUiModel(
    val columnsCount: Int,
    val horizontalSpacing: Dp,
    val iconSize: Dp,
    val maxLines: Int,
    val rowsCount: Int,
    val textSize: TextUnit,
    val verticalSpacing: Dp
)
