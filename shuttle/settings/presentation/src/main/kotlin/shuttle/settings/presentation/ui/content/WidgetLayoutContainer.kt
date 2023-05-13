package shuttle.settings.presentation.ui.content

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import shuttle.design.model.WidgetPreviewUiModel
import shuttle.design.theme.Dimens
import shuttle.design.ui.BackIconButton
import shuttle.design.ui.BottomSheetScaffold
import shuttle.design.ui.LoadingSpinner
import shuttle.design.ui.WidgetPreview
import shuttle.resources.R.drawable
import shuttle.resources.R.string
import shuttle.settings.presentation.viewmodel.WidgetLayoutViewModel.State

@Composable
internal fun WidgetLayoutContainer(
    @StringRes title: Int,
    state: State,
    onBack: () -> Unit,
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current
    var previewHeight by remember { mutableStateOf(Dimens.WidgetLayoutPreviewHeight) }
    val draggableState = rememberDraggableState { pixelsDelta ->
        val dpDelta = with(density) { pixelsDelta.toDp() }
        previewHeight += dpDelta
    }
    val backgroundColor = if (state is State.Data && state.layout.useMaterialColors.not()) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.background
    }
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(color = backgroundColor)
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
private fun TopBar(
    @StringRes title: Int,
    onBack: () -> Unit,
    draggableState: DraggableState
) {
    Column {
        Icon(
            modifier = Modifier
                .fillMaxWidth()
                .draggable(orientation = Orientation.Vertical, state = draggableState),
            painter = painterResource(id = drawable.ic_vertical_drag),
            contentDescription = stringResource(id = string.settings_widget_layout_toolbar_drag_description)
        )
        TopAppBar(
            title = { Text(stringResource(id = title)) },
            navigationIcon = { BackIconButton(onBack) },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
            windowInsets = WindowInsets(0, 0, 0, 0)
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
                WidgetPreviewUiModel(
                    apps = state.previewApps,
                    layout = state.layout
                )
            )
        }
    }
}
