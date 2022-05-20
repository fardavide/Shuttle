package shuttle.settings.presentation.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import shuttle.design.PreviewDimens
import shuttle.settings.presentation.WidgetLayout
import shuttle.settings.presentation.ui.component.SectionItem
import studio.forface.shuttle.design.R

@Composable
internal fun HomeWidgetLayoutContent(
    toGrid: () -> Unit,
    toIconsDimensions: () -> Unit,
    toAppsLabels: () -> Unit
) {
    LazyColumn {
        item {
            SectionItem(
                title = WidgetLayout.Grid.title,
                icon = R.drawable.ic_grid,
                onClick = toGrid
            )
        }
        item {
            SectionItem(
                title = WidgetLayout.IconsDimensions.title,
                icon = R.drawable.ic_dimensions,
                onClick = toIconsDimensions
            )
        }
        item {
            SectionItem(
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
