package shuttle.settings.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import org.koin.androidx.compose.getViewModel
import shuttle.design.PreviewDimens
import shuttle.design.theme.Dimens
import shuttle.design.ui.LoadingSpinner
import shuttle.design.ui.TextError
import shuttle.design.util.collectAsStateLifecycleAware
import shuttle.settings.domain.model.WidgetSettings
import shuttle.settings.presentation.model.WidgetPreviewAppUiModel
import shuttle.settings.presentation.model.WidgetSettingsUiModel
import shuttle.settings.presentation.resources.Strings
import shuttle.settings.presentation.viewmodel.WidgetSettingsViewModel
import shuttle.settings.presentation.viewmodel.WidgetSettingsViewModel.Action
import shuttle.settings.presentation.viewmodel.WidgetSettingsViewModel.State

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun WidgetSettingsPage() {
    Scaffold(topBar = { SmallTopAppBar(title = { Text(Strings.WidgetSettings.Title) }) }) {
        WidgetSettingsContent()
    }
}

@Composable
private fun WidgetSettingsContent() {
    val viewModel: WidgetSettingsViewModel = getViewModel()

    val s by viewModel.state.collectAsStateLifecycleAware()
    @Suppress("UnnecessaryVariable")
    when (val state = s) {
        State.Loading -> LoadingSpinner()
        is State.Data -> WidgetSettings(
            data = state,
            onRowsUpdated = { viewModel.submit(Action.UpdateRows(it)) },
            onColumnsUpdated = { viewModel.submit(Action.UpdateColumns(it)) },
            onIconSizeUpdated = { viewModel.submit(Action.UpdateIconSize(it)) },
            onSpacingUpdated = { viewModel.submit(Action.UpdateSpacing(it)) },
            onTextSizeUpdated = { viewModel.submit(Action.UpdateTextSize(it)) }
        )
        is State.Error -> TextError(text = state.message)
    }
}

@Composable
private fun WidgetSettings(
    data: State.Data,
    onRowsUpdated: (Int) -> Unit,
    onColumnsUpdated: (Int) -> Unit,
    onIconSizeUpdated: (Int) -> Unit,
    onSpacingUpdated: (Int) -> Unit,
    onTextSizeUpdated: (Int) -> Unit
) {
    Column {
        WidgetPreview(previewApps = data.previewApps, widgetSettings = data.widgetSettingsUiModel)
        SettingItems(
            settings = data.widgetSettingsUiModel,
            onRowsUpdated = onRowsUpdated,
            onColumnsUpdated = onColumnsUpdated,
            onIconSizeUpdated = onIconSizeUpdated,
            onSpacingUpdated = onSpacingUpdated,
            onTextSizeUpdated = onTextSizeUpdated
        )
    }
}

@Composable
private fun WidgetPreview(
    previewApps: List<WidgetPreviewAppUiModel>,
    widgetSettings: WidgetSettingsUiModel
) {
    val rows = widgetSettings.rowsCount
    val columns = widgetSettings.columnsCount
    val apps = previewApps.take(rows * columns).reversed()
    var index = 0

    Column(
        modifier = Modifier
            .padding(Dimens.Margin.Medium)
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(Dimens.Margin.Large)
            )
            .wrapContentSize()
    ) {
        repeat(rows) {
            Row {
                repeat(columns) {
                    AppIconItem(
                        app = apps[index++],
                        widgetSettings = widgetSettings
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingItems(
    settings: WidgetSettingsUiModel,
    onRowsUpdated: (Int) -> Unit,
    onColumnsUpdated: (Int) -> Unit,
    onIconSizeUpdated: (Int) -> Unit,
    onSpacingUpdated: (Int) -> Unit,
    onTextSizeUpdated: (Int) -> Unit
) {
    Column {
        SliderItem(
            title = Strings.WidgetSettings.RowsCount,
            valueRange = 1..5,
            stepsSize = 1,
            value = settings.rowsCount,
            onValueChange = onRowsUpdated
        )
        SliderItem(
            title = Strings.WidgetSettings.ColumnsCount,
            valueRange = 3..10,
            stepsSize = 1,
            value = settings.columnsCount,
            onValueChange = onColumnsUpdated
        )
        SliderItem(
            title = Strings.WidgetSettings.IconsSize,
            valueRange = 24..56,
            stepsSize = 1,
            value = settings.iconSize.value.toInt(),
            onValueChange = onIconSizeUpdated
        )
        SliderItem(
            title = Strings.WidgetSettings.Spacing,
            valueRange = 2..16,
            stepsSize = 1,
            value = settings.spacing.value.toInt(),
            onValueChange = onSpacingUpdated
        )
        SliderItem(
            title = Strings.WidgetSettings.TextSize,
            valueRange = 6..16,
            stepsSize = 1,
            value = settings.textSize.value.toInt(),
            onValueChange = onTextSizeUpdated
        )
    }
}

@Composable
private fun SliderItem(
    title: String,
    valueRange: IntRange,
    stepsSize: Int,
    value: Int,
    onValueChange: (Int) -> Unit
) {
    var state by remember(key1 = title) { mutableStateOf(value.toFloat()) }
    Column(modifier = Modifier.padding(vertical = Dimens.Margin.Small, horizontal = Dimens.Margin.Medium)) {
        Row {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Text(
                text = "$value",
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Slider(
            valueRange = valueRange.first.toFloat()..valueRange.last.toFloat(),
            steps = (valueRange.last - valueRange.first) / stepsSize,
            value = state,
            onValueChange = {
                state = it
                onValueChange(it.toInt())
            }
        )
    }
}

@Composable
private fun AppIconItem(
    app: WidgetPreviewAppUiModel,
    widgetSettings: WidgetSettingsUiModel
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = widgetSettings.spacing / 2, horizontal = widgetSettings.spacing)
    ) {

        Image(
            painter = rememberImagePainter(data = app.icon),
            contentDescription = Strings.AppIconContentDescription,
            modifier = Modifier.size(widgetSettings.iconSize)
        )
        Spacer(modifier = Modifier.height(widgetSettings.spacing))
        Text(
            text = app.name,
            maxLines = 1,
            fontSize = widgetSettings.textSize,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(widgetSettings.iconSize)
        )
    }
}

@Composable
@Preview(showBackground = true, widthDp = PreviewDimens.Medium.Width, heightDp = PreviewDimens.Medium.Height)
private fun WidgetPreviewPreview() {
    val icon = LocalContext.current.getDrawable(androidx.core.R.drawable.notification_bg_low)!!
    val apps = listOf(
        WidgetPreviewAppUiModel("Shuttle", icon)
    )
    val widgetSettings = WidgetSettingsUiModel(
        rowsCount = WidgetSettings.Default.rowsCount,
        columnsCount = WidgetSettings.Default.columnsCount,
        iconSize = WidgetSettings.Default.iconSize.value.dp,
        spacing = WidgetSettings.Default.spacing.value.dp,
        textSize = WidgetSettings.Default.textSize.value.sp,
    )
    MaterialTheme {
        WidgetPreview(previewApps = apps, widgetSettings = widgetSettings)
    }
}

@Composable
@Preview(showBackground = true, widthDp = PreviewDimens.Medium.Width, heightDp = PreviewDimens.Medium.Height)
private fun SettingItemsPreview() {
    val widgetSettings = WidgetSettingsUiModel(
        rowsCount = WidgetSettings.Default.rowsCount,
        columnsCount = WidgetSettings.Default.columnsCount,
        iconSize = WidgetSettings.Default.iconSize.value.dp,
        spacing = WidgetSettings.Default.spacing.value.dp,
        textSize = WidgetSettings.Default.textSize.value.sp,
    )
    MaterialTheme {
        SettingItems(widgetSettings, {}, {}, {}, {}, {})
    }
}
