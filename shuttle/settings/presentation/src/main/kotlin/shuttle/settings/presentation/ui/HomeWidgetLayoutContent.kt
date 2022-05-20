package shuttle.settings.presentation.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import shuttle.design.PreviewDimens
import shuttle.settings.domain.model.WidgetSettings
import shuttle.settings.presentation.model.WidgetSettingsUiModel
import shuttle.settings.presentation.ui.component.SectionItem
import shuttle.settings.presentation.ui.component.SliderItem
import shuttle.settings.presentation.ui.component.SwitchItem
import studio.forface.shuttle.design.R

@Composable
internal fun HomeWidgetLayoutContent(
    settings: WidgetSettingsUiModel,
    toGrid: () -> Unit,
    onIconSizeUpdated: (Int) -> Unit,
    onHorizontalSpacingUpdated: (Int) -> Unit,
    onVerticalSpacingUpdated: (Int) -> Unit,
    onTextSizeUpdated: (Int) -> Unit,
    onAllowTwoLinesUpdated: (Boolean) -> Unit
) {
    LazyColumn {
        item {
            SectionItem(
                title = R.string.settings_widget_layout_grid,
                icon = 0,
                onClick = toGrid
            )
        }
        item {
            SliderItem(
                title = R.string.settings_widget_layout_icons_size,
                valueRange = WidgetSettings.IconsSizeRange,
                stepsSize = 1,
                value = settings.iconSize.value.toInt(),
                onValueChange = onIconSizeUpdated
            )
        }
        item {
            SliderItem(
                title = R.string.settings_widget_layout_horizontal_spacing,
                valueRange = WidgetSettings.HorizontalSpacingRange,
                stepsSize = 1,
                value = settings.horizontalSpacing.value.toInt(),
                onValueChange = onHorizontalSpacingUpdated
            )
        }
        item {
            SliderItem(
                title = R.string.settings_widget_layout_vertical_spacing,
                valueRange = WidgetSettings.VerticalSpacingRange,
                stepsSize = 1,
                value = settings.verticalSpacing.value.toInt(),
                onValueChange = onVerticalSpacingUpdated
            )
        }
        item {
            SliderItem(
                title = R.string.settings_widget_layout_text_size,
                valueRange = WidgetSettings.TextSizeRange,
                stepsSize = 1,
                value = settings.textSize.value.toInt(),
                onValueChange = onTextSizeUpdated
            )
        }
        item {
            SwitchItem(
                title = R.string.settings_widget_layout_two_lines,
                value = settings.allowTwoLines,
                onValueChange = onAllowTwoLinesUpdated
            )
        }
    }
}

@Composable
@Preview(showBackground = true, widthDp = PreviewDimens.Medium.Width, heightDp = PreviewDimens.Medium.Height)
private fun HomeWidgetLayoutContentPreview() {
    val widgetSettings = WidgetSettingsUiModel(
        rowsCount = WidgetSettings.Default.rowsCount,
        columnsCount = WidgetSettings.Default.columnsCount,
        iconSize = WidgetSettings.Default.iconsSize.value.dp,
        horizontalSpacing = WidgetSettings.Default.horizontalSpacing.value.dp,
        verticalSpacing = WidgetSettings.Default.verticalSpacing.value.dp,
        textSize = WidgetSettings.Default.textSize.value.sp,
        allowTwoLines = WidgetSettings.Default.allowTwoLines
    )
    MaterialTheme {
        HomeWidgetLayoutContent(widgetSettings, {}, {}, {}, {}, {}) {}
    }
}
