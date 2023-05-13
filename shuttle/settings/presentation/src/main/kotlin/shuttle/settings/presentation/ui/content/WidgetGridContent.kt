package shuttle.settings.presentation.ui.content

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import shuttle.design.model.WidgetLayoutUiModel
import shuttle.resources.R
import shuttle.settings.domain.model.WidgetSettings
import shuttle.settings.presentation.ui.component.SliderItem

@Composable
internal fun WidgetGridContent(settings: WidgetLayoutUiModel, actions: WidgetGridContent.Actions) {
    LazyColumn {
        item {
            SliderItem(
                title = R.string.settings_widget_layout_rows_count,
                valueRange = WidgetSettings.RowsCountRange,
                stepsSize = 1,
                value = settings.rowsCount,
                onValueChange = actions.onRowsUpdated
            )
        }
        item {
            SliderItem(
                title = R.string.settings_widget_layout_columns_count,
                valueRange = WidgetSettings.ColumnsCountRange,
                stepsSize = 1,
                value = settings.columnsCount,
                onValueChange = actions.onColumnsUpdated
            )
        }
    }
}

internal object WidgetGridContent {

    data class Actions(
        val onRowsUpdated: (Int) -> Unit,
        val onColumnsUpdated: (Int) -> Unit
    ) {

        companion object {

            val Empty = Actions(
                onRowsUpdated = {},
                onColumnsUpdated = {}
            )
        }
    }
}
