package shuttle.settings.presentation.ui.content

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import shuttle.design.model.WidgetLayoutUiModel
import shuttle.settings.domain.model.WidgetSettings
import shuttle.settings.presentation.ui.component.SliderItem
import studio.forface.shuttle.design.R

@Composable
internal fun WidgetGridContent(
    settings: WidgetLayoutUiModel,
    onRowsUpdated: (Int) -> Unit,
    onColumnsUpdated: (Int) -> Unit
) {
    LazyColumn {
        item {
            SliderItem(
                title = R.string.settings_widget_layout_rows_count,
                valueRange = WidgetSettings.RowsCountRange,
                stepsSize = 1,
                value = settings.rowsCount,
                onValueChange = onRowsUpdated
            )
        }
        item {
            SliderItem(
                title = R.string.settings_widget_layout_columns_count,
                valueRange = WidgetSettings.ColumnsCountRange,
                stepsSize = 1,
                value = settings.columnsCount,
                onValueChange = onColumnsUpdated
            )
        }
    }
}
