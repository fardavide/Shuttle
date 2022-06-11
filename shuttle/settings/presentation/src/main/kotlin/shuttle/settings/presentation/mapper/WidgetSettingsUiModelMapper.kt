package shuttle.settings.presentation.mapper

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import shuttle.design.model.WidgetLayoutUiModel
import shuttle.settings.domain.model.WidgetSettings

class WidgetSettingsUiModelMapper {

    fun toUiModel(settings: WidgetSettings) = WidgetLayoutUiModel(
        rowsCount = settings.rowsCount,
        columnsCount = settings.columnsCount,
        iconSize = settings.iconsSize.value.dp,
        horizontalSpacing = settings.horizontalSpacing.value.dp,
        verticalSpacing = settings.verticalSpacing.value.dp,
        textSize = settings.textSize.value.sp,
        allowTwoLines = settings.allowTwoLines
    )
}
