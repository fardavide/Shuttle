package shuttle.settings.presentation.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import shuttle.design.PreviewDimens
import shuttle.design.theme.Dimens
import shuttle.design.ui.BackIconButton
import shuttle.design.ui.BottomSheetScaffold
import shuttle.design.ui.LoadingSpinner
import shuttle.settings.domain.model.Dp
import shuttle.settings.domain.model.Sp
import shuttle.settings.domain.model.WidgetSettings
import shuttle.settings.presentation.model.WidgetPreviewAppUiModel
import shuttle.settings.presentation.model.WidgetSettingsUiModel
import shuttle.settings.presentation.viewmodel.WidgetLayoutViewModel.State
import studio.forface.shuttle.design.R.drawable
import studio.forface.shuttle.design.R.string

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WidgetLayoutContainer(
    @StringRes title: Int,
    state: State,
    onBack: () -> Unit,
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current
    var previewHeight by remember { mutableStateOf(Dimens.Component.XXXLarge) }
    val draggableState = rememberDraggableState { pixelsDelta ->
        val dpDelta = with(density) { pixelsDelta.toDp() }
        previewHeight += dpDelta
    }
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(color = MaterialTheme.colorScheme.primaryContainer)
    ) {
        WidgetPreviewContent(state = state, height = previewHeight)
        BottomSheetScaffold(
            topBar = {
                TopBar(title = title, onBack = onBack, draggableState = draggableState)
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                content()
            }
        }
    }
}

@Composable
private fun TopBar(@StringRes title: Int, onBack: () -> Unit, draggableState: DraggableState) {
    Column {
        Icon(
            modifier = Modifier
                .fillMaxWidth()
                .draggable(orientation = Orientation.Vertical, state = draggableState),
            painter = painterResource(id = drawable.ic_vertical_drag),
            contentDescription = stringResource(id = string.settings_widget_layout_toolbar_drag_description)
        )
        SmallTopAppBar(
            colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Transparent),
            title = { Text(stringResource(id = title)) },
            navigationIcon = { BackIconButton(onBack) }
        )
    }
}

@Composable
private fun WidgetPreviewContent(state: State, height: androidx.compose.ui.unit.Dp) {
    Box(
        modifier = Modifier
            .height(height)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        when (state) {
            State.Loading, is State.Error -> LoadingSpinner()
            is State.Data -> WidgetPreview(
                previewApps = state.previewApps,
                widgetSettings = state.widgetSettingsUiModel
            )
        }
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
            .wrapContentSize()
            .padding(horizontal = widgetSettings.horizontalSpacing, vertical = widgetSettings.verticalSpacing)
            .background(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.78f),
                shape = RoundedCornerShape(Dimens.Margin.Large)
            )
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
private fun AppIconItem(
    app: WidgetPreviewAppUiModel,
    widgetSettings: WidgetSettingsUiModel
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(
            vertical = widgetSettings.verticalSpacing,
            horizontal = widgetSettings.horizontalSpacing
        )
    ) {

        Image(
            painter = rememberImagePainter(data = app.icon),
            contentDescription = stringResource(id = string.x_app_icon_description),
            modifier = Modifier.size(widgetSettings.iconSize)
        )
        Spacer(modifier = Modifier.height(widgetSettings.verticalSpacing))
        Text(
            text = app.name,
            maxLines = if (widgetSettings.allowTwoLines) 2 else 1,
            fontSize = widgetSettings.textSize,
            lineHeight = widgetSettings.textSize,
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
        iconSize = WidgetSettings.Default.iconsSize.dp,
        horizontalSpacing = WidgetSettings.Default.horizontalSpacing.dp,
        verticalSpacing = WidgetSettings.Default.verticalSpacing.dp,
        textSize = WidgetSettings.Default.textSize.sp,
        allowTwoLines = WidgetSettings.Default.allowTwoLines
    )
    MaterialTheme {
        WidgetPreview(previewApps = apps, widgetSettings = widgetSettings)
    }
}

private val Dp.dp get() = value.dp
private val Sp.sp get() = value.sp
