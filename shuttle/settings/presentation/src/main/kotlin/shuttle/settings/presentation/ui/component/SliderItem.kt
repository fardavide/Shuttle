package shuttle.settings.presentation.ui.component

import androidx.annotation.PluralsRes
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
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import shuttle.design.theme.Dimens
import shuttle.design.theme.ShuttleTheme
import shuttle.resources.R.plurals
import shuttle.resources.R.string
import shuttle.resources.TextRes
import shuttle.resources.string
import shuttle.settings.domain.model.WidgetSettings

@Composable
internal fun SliderItem(
    @StringRes title: Int,
    valueRange: IntRange,
    stepsSize: Int,
    value: Int,
    onValueChange: (Int) -> Unit
) {
    var currentValue by remember(title) { mutableStateOf(SliderItem.IntValue(value)) }
    SliderItemContainer(title = title, currentValueText = currentValue.text) {
        Slider(
            valueRange = valueRange.first.toFloat()..valueRange.last.toFloat(),
            steps = (valueRange.last - valueRange.first - 1) / stepsSize,
            value = currentValue.value,
            onValueChange = { float ->
                currentValue = SliderItem.IntValue(float)
                onValueChange(currentValue.value.toInt())
            }
        )
    }
}

@Composable
internal fun <V : SliderItem.FloatValue> SliderItem(
    @StringRes title: Int,
    values: ImmutableList<V>,
    value: V,
    onValueChange: (V) -> Unit,
    stepSize: Int = 1
) {
    var currentValue by remember(title) { mutableStateOf(value) }
    SliderItemContainer(title = title, currentValueText = currentValue.text) {
        Slider(
            valueRange = values.first().value..values.last().value,
            steps = (values.size - 2) / stepSize,
            value = currentValue.value,
            onValueChange = { float ->
                currentValue = values.first { it.value == float }
                onValueChange(currentValue)
            }
        )
    }
}

@Composable
private fun SliderItemContainer(
    @StringRes title: Int,
    currentValueText: TextRes,
    content: @Composable () -> Unit
) {
    Column(modifier = Modifier.padding(vertical = Dimens.Margin.Small, horizontal = Dimens.Margin.Medium)) {
        Row {
            Text(text = stringResource(id = title), style = MaterialTheme.typography.titleMedium)
            Text(
                text = string(textRes = currentValueText),
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }
        content()
    }
}

internal object SliderItem {

    sealed interface FloatValue {
        val text: TextRes
        val value: Float
    }

    @JvmInline
    value class IntValue internal constructor(
        override val value: Float
    ) : FloatValue {

        override val text get() = TextRes(value.toInt().toString())
        constructor(value: Int) : this(value.toFloat())
    }

    data class NamedValue internal constructor(
        override val text: TextRes,
        override val value: Float
    ) : FloatValue {

        constructor(name: TextRes, value: Int) : this(name, value.toFloat())
        constructor(@PluralsRes nameRes: Int, quantity: Int, value: Int) :
            this(TextRes.plural(nameRes, quantity = quantity, quantity), value.toFloat())
    }
}

@Preview
@Composable
private fun SliderItemPreview() {
    var currentValue by remember { mutableStateOf(WidgetSettings.Default.rowCount) }
    ShuttleTheme {
        SliderItem(
            title = string.settings_widget_layout_rows_count,
            valueRange = WidgetSettings.RowsCountRange,
            stepsSize = 1,
            value = currentValue,
            onValueChange = { currentValue = it }
        )
    }
}

@Preview
@Composable
private fun NamedValueSliderItemPreview() {
    val values: ImmutableList<SliderItem.NamedValue> = persistentListOf(
        SliderItem.NamedValue(plurals.settings_statistics_months, quantity = 1, value = 0),
        SliderItem.NamedValue(plurals.settings_statistics_months, quantity = 2, value = 1),
        SliderItem.NamedValue(plurals.settings_statistics_months, quantity = 3, value = 2),
        SliderItem.NamedValue(plurals.settings_statistics_months, quantity = 6, value = 3),
        SliderItem.NamedValue(TextRes(string.settings_statistics_keep_forever), value = 4)
    )
    var currentValue by remember { mutableStateOf(values.first()) }
    ShuttleTheme {
        SliderItem(
            title = string.settings_statistics_keep_for,
            values = values,
            value = currentValue,
            onValueChange = { currentValue = it }
        )
    }
}
