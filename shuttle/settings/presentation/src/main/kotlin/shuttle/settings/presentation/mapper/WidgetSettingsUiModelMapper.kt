package shuttle.settings.presentation.mapper

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import shuttle.settings.domain.model.WidgetSettings
import shuttle.settings.presentation.model.WidgetSettingsUiModel

class WidgetSettingsUiModelMapper {

    fun toUiModel(settings: WidgetSettings) = WidgetSettingsUiModel(
        rowsCount = settings.rowsCount,
        columnsCount = settings.columnsCount,
        iconSize = settings.iconSize.value.dp,
        spacing = settings.spacing.value.dp,
        textSize = settings.textSize.value.sp
    )
}
