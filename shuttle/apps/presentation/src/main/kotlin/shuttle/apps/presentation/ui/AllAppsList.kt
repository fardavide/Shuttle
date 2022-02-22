@file:Suppress("UnnecessaryVariable")

package shuttle.apps.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import org.koin.androidx.compose.getViewModel
import shuttle.apps.domain.model.AppModel
import shuttle.apps.presentation.model.AppUiModel
import shuttle.apps.presentation.resource.Strings
import shuttle.apps.presentation.viewmodel.AllAppsListViewModel
import shuttle.design.Dimens

@Composable
fun AllAppsListPage() {
    val viewModel: AllAppsListViewModel = getViewModel()

    val s by viewModel.state.collectAsState()
    when (val state = s) {
        AllAppsListViewModel.State.Loading -> {}
        is AllAppsListViewModel.State.Data -> AllAppsList(state.apps)
        is AllAppsListViewModel.State.Error -> TODO()
    }
}

@Composable
internal fun AllAppsList(
    apps: List<AppUiModel>
) {
    LazyColumn(contentPadding = PaddingValues(Dimens.Margin.Large)) {
        items(apps) {
            App(it)
        }
    }
}

@Composable
internal fun App(
    app: AppUiModel
) {
    Row(modifier = Modifier.padding(vertical = Dimens.Margin.Medium)) {
        Image(
            painter = rememberImagePainter(data = app.icon),
            contentDescription = Strings.AppIconContentDescription,
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
