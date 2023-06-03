package shuttle.settings.presentation.ui.content

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import shuttle.design.model.WidgetLayoutUiModel
import shuttle.resources.R.string
import shuttle.settings.domain.model.WidgetSettings
import shuttle.settings.presentation.ui.component.SliderItem
import shuttle.settings.presentation.ui.component.SwitchItem

@Composable
internal fun WidgetAppsLabelsContent(
    settings: WidgetLayoutUiModel,
    actions: WidgetAppsLabelsContent.Actions
) {
    LazyColumn {
        item {
            SliderItem(
                title = string.settings_widget_layout_text_size,
                valueRange = WidgetSettings.TextSizeRange,
                stepsSize = 1,
                value = settings.textSize.value.toInt(),
                onValueChange = actions.onTextSizeUpdated
            )
        }
        item {
            SwitchItem(
                title = string.settings_widget_layout_two_lines,
                value = settings.allowTwoLines,
                onValueChange = actions.onAllowTwoLinesUpdated
            )
        }
    }
}

internal object WidgetAppsLabelsContent {

    data class Actions(
        val onTextSizeUpdated: (Int) -> Unit,
        val onAllowTwoLinesUpdated: (Boolean) -> Unit
    ) {

        companion object {

            val Empty = Actions(
                onTextSizeUpdated = {},
                onAllowTwoLinesUpdated = {}
            )
        }
    }
}
