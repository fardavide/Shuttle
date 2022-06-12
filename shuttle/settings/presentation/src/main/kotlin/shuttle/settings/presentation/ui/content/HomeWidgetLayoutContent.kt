package shuttle.settings.presentation.ui.content

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import shuttle.design.PreviewDimens
import shuttle.design.ui.NavigableListItem
import shuttle.settings.presentation.WidgetLayout
import studio.forface.shuttle.design.R

@Composable
internal fun HomeWidgetLayoutContent(
    toGrid: () -> Unit,
    toIconsDimensions: () -> Unit,
    toAppsLabels: () -> Unit
) {
    LazyColumn {
        item {
            NavigableListItem(
                title = WidgetLayout.Grid.title,
                icon = R.drawable.ic_grid,
                onClick = toGrid
            )
        }
        item {
            NavigableListItem(
                title = WidgetLayout.IconsDimensions.title,
                icon = R.drawable.ic_dimensions,
                onClick = toIconsDimensions
            )
        }
        item {
            NavigableListItem(
                title = WidgetLayout.AppsLabels.title,
                icon = R.drawable.ic_label,
                onClick = toAppsLabels
            )
        }
    }
}

@Composable
@Preview(showBackground = true, widthDp = PreviewDimens.Medium.Width, heightDp = PreviewDimens.Medium.Height)
private fun HomeWidgetLayoutContentPreview() {
    MaterialTheme {
        HomeWidgetLayoutContent({}, {}, {})
    }
}
