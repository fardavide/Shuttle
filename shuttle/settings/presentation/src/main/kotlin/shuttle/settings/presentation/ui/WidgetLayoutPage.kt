package shuttle.settings.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.compose.getViewModel
import shuttle.design.NavHost
import shuttle.design.composable
import shuttle.design.ui.LoadingSpinner
import shuttle.design.ui.TextError
import shuttle.design.util.collectAsStateLifecycleAware
import shuttle.settings.presentation.WidgetLayout
import shuttle.settings.presentation.navigate
import shuttle.settings.presentation.viewmodel.WidgetLayoutViewModel
import shuttle.settings.presentation.viewmodel.WidgetLayoutViewModel.Action
import shuttle.settings.presentation.viewmodel.WidgetLayoutViewModel.State

@Composable
fun WidgetLayoutPage(onBack: () -> Unit) {
    val navController = rememberNavController()
    val onWidgetLayoutBack = { navController.popOrBack(onBack) }
    val viewModel: WidgetLayoutViewModel = getViewModel()
    val state = viewModel.state.collectAsStateLifecycleAware()
    val titleState = remember { mutableStateOf(WidgetLayout.Home.title) }

    WidgetLayoutContainer(title = titleState.value, state = state.value, onBack = onWidgetLayoutBack) {
        when (val s = state.value) {
            State.Loading -> LoadingSpinner()
            is State.Error -> TextError(text = s.message)
            is State.Data -> NavHost(navController = navController, startDestination = WidgetLayout.Home) {
                composable(WidgetLayout.Home) {
                    HomeWidgetLayoutRoute(
                        state = s,
                        toGrid = { navController.navigate(WidgetLayout.Grid, titleState) },
                        onIconSizeUpdated = { viewModel.submit(Action.UpdateIconsSize(it)) },
                        onHorizontalSpacingUpdated = { viewModel.submit(Action.UpdateHorizontalSpacing(it)) },
                        onVerticalSpacingUpdated = { viewModel.submit(Action.UpdateVerticalSpacing(it)) },
                        onTextSizeUpdated = { viewModel.submit(Action.UpdateTextSize(it)) }
                    ) { viewModel.submit(Action.UpdateAllowTwoLines(it)) }
                }
                composable(WidgetLayout.Grid) {
                    WidgetGridRoute(
                        state = s,
                        onRowsUpdated = { viewModel.submit(Action.UpdateRows(it)) },
                        onColumnsUpdated = { viewModel.submit(Action.UpdateColumns(it)) })
                }
            }
        }
    }
}

@Composable
private fun HomeWidgetLayoutRoute(
    state: State.Data,
    toGrid: () -> Unit,
    onIconSizeUpdated: (Int) -> Unit,
    onHorizontalSpacingUpdated: (Int) -> Unit,
    onVerticalSpacingUpdated: (Int) -> Unit,
    onTextSizeUpdated: (Int) -> Unit,
    onAllowTwoLinesUpdated: (Boolean) -> Unit
) = HomeWidgetLayoutContent(
    settings = state.widgetSettingsUiModel,
    toGrid = toGrid,
    onIconSizeUpdated = onIconSizeUpdated,
    onHorizontalSpacingUpdated = onHorizontalSpacingUpdated,
    onVerticalSpacingUpdated = onVerticalSpacingUpdated,
    onTextSizeUpdated = onTextSizeUpdated,
    onAllowTwoLinesUpdated = onAllowTwoLinesUpdated
)

@Composable
private fun WidgetGridRoute(
    state: State.Data,
    onRowsUpdated: (Int) -> Unit,
    onColumnsUpdated: (Int) -> Unit
) {
    WidgetGridContent(
        settings = state.widgetSettingsUiModel,
        onRowsUpdated = onRowsUpdated,
        onColumnsUpdated = onColumnsUpdated
    )
}

private fun NavController.popOrBack(onBack: () -> Unit) {
    if (popBackStack().not()) onBack()
}

