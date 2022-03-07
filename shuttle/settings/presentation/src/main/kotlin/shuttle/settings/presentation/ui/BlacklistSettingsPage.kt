@file:Suppress("UnnecessaryVariable")

package shuttle.settings.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import coil.compose.rememberImagePainter
import org.koin.androidx.compose.getViewModel
import shuttle.apps.domain.model.AppId
import shuttle.design.theme.Dimens
import shuttle.design.ui.LoadingSpinner
import shuttle.design.ui.TextError
import shuttle.design.util.collectAsStateLifecycleAware
import shuttle.settings.presentation.model.AppBlacklistSettingUiModel
import shuttle.settings.presentation.resources.Strings
import shuttle.settings.presentation.viewmodel.BlacklistSettingsViewModel
import shuttle.settings.presentation.viewmodel.BlacklistSettingsViewModel.Action
import shuttle.settings.presentation.viewmodel.BlacklistSettingsViewModel.State

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun BlacklistSettingsPage() {
    Scaffold(topBar = { SmallTopAppBar(title = { Text(Strings.Blacklist.Title) }) }) {
        BlacklistSettingsContent()
    }
}

@Composable
fun BlacklistSettingsContent() {
    val viewModel: BlacklistSettingsViewModel = getViewModel()

    val s by viewModel.state.collectAsStateLifecycleAware()
    when (val state = s) {
        State.Loading -> LoadingSpinner()
        is State.Data -> AllAppsList(
            state.apps,
            onAddToBlacklist = { viewModel.submit(Action.AddToBlacklist(it)) },
            onRemoveFromBlacklist = { viewModel.submit(Action.RemoveFromBlacklist(it)) })
        is State.Error -> TextError(text = state.message)
    }
}

@Composable
internal fun AllAppsList(
    apps: List<AppBlacklistSettingUiModel>,
    onAddToBlacklist: (AppId) -> Unit,
    onRemoveFromBlacklist: (AppId) -> Unit
) {
    LazyColumn(contentPadding = PaddingValues(Dimens.Margin.Small)) {
        items(apps) {
            AppListItem(
                app = it,
                onAddToBlacklist = onAddToBlacklist,
                onRemoveFromBlacklist = onRemoveFromBlacklist
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun AppListItem(
    app: AppBlacklistSettingUiModel,
    onAddToBlacklist: (AppId) -> Unit,
    onRemoveFromBlacklist: (AppId) -> Unit
) {
    val toggleAction = { isChecked: Boolean ->
        if (isChecked) onAddToBlacklist(app.id)
        else onRemoveFromBlacklist(app.id)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = Dimens.Margin.XXSmall, horizontal = Dimens.Margin.Small)
    ) {
        Image(
            painter = rememberImagePainter(data = app.icon),
            contentDescription = Strings.AppIconContentDescription,
            modifier = Modifier.size(Dimens.Icon.Medium)
        )
        Spacer(modifier = Modifier.width(Dimens.Margin.Medium))
        Text(
            text = app.name,
            style = MaterialTheme.typography.bodyMedium
        )
        Column(horizontalAlignment = Alignment.End, modifier = Modifier.fillMaxWidth()) {
            Checkbox(checked = app.isBlacklisted, onCheckedChange = toggleAction)
        }
    }
}
