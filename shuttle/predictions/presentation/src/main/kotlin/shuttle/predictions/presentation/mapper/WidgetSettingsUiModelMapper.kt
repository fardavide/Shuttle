package shuttle.predictions.presentation.mapper

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import shuttle.predictions.presentation.model.WidgetSettingsUiModel
import shuttle.settings.domain.model.WidgetSettings

class WidgetSettingsUiModelMapper {

    fun toUiModel(settings: WidgetSettings) = WidgetSettingsUiModel(
        columnsCount = settings.columnsCount,
        horizontalSpacing = settings.horizontalSpacing.value.dp,
        iconSize = settings.iconsSize.value.dp,
        maxLines = if (settings.allowTwoLines) 2 else 1,
        rowsCount = settings.rowsCount,
        textSize = settings.textSize.value.sp,
        verticalSpacing = settings.verticalSpacing.value.dp
    )
}
