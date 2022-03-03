package studio.forface.shuttle

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

internal enum class Destination(val id: String) {

    LocationPermissions("location permissions"),
    Settings("settings"),
    Suggestions("suggestions")
}

internal fun NavController.navigate(destination: Destination, navOptions: NavOptions? = null) {
    if (destination.id != currentDestination?.route) {
        navigate(destination.id, navOptions)
    }
}

internal fun NavGraphBuilder.composable(
    destination: Destination,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable (NavBackStackEntry) -> Unit
) = composable(
    route = destination.id,
    arguments = arguments,
    deepLinks = deepLinks,
    content = content
)

@Composable
internal fun NavHost(
    navController: NavHostController,
    startDestination: Destination,
    modifier: Modifier = Modifier,
    route: String? = null,
    builder: NavGraphBuilder.() -> Unit
) = NavHost(
    navController = navController,
    startDestination = startDestination.id,
    modifier = modifier,
    route = route,
    builder = builder
)
