package shuttle.predictions.presentation.model

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

data class WidgetSettingsUiModel(
    val rowsCount: Int,
    val columnsCount: Int,
    val iconSize: Dp,
    val spacing: Dp,
    val textSize: TextUnit
)
