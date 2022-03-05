package shuttle.predictions.presentation.mapper

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import shuttle.predictions.presentation.model.WidgetSettingsUiModel
import shuttle.settings.domain.model.WidgetSettings

class WidgetSettingsUiModelMapper {

    fun toUiModel(settings: WidgetSettings) = WidgetSettingsUiModel(
        rowsCount = settings.rowsCount,
        columnsCount = settings.columnsCount,
        iconSize = settings.iconSize.value.dp,
        spacing = settings.spacing.value.dp,
        textSize = settings.textSize.value.sp
    )
}
