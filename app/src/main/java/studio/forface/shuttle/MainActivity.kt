package studio.forface.shuttle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import shuttle.predictions.presentation.ui.LocationPermissionsScreen
import shuttle.predictions.presentation.ui.SuggestedAppsListPage
import studio.forface.shuttle.Destination.LocationPermissions
import studio.forface.shuttle.Destination.Suggestions
import studio.forface.shuttle.ui.theme.ShuttleTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShuttleTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    App()
                }
            }
        }
    }
}

@Composable
fun App() {
    var currentScreen by rememberSaveable {
        mutableStateOf(LocationPermissions)
    }
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = currentScreen) {
        composable(LocationPermissions) { LocationPermissionsScreen { currentScreen = Suggestions } }
        composable(Suggestions) { SuggestedAppsListPage() }
    }
}
