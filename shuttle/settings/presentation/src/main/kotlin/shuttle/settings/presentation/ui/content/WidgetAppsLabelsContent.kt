package shuttle.settings.presentation.ui.content

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import shuttle.design.model.WidgetLayoutUiModel
import shuttle.settings.domain.model.WidgetSettings
import shuttle.settings.presentation.ui.component.SliderItem
import shuttle.settings.presentation.ui.component.SwitchItem
import studio.forface.shuttle.design.R

@Composable
internal fun WidgetAppsLabelsContent(
    settings: WidgetLayoutUiModel,
    onTextSizeUpdated: (Int) -> Unit,
    onAllowTwoLinesUpdated: (Boolean) -> Unit
) {
    LazyColumn {
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
