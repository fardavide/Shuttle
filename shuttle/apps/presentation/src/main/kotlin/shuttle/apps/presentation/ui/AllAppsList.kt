@file:Suppress("UnnecessaryVariable")

package shuttle.apps.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import coil.compose.rememberImagePainter
import org.koin.androidx.compose.getViewModel
import shuttle.apps.presentation.model.AppUiModel
import shuttle.apps.presentation.resource.AppsStrings
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
            AppListItem(it)
        }
    }
}

@Composable
internal fun AppListItem(
    app: AppUiModel
) {
    Row(modifier = Modifier.padding(vertical = Dimens.Margin.Medium)) {
        Image(
            painter = rememberImagePainter(data = app.icon),
            contentDescription = AppsStrings.AppIconContentDescription,
            modifier = Modifier.size(Dimens.Icon.Large)
        )
        Spacer(modifier = Modifier.width(Dimens.Margin.Large))
        Text(
            text = app.name,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}
