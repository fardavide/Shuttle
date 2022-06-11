package studio.forface.shuttle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.compose.rememberNavController
import shuttle.design.Destination
import shuttle.design.NavHost
import shuttle.design.composable
import shuttle.design.navigate
import shuttle.design.theme.ShuttleTheme
import shuttle.onboarding.presentation.ui.OnboardingPage
import shuttle.permissions.presentation.ui.PermissionsPage
import shuttle.predictions.presentation.ui.SuggestedAppsListPage
import shuttle.settings.presentation.WidgetLayout
import shuttle.settings.presentation.ui.page.AboutPage
import shuttle.settings.presentation.ui.page.BlacklistSettingsPage
import shuttle.settings.presentation.ui.page.IconPackSettingsPage
import shuttle.settings.presentation.ui.page.SettingsPage
import shuttle.settings.presentation.ui.page.WidgetLayoutPage

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            ShuttleTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    App(onFinish = this::finish)
                }
            }
        }
    }
}

@Composable
internal fun App(onFinish: () -> Unit) {
    val navController = rememberNavController()
    val onBack = { navController.popOrFinish(onFinish) }

    NavHost(navController = navController, startDestination = Onboarding) {
        composable(About) { AboutRoute(onBack = onBack) }
        composable(BlacklistSettings) { BlacklistSettingsRoute(onBack = onBack) }
        composable(IconPackSettings) { IconPackSettingsRoute(onBack = onBack) }
        composable(Onboarding) { OnboardingRoute(navController) }
        composable(Permissions) { PermissionsRoute(navController) }
        composable(Settings) { SettingsRoute(navController, onBack = onBack) }
        composable(Suggestions) { SuggestionsRoute(navController) }
        composable(WidgetLayout) { WidgetLayoutRoute(onBack = onBack) }
    }
}

@Composable
private fun AboutRoute(onBack: () -> Unit) =
    AboutPage(onBack = onBack)

@Composable
private fun BlacklistSettingsRoute(onBack: () -> Unit) =
    BlacklistSettingsPage(onBack = onBack)

@Composable
private fun IconPackSettingsRoute(onBack: () -> Unit) =
    IconPackSettingsPage(onBack = onBack)

@Composable
private fun OnboardingRoute(navController: NavController) =
    OnboardingPage(
        actions = OnboardingPage.Actions(
            onOnboardingComplete = { navController.navigate(Permissions, pop(Onboarding)) }
        )
    )

@Composable
private fun PermissionsRoute(navController: NavController) =
    PermissionsPage(toSettings = { navController.navigate(Settings, pop(Permissions)) })

@Composable
private fun SettingsRoute(navController: NavController, onBack: () -> Unit) =
    SettingsPage(
        onBack = onBack,
        toBlacklist = { navController.navigate(BlacklistSettings) },
        toWidgetLayout = { navController.navigate(WidgetLayout) },
        toIconPacks = { navController.navigate(IconPackSettings) },
        toPermissions = { navController.navigate(Permissions) },
        toAbout = { navController.navigate(About) }
    )

@Composable
private fun SuggestionsRoute(navController: NavController) =
    SuggestedAppsListPage(onSettings = { navController.navigate(Settings) })

@Composable
private fun WidgetLayoutRoute(onBack: () -> Unit) =
    WidgetLayoutPage(onBack = onBack)

private fun pop(destination: Destination) = NavOptions.Builder()
    .setPopUpTo(destination.id, inclusive = true)
    .build()

private fun NavController.popOrFinish(onFinish: () -> Unit) {
    if (popBackStack().not()) onFinish()
}
