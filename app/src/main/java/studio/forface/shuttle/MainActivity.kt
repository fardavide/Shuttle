package studio.forface.shuttle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.compose.rememberNavController
import shuttle.design.theme.ShuttleTheme
import shuttle.permissions.ui.PermissionsPage
import shuttle.predictions.presentation.ui.SuggestedAppsListPage
import shuttle.settings.presentation.ui.BlacklistSettingsPage
import shuttle.settings.presentation.ui.IconPackSettingsPage
import shuttle.settings.presentation.ui.SettingsPage
import shuttle.settings.presentation.ui.WidgetSettingsPage
import studio.forface.shuttle.Destination.BlacklistSettings
import studio.forface.shuttle.Destination.IconPackSettings
import studio.forface.shuttle.Destination.Permissions
import studio.forface.shuttle.Destination.Settings
import studio.forface.shuttle.Destination.Suggestions
import studio.forface.shuttle.Destination.WidgetSettings

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShuttleTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    App()
                }
            }
        }
    }
}

@Composable
private fun App() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Permissions) {
        composable(BlacklistSettings) { BlacklistSettingsRoute() }
        composable(IconPackSettings) { IconPackSettingsRoute() }
        composable(Permissions) { PermissionsRoute(navController) }
        composable(Settings) { SettingsRoute(navController) }
        composable(Suggestions) { SuggestionsRoute(navController) }
        composable(WidgetSettings) { WidgetSettingsRoute() }
    }
}

@Composable
private fun BlacklistSettingsRoute() =
    BlacklistSettingsPage()

@Composable
private fun IconPackSettingsRoute() =
    IconPackSettingsPage()

@Composable
private fun PermissionsRoute(navController: NavController) =
    PermissionsPage(onAllPermissionsGranted = { navController.navigate(Settings, PopAll) })

@Composable
private fun SettingsRoute(navController: NavController) =
    SettingsPage(
        toBlacklist = { navController.navigate(BlacklistSettings) },
        toWidgetSettings = { navController.navigate(WidgetSettings) },
        toIconPacks = { navController.navigate(IconPackSettings) }
    )

@Composable
private fun SuggestionsRoute(navController: NavController) =
    SuggestedAppsListPage(onSettings = { navController.navigate(Settings) })

@Composable
private fun WidgetSettingsRoute() =
    WidgetSettingsPage()

private val PopAll: NavOptions = NavOptions.Builder()
    .setPopUpTo(Permissions.id, inclusive = true)
    .build()
