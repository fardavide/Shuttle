package shuttle.settings.presentation.mapper

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import shuttle.settings.domain.model.WidgetSettings
import shuttle.settings.presentation.model.WidgetSettingsUiModel

class WidgetSettingsUiModelMapper {

    fun toUiModel(settings: WidgetSettings) = WidgetSettingsUiModel(
        rowsCount = settings.rowsCount,
        columnsCount = settings.columnsCount,
        iconSize = settings.iconsSize.value.dp,
        horizontalSpacing = settings.horizontalSpacing.value.dp,
        verticalSpacing = settings.verticalSpacing.value.dp,
        textSize = settings.textSize.value.sp,
        allowTwoLines = settings.allowTwoLines
    )
}
