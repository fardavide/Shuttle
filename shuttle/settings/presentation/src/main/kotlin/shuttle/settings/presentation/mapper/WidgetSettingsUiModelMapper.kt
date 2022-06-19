package shuttle.settings.presentation.mapper

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import shuttle.design.model.WidgetLayoutUiModel
import shuttle.settings.domain.model.WidgetSettings

class WidgetSettingsUiModelMapper {

    fun toUiModel(settings: WidgetSettings) = WidgetLayoutUiModel(
        allowTwoLines = settings.allowTwoLines,
        columnsCount = settings.columnsCount,
        horizontalSpacing = settings.horizontalSpacing.value.dp,
        iconSize = settings.iconsSize.value.dp,
        rowsCount = settings.rowsCount,
        showRefreshLocation = settings.showRefreshLocation,
        textSize = settings.textSize.value.sp,
        verticalSpacing = settings.verticalSpacing.value.dp
    )
}
