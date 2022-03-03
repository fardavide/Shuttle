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
import shuttle.permissions.ui.LocationPermissionsPage
import shuttle.predictions.presentation.ui.SuggestedAppsListPage
import shuttle.settings.presentation.ui.BlacklistSettingsPage
import studio.forface.shuttle.Destination.AccessibilityPermissions
import studio.forface.shuttle.Destination.LocationPermissions
import studio.forface.shuttle.Destination.Settings
import studio.forface.shuttle.Destination.Suggestions

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
    NavHost(navController = navController, startDestination = LocationPermissions) {
        composable(AccessibilityPermissions) { AccessibilityPermissionsRoute(navController) }
        composable(LocationPermissions) { LocationPermissionsRoute(navController) }
        composable(Settings) { BlacklistSettingsRoute() }
        composable(Suggestions) { SuggestionsRoute(navController) }
    }
}

@Composable
private fun AccessibilityPermissionsRoute(navController: NavController): Unit =
    TODO()

@Composable
private fun LocationPermissionsRoute(navController: NavController) =
    LocationPermissionsPage { navController.navigate(Suggestions, PopAll) }

@Composable
private fun BlacklistSettingsRoute() =
    BlacklistSettingsPage()

@Composable
private fun SuggestionsRoute(navController: NavController) =
    SuggestedAppsListPage(onSettings = { navController.navigate(Settings) })

private val PopAll: NavOptions = NavOptions.Builder()
    .setPopUpTo(LocationPermissions.id, inclusive = true)
    .build()
