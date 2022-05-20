package shuttle.settings.presentation

import androidx.annotation.StringRes
import androidx.compose.runtime.MutableState
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import shuttle.design.Destination
import studio.forface.shuttle.design.R

object WidgetLayout : Destination("widget layout") {

    internal object Home : WidgetLayoutDestination(id = "home", title = R.string.settings_widget_layout_title)
    internal object Grid : WidgetLayoutDestination(id = "grid", title = R.string.settings_widget_layout_grid)
}

internal fun NavController.navigate(
    destination: WidgetLayoutDestination,
    titleState: MutableState<Int>,
    navOptions: NavOptions? = null
) {
    if (destination.id != currentDestination?.route) {
        navigate(destination.id, navOptions)
        titleState.value = destination.title
    }
}

internal open class WidgetLayoutDestination(
    id: String,
    @StringRes val title: Int
) : Destination("${WidgetLayout.id}/$id")

