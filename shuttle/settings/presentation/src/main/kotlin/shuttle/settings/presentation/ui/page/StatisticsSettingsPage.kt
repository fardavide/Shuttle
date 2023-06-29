package shuttle.settings.presentation.ui.page

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.koin.androidx.compose.koinViewModel
import shuttle.design.ui.BackIconButton
import shuttle.design.ui.LoadingSpinner
import shuttle.design.util.collectAsStateLifecycleAware
import shuttle.resources.R.string
import shuttle.settings.presentation.action.StatisticsSettingsAction
import shuttle.settings.presentation.state.StatisticsSettingsState
import shuttle.settings.presentation.ui.component.SliderItem
import shuttle.settings.presentation.viewmodel.StatisticsSettingsViewModel

@Composable
fun StatisticsSettingsPage(onBack: () -> Unit) {
    Scaffold(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = string.settings_statistics_title)) },
                navigationIcon = { BackIconButton(onBack) }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            StatisticsSettingsPageContent()
        }
    }
}

@Composable
private fun StatisticsSettingsPageContent() {
    val viewModel: StatisticsSettingsViewModel = koinViewModel()

    val s by viewModel.state.collectAsStateLifecycleAware()
    when (val state = s) {
        StatisticsSettingsState.Loading -> LoadingSpinner()
        is StatisticsSettingsState.Data -> SliderItem(
            title = string.settings_statistics_title,
            values = state.uiModel.allSlideItemValues,
            value = state.uiModel.currentValueUiModel.sliderItemValue,
            onValueChange = { viewModel.submit(StatisticsSettingsAction.SetKeepStatisticsFor(it)) }
        )
    }
}
