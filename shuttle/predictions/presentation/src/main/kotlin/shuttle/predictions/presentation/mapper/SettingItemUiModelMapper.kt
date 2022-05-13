package shuttle.predictions.presentation.mapper

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import shuttle.predictions.presentation.model.WidgetSettingsUiModel
import shuttle.settings.domain.model.WidgetSettings

class WidgetSettingsUiModelMapper {

    fun toUiModel(settings: WidgetSettings) = WidgetSettingsUiModel(
        rowsCount = settings.rowsCount,
        columnsCount = settings.columnsCount,
        iconSize = settings.iconsSize.value.dp,
        horizontalSpacing = settings.horizontalSpacing.value.dp,
        verticalSpacing = settings.verticalSpacing.value.dp,
        textSize = settings.textSize.value.sp,
        maxLines = if (settings.allowTwoLines) 2 else 1
    )
}
