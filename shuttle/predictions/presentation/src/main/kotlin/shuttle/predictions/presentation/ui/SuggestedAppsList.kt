@file:Suppress("UnnecessaryVariable")

package shuttle.predictions.presentation.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.rememberImagePainter
import org.koin.androidx.compose.getViewModel
import shuttle.apps.domain.model.AppId
import shuttle.apps.presentation.model.AppUiModel
import shuttle.apps.presentation.resource.AppsStrings
import shuttle.design.Dimens
import shuttle.predictions.presentation.resources.Strings
import shuttle.predictions.presentation.viewmodel.SuggestedAppsListViewModel
import shuttle.predictions.presentation.viewmodel.SuggestedAppsListViewModel.Action
import shuttle.predictions.presentation.viewmodel.SuggestedAppsListViewModel.State

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SuggestedAppsListPage(
    onSettings: () -> Unit
) {
    val viewModel: SuggestedAppsListViewModel = getViewModel()
    val s by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text(Strings.ShuttleTitle) },
                actions = {
                    IconButton(onClick = onSettings) {
                        Icon(Icons.Filled.Settings, contentDescription = Strings.SettingsIconContentDescription)
                    }
                }
            )
        }, content = {

            when (val state = s) {
                State.Loading -> {}
                is State.Data -> SuggestedAppsList(state.apps) { viewModel.submit(Action.OnAppClicked(it)) }
                is State.Error -> TODO()
                is State.RequestOpenApp -> LocalContext.current.startActivity(state.intent)
            }
        }
    )
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
internal fun SuggestedAppsList(
    apps: List<AppUiModel>,
    onAppClicked: (AppId) -> Unit
) {
    val minCellSize = Dimens.Icon.Large + Dimens.Margin.Large
    LazyVerticalGrid(
        cells = GridCells.Adaptive(minCellSize),
        modifier = Modifier.padding(Dimens.Margin.Small)
    ) {
        items(apps, key = { app -> app.id.value }) {
            AppIconItem(it, onAppClicked)
        }
    }
}

@Composable
internal fun AppIconItem(
    app: AppUiModel,
    onAppClicked: (AppId) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(Dimens.Margin.Small)
            .fillMaxWidth()
            .clickable { onAppClicked(app.id) }
    ) {
        Image(
            painter = rememberImagePainter(data = app.icon),
            contentDescription = AppsStrings.AppIconContentDescription,
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
