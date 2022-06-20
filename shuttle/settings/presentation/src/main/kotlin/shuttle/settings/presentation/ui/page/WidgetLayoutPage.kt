package shuttle.settings.presentation.ui.page

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.compose.getViewModel
import shuttle.design.NavHost
import shuttle.design.composable
import shuttle.design.navigate
import shuttle.design.ui.LoadingSpinner
import shuttle.design.ui.TextError
import shuttle.design.util.collectAsStateLifecycleAware
import shuttle.settings.presentation.WidgetLayout
import shuttle.settings.presentation.ui.content.HomeWidgetLayoutContent
import shuttle.settings.presentation.ui.content.WidgetAppsLabelsContent
import shuttle.settings.presentation.ui.content.WidgetGridContent
import shuttle.settings.presentation.ui.content.WidgetIconsDimensionsContent
import shuttle.settings.presentation.ui.content.WidgetLayoutContainer
import shuttle.settings.presentation.viewmodel.WidgetLayoutViewModel
import shuttle.settings.presentation.viewmodel.WidgetLayoutViewModel.Action
import shuttle.settings.presentation.viewmodel.WidgetLayoutViewModel.State

@Composable
fun WidgetLayoutPage(onBack: () -> Unit) {
    val navController = rememberNavController()
    val viewModel: WidgetLayoutViewModel = getViewModel()
    val state = viewModel.state.collectAsStateLifecycleAware()
    val titleState = remember { mutableStateOf(WidgetLayout.Home.title) }
    val onWidgetLayoutBack = { navController.popOrBack(onBack, titleState = titleState) }

    WidgetLayoutContainer(title = titleState.value, state = state.value, onBack = onWidgetLayoutBack) {
        when (val s = state.value) {
            State.Loading -> LoadingSpinner()
            is State.Error -> TextError(text = s.message)
            is State.Data -> {
                val args = Args(
                    state = s,
                    viewModel = viewModel,
                    navController = navController,
                    titleState = titleState
                )
                NavHost(navController = navController, startDestination = WidgetLayout.Home) {
                    composable(WidgetLayout.Home) { HomeWidgetLayoutRoute(args) }
                    composable(WidgetLayout.Grid) { WidgetGridRoute(args) }
                    composable(WidgetLayout.IconsDimensions) { WidgetIconsDimensionsRoute(args) }
                    composable(WidgetLayout.AppsLabels) { WidgetAppsLabelsRoute(args) }
                }
            }
        }
    }
}

@Composable
private fun HomeWidgetLayoutRoute(args: Args) = with(args) {
    LaunchedEffect(key1 = 0, block = { titleState.value = WidgetLayout.Home.title })
    HomeWidgetLayoutContent(
        HomeWidgetLayoutContent.Actions(
            toGrid = { navController.navigate(WidgetLayout.Grid) },
            toIconsDimensions = { navController.navigate(WidgetLayout.IconsDimensions) },
            toAppsLabels = { navController.navigate(WidgetLayout.AppsLabels) }
        )
    )
}

@Composable
private fun WidgetGridRoute(args: Args) = with(args) {
    LaunchedEffect(key1 = 0, block = {titleState.value = WidgetLayout.Grid.title})
    WidgetGridContent(
        settings = state.layout,
        onRowsUpdated = { viewModel.submit(Action.UpdateRows(it)) },
        onColumnsUpdated = { viewModel.submit(Action.UpdateColumns(it)) }
    )
}

@Composable
private fun WidgetIconsDimensionsRoute(args: Args) = with(args) {
    LaunchedEffect(key1 = 0, block = {titleState.value = WidgetLayout.IconsDimensions.title})
    WidgetIconsDimensionsContent(
        settings = state.layout,
        onIconSizeUpdated = { viewModel.submit(Action.UpdateIconsSize(it)) },
        onHorizontalSpacingUpdated = { viewModel.submit(Action.UpdateHorizontalSpacing(it)) },
        onVerticalSpacingUpdated = { viewModel.submit(Action.UpdateVerticalSpacing(it)) }
    )
}

@Composable
private fun WidgetAppsLabelsRoute(args: Args) = with(args) {
    LaunchedEffect(key1 = 0, block = {titleState.value = WidgetLayout.AppsLabels.title})
    WidgetAppsLabelsContent(
        settings = state.layout,
        onTextSizeUpdated = { viewModel.submit(Action.UpdateTextSize(it)) },
        onAllowTwoLinesUpdated = { viewModel.submit(Action.UpdateAllowTwoLines(it)) }
    )
}

private fun NavController.popOrBack(onBack: () -> Unit, titleState: MutableState<Int>) {
    if (popBackStack()) titleState.value = WidgetLayout.Home.title
    else onBack()
}

private class Args(
    val state: State.Data,
    val viewModel: WidgetLayoutViewModel,
    val navController: NavController,
    val titleState: MutableState<Int>
)
