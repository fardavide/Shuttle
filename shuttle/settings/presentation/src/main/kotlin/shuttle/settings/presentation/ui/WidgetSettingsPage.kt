package shuttle.settings.presentation.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import org.koin.androidx.compose.getViewModel
import shuttle.design.ui.LoadingSpinner
import shuttle.design.ui.TextError
import shuttle.design.util.collectAsStateLifecycleAware
import shuttle.settings.presentation.resources.Strings
import shuttle.settings.presentation.viewmodel.WidgetSettingsViewModel
import shuttle.settings.presentation.viewmodel.WidgetSettingsViewModel.State

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun WidgetSettingsPage() {
    Scaffold(topBar = { SmallTopAppBar(title = { Text(Strings.WidgetSettingsTitle) }) }) {
        WidgetSettingsContent()
    }
}

@Composable
fun WidgetSettingsContent() {
    val viewModel: WidgetSettingsViewModel = getViewModel()

    val s by viewModel.state.collectAsStateLifecycleAware()
    when (val state = s) {
        State.Loading -> LoadingSpinner()
        is State.Data -> {}
        is State.Error -> TextError(text = state.message)
    }
}
