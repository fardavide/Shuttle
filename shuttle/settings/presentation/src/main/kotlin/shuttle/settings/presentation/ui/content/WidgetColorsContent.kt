package shuttle.settings.presentation.ui.content

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import shuttle.design.model.WidgetLayoutUiModel
import shuttle.resources.R.string
import shuttle.settings.domain.model.WidgetSettings
import shuttle.settings.presentation.ui.component.SliderItem
import shuttle.settings.presentation.ui.component.SwitchItem

@Composable
internal fun WidgetColorsContent(settings: WidgetLayoutUiModel, actions: WidgetColorsContent.Actions) {
    LazyColumn {
        item {
            SwitchItem(
                title = string.settings_widget_colors_use_material_colors,
                value = settings.useMaterialColors,
                onValueChange = actions.onUseMaterialColorsUpdated
            )
        }
        item {
            SliderItem(
                title = string.settings_widget_colors_transparency,
                valueRange = WidgetSettings.TransparencyRange,
                stepsSize = 1,
                value = settings.transparency,
                onValueChange = actions.onTransparencyUpdated
            )
        }
    }
}

internal object WidgetColorsContent {

    data class Actions(
        val onTransparencyUpdated: (Int) -> Unit,
        val onUseMaterialColorsUpdated: (Boolean) -> Unit
    ) {

        companion object {

            val Empty = Actions(
                onTransparencyUpdated = {},
                onUseMaterialColorsUpdated = {}
            )
        }
    }
}
