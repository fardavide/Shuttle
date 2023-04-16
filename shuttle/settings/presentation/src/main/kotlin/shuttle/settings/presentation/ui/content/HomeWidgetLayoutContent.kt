package shuttle.settings.presentation.ui.content

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import shuttle.design.PreviewUtils
import shuttle.design.R.drawable
import shuttle.design.ui.NavigableListItem
import shuttle.settings.presentation.WidgetLayout

@Composable
internal fun HomeWidgetLayoutContent(actions: HomeWidgetLayoutContent.Actions) {
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
            NavigableListItem(
                title = WidgetLayout.Colors.title,
                icon = drawable.ic_color_palette,
                onClick = actions.toColors
            )
        }
    }
}

object HomeWidgetLayoutContent {

    data class Actions(
        val toGrid: () -> Unit,
        val toIconsDimensions: () -> Unit,
        val toAppsLabels: () -> Unit,
        val toColors: () -> Unit
    ) {

        companion object {
            val Empty = Actions(
                toGrid = {},
                toIconsDimensions = {},
                toAppsLabels = {},
                toColors = {}
            )
        }
    }
}

@Composable
@Preview(
    showBackground = true,
    widthDp = PreviewUtils.Dimens.Medium.Width,
    heightDp = PreviewUtils.Dimens.Medium.Height
)
private fun HomeWidgetLayoutContentPreview() {
    MaterialTheme {
        HomeWidgetLayoutContent(HomeWidgetLayoutContent.Actions.Empty)
    }
}
