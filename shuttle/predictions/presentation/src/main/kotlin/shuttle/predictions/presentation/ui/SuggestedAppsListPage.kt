@file:Suppress("UnnecessaryVariable")

package shuttle.predictions.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.rememberImagePainter
import kotlinx.collections.immutable.ImmutableList
import org.koin.androidx.compose.getViewModel
import shuttle.apps.domain.model.AppId
import shuttle.design.theme.Dimens
import shuttle.design.ui.LoadingSpinner
import shuttle.design.ui.TextError
import shuttle.design.util.collectAsStateLifecycleAware
import shuttle.predictions.presentation.model.AppUiModel
import shuttle.predictions.presentation.viewmodel.SuggestedAppsListViewModel
import shuttle.predictions.presentation.viewmodel.SuggestedAppsListViewModel.Action
import shuttle.predictions.presentation.viewmodel.SuggestedAppsListViewModel.State
import studio.forface.shuttle.design.R

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SuggestedAppsListPage(onSettings: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.app_name)) },
                actions = {
                    IconButton(onClick = onSettings) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = stringResource(id = R.string.settings_icon_description)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            SuggestedAppsListContent()
        }
    }
}

@Composable
fun SuggestedAppsListContent() {
    val viewModel: SuggestedAppsListViewModel = getViewModel()
    val s by viewModel.state.collectAsStateLifecycleAware()

    when (val state = s) {
        State.Loading -> LoadingSpinner()
        is State.Data -> SuggestedAppsList(state.apps) { viewModel.submit(Action.OnAppClicked(it)) }
        is State.Error -> TextError(text = stringResource(id = state.message))
        is State.RequestOpenApp -> LocalContext.current.startActivity(state.intent)
    }
}

@Composable
private fun SuggestedAppsList(apps: ImmutableList<AppUiModel>, onAppClicked: (AppId) -> Unit) {
    val minCellSize = Dimens.Icon.Large + Dimens.Margin.Large
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minCellSize),
        modifier = Modifier.padding(Dimens.Margin.Small)
    ) {
        items(apps) {
            AppIconItem(it, onAppClicked)
        }
    }
}

@Composable
private fun AppIconItem(app: AppUiModel, onAppClicked: (AppId) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(Dimens.Margin.Small)
            .fillMaxWidth()
            .clickable { onAppClicked(app.id) }
    ) {
        Image(
            painter = rememberImagePainter(data = app.icon),
            contentDescription = stringResource(id = R.string.x_app_icon_description),
            modifier = Modifier.size(Dimens.Icon.Large)
        )
        Spacer(modifier = Modifier.height(Dimens.Margin.Small))
        Text(
            text = app.name,
            maxLines = 1,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
