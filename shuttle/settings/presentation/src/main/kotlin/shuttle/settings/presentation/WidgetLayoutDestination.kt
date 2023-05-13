package shuttle.settings.presentation

import androidx.annotation.StringRes
import shuttle.design.Destination
import shuttle.resources.R

object WidgetLayout : Destination("widget layout") {

    internal object Home : WidgetLayoutDestination(
        id = "home",
        title = R.string.settings_widget_layout_title
    )

    internal object Grid : WidgetLayoutDestination(
        id = "grid",
        title = R.string.settings_widget_layout_grid
    )

    internal object IconsDimensions : WidgetLayoutDestination(
        id = "icons dimensions",
        title = R.string.settings_widget_layout_icons_dimensions
    )

    internal object AppsLabels : WidgetLayoutDestination(
        id = "apps labels",
        title = R.string.settings_widget_layout_apps_labels
    )

    internal object Colors : WidgetLayoutDestination(
        id = "colors",
        title = R.string.settings_widget_layout_colors
    )
}

internal open class WidgetLayoutDestination(
    id: String,
    @StringRes val title: Int
) : Destination("${WidgetLayout.id}/$id")

