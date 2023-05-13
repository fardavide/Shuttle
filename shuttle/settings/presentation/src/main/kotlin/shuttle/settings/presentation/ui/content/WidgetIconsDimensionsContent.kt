package shuttle.settings.presentation.ui.content

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import shuttle.design.model.WidgetLayoutUiModel
import shuttle.resources.R
import shuttle.settings.domain.model.WidgetSettings
import shuttle.settings.presentation.ui.component.SliderItem

@Composable
internal fun WidgetIconsDimensionsContent(
    settings: WidgetLayoutUiModel,
    actions: WidgetIconsDimensionsContent.Actions
) {
    LazyColumn {
        item {
            SliderItem(
                title = R.string.settings_widget_layout_icons_size,
                valueRange = WidgetSettings.IconsSizeRange,
                stepsSize = 1,
                value = settings.iconSize.value.toInt(),
                onValueChange = actions.onIconSizeUpdated
            )
        }
        item {
            SliderItem(
                title = R.string.settings_widget_layout_horizontal_spacing,
                valueRange = WidgetSettings.HorizontalSpacingRange,
                stepsSize = 1,
                value = settings.horizontalSpacing.value.toInt(),
                onValueChange = actions.onHorizontalSpacingUpdated
            )
        }
        item {
            SliderItem(
                title = R.string.settings_widget_layout_vertical_spacing,
                valueRange = WidgetSettings.VerticalSpacingRange,
                stepsSize = 1,
                value = settings.verticalSpacing.value.toInt(),
                onValueChange = actions.onVerticalSpacingUpdated
            )
        }
    }
}

internal object WidgetIconsDimensionsContent {

    data class Actions(
        val onIconSizeUpdated: (Int) -> Unit,
        val onHorizontalSpacingUpdated: (Int) -> Unit,
        val onVerticalSpacingUpdated: (Int) -> Unit
    ) {

        companion object {

            val Empty = Actions(
                onIconSizeUpdated = {},
                onHorizontalSpacingUpdated = {},
                onVerticalSpacingUpdated = {}
            )
        }
    }
}
