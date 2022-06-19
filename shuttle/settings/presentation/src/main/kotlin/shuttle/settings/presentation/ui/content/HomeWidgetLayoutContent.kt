package shuttle.settings.presentation.ui.content

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import shuttle.design.PreviewUtils
import shuttle.design.model.TextRes
import shuttle.design.ui.CheckableListItem
import shuttle.design.ui.NavigableListItem
import shuttle.settings.presentation.WidgetLayout
import studio.forface.shuttle.design.R.drawable
import studio.forface.shuttle.design.R.string

@Composable
internal fun HomeWidgetLayoutContent(
    showRefreshLocation: Boolean,
    actions: HomeWidgetLayoutContent.Actions
) {
    LazyColumn {
        item {
            NavigableListItem(
                title = WidgetLayout.Grid.title,
                icon = drawable.ic_grid,
                onClick = actions.toGrid
            )
        }
        item {
            NavigableListItem(
                title = WidgetLayout.IconsDimensions.title,
                icon = drawable.ic_dimensions,
                onClick = actions.toIconsDimensions
            )
        }
        item {
            NavigableListItem(
                title = WidgetLayout.AppsLabels.title,
                icon = drawable.ic_label,
                onClick = actions.toAppsLabels
            )
        }
        item {
            CheckableListItem(
                title = TextRes(string.settings_widget_layout_show_refresh_location),
                icon = painterResource(id = drawable.ic_refresh),
                contentDescription = TextRes(string.settings_widget_layout_show_refresh_location),
                isChecked = showRefreshLocation,
                onCheckChange = actions.onShowRefreshLocationChange
            )
        }
    }
}

object HomeWidgetLayoutContent {

    data class Actions(
        val toGrid: () -> Unit,
        val toIconsDimensions: () -> Unit,
        val toAppsLabels: () -> Unit,
        val onShowRefreshLocationChange: (Boolean) -> Unit
    )
}

@Composable
@Preview(
    showBackground = true,
    widthDp = PreviewUtils.Dimens.Medium.Width,
    heightDp = PreviewUtils.Dimens.Medium.Height
)
private fun HomeWidgetLayoutContentPreview() {
    MaterialTheme {
        HomeWidgetLayoutContent(
            showRefreshLocation = true,
            HomeWidgetLayoutContent.Actions(
                toGrid = {},
                toIconsDimensions = {},
                toAppsLabels = {},
                onShowRefreshLocationChange = {}
            )
        )
    }
}
