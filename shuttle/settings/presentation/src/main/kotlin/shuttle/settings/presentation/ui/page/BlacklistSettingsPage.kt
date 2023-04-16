@file:Suppress("UnnecessaryVariable")

package shuttle.settings.presentation.ui.page

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.collections.immutable.ImmutableList
import org.koin.androidx.compose.getViewModel
import shuttle.apps.domain.model.AppId
import shuttle.design.PreviewUtils
import shuttle.design.R
import shuttle.design.model.TextRes
import shuttle.design.theme.Dimens
import shuttle.design.theme.ShuttleTheme
import shuttle.design.ui.BackIconButton
import shuttle.design.ui.CheckableListItem
import shuttle.design.ui.LoadingSpinner
import shuttle.design.ui.TextError
import shuttle.design.util.collectAsStateLifecycleAware
import shuttle.settings.presentation.model.AppBlacklistSettingUiModel
import shuttle.settings.presentation.viewmodel.BlacklistSettingsViewModel
import shuttle.settings.presentation.viewmodel.BlacklistSettingsViewModel.Action
import shuttle.settings.presentation.viewmodel.BlacklistSettingsViewModel.State

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun BlacklistSettingsPage(onBack: () -> Unit) {
    Scaffold(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.settings_blacklist_title)) },
                navigationIcon = { BackIconButton(onBack) }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            BlacklistSettingsContent()
        }
    }
}

@Composable
private fun BlacklistSettingsContent() {
    val viewModel: BlacklistSettingsViewModel = getViewModel()

    val s by viewModel.state.collectAsStateLifecycleAware()
    when (val state = s) {
        State.Loading -> LoadingSpinner()
        is State.Data -> Column {
            SearchBar(onSearch = { viewModel.submit(Action.Search(it)) })
            BlacklistItemsList(
                apps = state.apps,
                actions = AppListItem.Actions(
                    onAddToBlacklist = { viewModel.submit(Action.AddToBlacklist(it)) },
                    onRemoveFromBlacklist = { viewModel.submit(Action.RemoveFromBlacklist(it)) }
                )
            )
        }
        is State.Error -> TextError(text = state.message)
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun SearchBar(onSearch: (String) -> Unit) {
    var textFieldValue by remember { mutableStateOf(TextFieldValue()) }

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = Dimens.Margin.Large, end = Dimens.Margin.Large, bottom = Dimens.Margin.Small),
        placeholder = { Text(text = stringResource(id = R.string.settings_blacklist_search)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = ImeAction.Done
        ),
        value = textFieldValue,
        onValueChange = { newTextFieldValue ->
            textFieldValue = newTextFieldValue
            onSearch(newTextFieldValue.text)
        }
    )
}

@Composable
private fun BlacklistItemsList(apps: ImmutableList<AppBlacklistSettingUiModel>, actions: AppListItem.Actions) {
    LazyColumn(contentPadding = PaddingValues(Dimens.Margin.Small)) {
        items(apps) { model ->
            AppListItem(model, actions)
        }
    }
}

@Composable
private fun AppListItem(app: AppBlacklistSettingUiModel, actions: AppListItem.Actions) {
    val onCheckChange = { isChecked: Boolean ->
        if (isChecked) actions.onAddToBlacklist(app.id)
        else actions.onRemoveFromBlacklist(app.id)
    }
    CheckableListItem.LargeIcon(
        id = app.id,
        title = TextRes(app.name),
        iconDrawable = app.icon,
        contentDescription = TextRes(R.string.x_app_icon_description),
        isChecked = app.isBlacklisted,
        onCheckChange = onCheckChange
    )
}

private object AppListItem {
    
    data class Actions(
        val onAddToBlacklist: (AppId) -> Unit,
        val onRemoveFromBlacklist: (AppId) -> Unit
    ) {

        companion object {
            val Empty = Actions(onAddToBlacklist = {}, onRemoveFromBlacklist = {})
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun AppListItemPreview() {
    val app = AppBlacklistSettingUiModel(
        id = AppId("shuttle"),
        name = "Shuttle",
        icon = PreviewUtils.ShuttleIconDrawable,
        isBlacklisted = true
    )
    ShuttleTheme {
        AppListItem(app, AppListItem.Actions.Empty)
    }
}
