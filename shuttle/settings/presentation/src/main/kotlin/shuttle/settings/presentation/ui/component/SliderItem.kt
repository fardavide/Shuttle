package shuttle.settings.presentation.ui.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import shuttle.design.theme.Dimens
import shuttle.design.theme.ShuttleTheme
import shuttle.resources.R.string
import shuttle.settings.domain.model.WidgetSettings

@Composable
internal fun SliderItem(
    @StringRes title: Int,
    valueRange: IntRange,
    stepsSize: Int,
    value: Int,
    onValueChange: (Int) -> Unit
) {
    var state by remember(title) { mutableStateOf(value.toFloat()) }
    Column(modifier = Modifier.padding(vertical = Dimens.Margin.Small, horizontal = Dimens.Margin.Medium)) {
        Row {
            Text(text = stringResource(id = title), style = MaterialTheme.typography.titleMedium)
            Text(
                text = "$value",
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Slider(
            valueRange = valueRange.first.toFloat()..valueRange.last.toFloat(),
            steps = (valueRange.last - valueRange.first - 1) / stepsSize,
            value = state,
            onValueChange = {
                state = it
                onValueChange(it.toInt())
            }
        )
    }
}

@Preview
@Composable
private fun SliderItemPreview() {
    ShuttleTheme {
        SliderItem(
            title = string.settings_widget_layout_rows_count,
            valueRange = WidgetSettings.RowsCountRange,
            stepsSize = 1,
            value = WidgetSettings.Default.rowCount,
            onValueChange = {}
        )
    }
}
