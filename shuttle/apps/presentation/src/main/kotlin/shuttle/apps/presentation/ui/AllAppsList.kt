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
import androidx.compose.ui.res.stringResource
import coil.compose.rememberAsyncImagePainter
import kotlinx.collections.immutable.ImmutableList
import org.koin.androidx.compose.getViewModel
import shuttle.apps.presentation.model.AppUiModel
import shuttle.apps.presentation.viewmodel.AllAppsListViewModel
import shuttle.design.theme.Dimens
import shuttle.design.ui.TextError
import shuttle.resources.R.string

@Composable
fun AllAppsListPage() {
    val viewModel: AllAppsListViewModel = getViewModel()

    val s by viewModel.state.collectAsState()
    when (val state = s) {
        AllAppsListViewModel.State.Loading -> {}
        is AllAppsListViewModel.State.Data -> AllAppsList(state.apps)
        is AllAppsListViewModel.State.Error -> TextError(text = state.message)
    }
}

@Composable
internal fun AllAppsList(apps: ImmutableList<AppUiModel>) {
    LazyColumn(contentPadding = PaddingValues(Dimens.Margin.large)) {
        items(apps) {
            AppListItem(it)
        }
    }
}

@Composable
internal fun AppListItem(app: AppUiModel) {
    Row(modifier = Modifier.padding(vertical = Dimens.Margin.medium)) {
        Image(
            painter = rememberAsyncImagePainter(model = app.icon),
            contentDescription = stringResource(id = string.app_icon_description),
            modifier = Modifier.size(Dimens.Icon.Large)
        )
        Spacer(modifier = Modifier.width(Dimens.Margin.large))
        Text(
            text = app.name,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}
