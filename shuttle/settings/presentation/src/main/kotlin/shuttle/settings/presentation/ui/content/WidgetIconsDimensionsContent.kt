package shuttle.settings.presentation.ui.content

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import shuttle.design.model.WidgetLayoutUiModel
import shuttle.settings.domain.model.WidgetSettings
import shuttle.settings.presentation.ui.component.SliderItem
import studio.forface.shuttle.design.R

@Composable
internal fun WidgetIconsDimensionsContent(
    settings: WidgetLayoutUiModel,
    onIconSizeUpdated: (Int) -> Unit,
    onHorizontalSpacingUpdated: (Int) -> Unit,
    onVerticalSpacingUpdated: (Int) -> Unit
) {
    LazyColumn {
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
    }
}
