@file:Suppress("UnnecessaryVariable")

package shuttle.settings.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import coil.compose.rememberImagePainter
import org.koin.androidx.compose.getViewModel
import shuttle.apps.domain.model.AppId
import shuttle.design.theme.Dimens
import shuttle.design.ui.BackIconButton
import shuttle.design.ui.LoadingSpinner
import shuttle.design.ui.TextError
import shuttle.design.util.collectAsStateLifecycleAware
import shuttle.settings.presentation.model.AppBlacklistSettingUiModel
import shuttle.settings.presentation.viewmodel.BlacklistSettingsViewModel
import shuttle.settings.presentation.viewmodel.BlacklistSettingsViewModel.Action
import shuttle.settings.presentation.viewmodel.BlacklistSettingsViewModel.State
import studio.forface.shuttle.design.R

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun BlacklistSettingsPage(onBack: () -> Unit) {
    Scaffold(topBar = {
        SmallTopAppBar(
            title = { Text(stringResource(id = R.string.settings_blacklist_title)) },
            navigationIcon = { BackIconButton(onBack) }
        )
    }) {
        BlacklistSettingsContent()
    }
}

@Composable
private fun BlacklistSettingsContent() {
    val viewModel: BlacklistSettingsViewModel = getViewModel()

    val s by viewModel.state.collectAsStateLifecycleAware()
    when (val state = s) {
        State.Loading -> LoadingSpinner()
        is State.Data -> IconPackItemsList(
            state.apps,
            onAddToBlacklist = { viewModel.submit(Action.AddToBlacklist(it)) },
            onRemoveFromBlacklist = { viewModel.submit(Action.RemoveFromBlacklist(it)) })
        is State.Error -> TextError(text = state.message)
    }
}

@Composable
private fun IconPackItemsList(
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
private fun AppListItem(
    app: AppBlacklistSettingUiModel,
    onAddToBlacklist: (AppId) -> Unit,
    onRemoveFromBlacklist: (AppId) -> Unit
) {
    var checkedState by remember { mutableStateOf(app.isBlacklisted) }
    val toggleAction = { isChecked: Boolean ->
        checkedState = isChecked
        if (isChecked) onAddToBlacklist(app.id)
        else onRemoveFromBlacklist(app.id)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(vertical = Dimens.Margin.XXSmall, horizontal = Dimens.Margin.Small)
            .clickable { toggleAction(checkedState.not()) }
    ) {
        Image(
            painter = rememberImagePainter(data = app.icon),
            contentDescription = stringResource(id = R.string.x_app_icon_description),
            modifier = Modifier.size(Dimens.Icon.Medium)
        )
        Spacer(modifier = Modifier.width(Dimens.Margin.Medium))
        Text(
            text = app.name,
            style = MaterialTheme.typography.bodyMedium
        )
        Column(horizontalAlignment = Alignment.End, modifier = Modifier.fillMaxWidth()) {
            Checkbox(checked = checkedState, onCheckedChange = toggleAction)
        }
    }
}
