@file:Suppress("UnnecessaryVariable")

package shuttle.predictions.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberImagePainter
import org.koin.androidx.compose.getViewModel
import shuttle.apps.domain.model.AppId
import shuttle.apps.presentation.model.AppUiModel
import shuttle.apps.presentation.resource.AppsStrings
import shuttle.design.Dimens
import shuttle.predictions.presentation.viewmodel.SuggestedAppsListViewModel
import shuttle.predictions.presentation.viewmodel.SuggestedAppsListViewModel.Action
import shuttle.predictions.presentation.viewmodel.SuggestedAppsListViewModel.State

@Composable
fun SuggestedAppsListPage() {
    val viewModel: SuggestedAppsListViewModel = getViewModel()

    val s by viewModel.state.collectAsState()
    when (val state = s) {
        State.Loading -> {}
        is State.Data -> SuggestedAppsList(state.apps) { viewModel.submit(Action.OnAppClicked(it)) }
        is State.Error -> TODO()
        is State.RequestOpenApp -> LocalContext.current.startActivity(state.intent)
    }
}

@Composable
internal fun SuggestedAppsList(
    apps: List<AppUiModel>,
    onAppClicked: (AppId) -> Unit
) {
    LazyColumn(contentPadding = PaddingValues(Dimens.Margin.Large)) {
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
    Row(modifier = Modifier.padding(vertical = Dimens.Margin.Small).clickable { onAppClicked(app.id) }) {
        Image(
            painter = rememberImagePainter(data = app.icon),
            contentDescription = AppsStrings.AppIconContentDescription,
            modifier = Modifier.size(Dimens.Icon.Large)
        )
        Spacer(modifier = Modifier.width(Dimens.Margin.Large))
        Text(
            text = app.name,
            style = MaterialTheme.typography.h5,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}
