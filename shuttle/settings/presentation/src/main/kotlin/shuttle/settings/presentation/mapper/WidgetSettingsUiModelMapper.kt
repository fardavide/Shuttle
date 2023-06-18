package shuttle.settings.presentation.mapper

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.core.annotation.Factory
import shuttle.design.model.WidgetLayoutUiModel
import shuttle.settings.domain.model.WidgetSettings

@Factory
class WidgetSettingsUiModelMapper {

    fun toUiModel(settings: WidgetSettings) = WidgetLayoutUiModel(
        allowTwoLines = settings.allowTwoLines,
        columnsCount = settings.columnCount,
        horizontalSpacing = settings.horizontalSpacing.value.dp,
        iconSize = settings.iconsSize.value.dp,
        rowsCount = settings.rowCount,
        textSize = settings.textSize.value.sp,
        transparency = settings.transparency,
        useMaterialColors = settings.useMaterialColors,
        verticalSpacing = settings.verticalSpacing.value.dp
    )
}
