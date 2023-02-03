package shuttle.settings.presentation.ui.content

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import shuttle.design.model.WidgetLayoutUiModel
import shuttle.settings.domain.model.WidgetSettings
import shuttle.settings.presentation.ui.component.SliderItem
import shuttle.settings.presentation.ui.component.SwitchItem
import studio.forface.shuttle.design.R

@Composable
internal fun WidgetColorsContent(
    settings: WidgetLayoutUiModel,
    onTransparencyUpdated: (Int) -> Unit,
    onUseMaterialColorsUpdated: (Boolean) -> Unit
) {
    LazyColumn {
        item {
            SwitchItem(
                title = R.string.settings_widget_colors_use_material_colors,
                value = settings.useMaterialColors,
                onValueChange = onUseMaterialColorsUpdated
            )
        }
        item {
            SliderItem(
                title = R.string.settings_widget_colors_transparency,
                valueRange = WidgetSettings.TransparencyRange,
                stepsSize = 1,
                value = settings.transparency,
                onValueChange = onTransparencyUpdated
            )
        }
    }
}
