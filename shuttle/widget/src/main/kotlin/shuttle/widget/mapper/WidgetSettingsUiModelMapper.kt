package shuttle.widget.mapper

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.core.annotation.Factory
import shuttle.settings.domain.model.WidgetSettings
import shuttle.widget.model.WidgetSettingsUiModel

@Factory
internal class WidgetSettingsUiModelMapper {

    fun toUiModel(settings: WidgetSettings) = WidgetSettingsUiModel(
        columnsCount = settings.columnCount,
        horizontalSpacing = settings.horizontalSpacing.value.dp,
        iconSize = settings.iconsSize.value.dp,
        maxLines = if (settings.allowTwoLines) 2 else 1,
        rowsCount = settings.rowCount,
        textSize = settings.textSize.value.sp,
        transparency = settings.transparency / TransparencyIntToFloatRation,
        useMaterialColors = settings.useMaterialColors,
        verticalSpacing = settings.verticalSpacing.value.dp
    )

    companion object {

        const val TransparencyIntToFloatRation = 100f
    }
}
