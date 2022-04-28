package shuttle.settings.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import org.koin.androidx.compose.getViewModel
import shuttle.design.theme.Dimens
import shuttle.design.ui.BackIconButton
import shuttle.design.ui.LoadingSpinner
import shuttle.design.util.collectAsStateLifecycleAware
import shuttle.permissions.domain.model.backgroundPermissionsList
import shuttle.settings.presentation.model.SettingsItemUiModel
import shuttle.settings.presentation.model.SettingsSectionUiModel
import shuttle.settings.presentation.viewmodel.SettingsViewModel
import shuttle.settings.presentation.viewmodel.SettingsViewModel.Action
import shuttle.settings.presentation.viewmodel.SettingsViewModel.State
import studio.forface.shuttle.design.R

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
fun SettingsPage(
    onBack: () -> Unit,
    toBlacklist: () -> Unit,
    toWidgetLayout: () -> Unit,
    toIconPacks: () -> Unit,
    toPermissions: () -> Unit
) {
    val viewModel = getViewModel<SettingsViewModel>()
    val state by viewModel.state.collectAsStateLifecycleAware()

    val backgroundLocationPermissionsState = rememberMultiplePermissionsState(backgroundPermissionsList)
    viewModel.submit(Action.UpdatePermissionsState(backgroundLocationPermissionsState))

    Scaffold(topBar = {
        SmallTopAppBar(
            title = { Text(stringResource(id = R.string.settings_title)) },
            navigationIcon = { BackIconButton(onBack) }
        )
    }) {
        SettingsContent(
            state = state,
            toBlacklist = toBlacklist,
            toWidgetLayout = toWidgetLayout,
            toIconPacks = toIconPacks,
            toPermissions = toPermissions,
            updatePrioritizeLocation = { viewModel.submit(Action.UpdatePrioritizeLocation(it)) }
        )
    }
}

@Composable
private fun SettingsContent(
    state: State,
    toBlacklist: () -> Unit,
    toWidgetLayout: () -> Unit,
    toIconPacks: () -> Unit,
    toPermissions: () -> Unit,
    updatePrioritizeLocation: (Boolean) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxHeight()) {
        item { DesignSection() }
        item { WidgetLayoutItem(toWidgetLayout) }
        item { IconPackItem(toIconPacks) }

        item { SuggestionsSection() }
        item { BlacklistItem(toBlacklist) }
        item { PrioritizeLocationItem(state = state.prioritizeLocation, updatePrioritizeLocation) }

        item { PermissionsSection() }
        item { CheckPermissionsItem(state.permissions, toPermissions) }

        item { AppVersionFooter(version = state.appVersion) }
    }
}

@Composable
fun DesignSection() {
    val uiModel = SettingsSectionUiModel(
        title = stringResource(id = R.string.settings_design_section_title)
    )
    SettingsSection(item = uiModel)
}

@Composable
private fun WidgetLayoutItem(toWidgetLayout: () -> Unit) {
    val uiModel = SettingsItemUiModel(
        title = stringResource(id = R.string.settings_widget_layout_title),
        description = stringResource(id = R.string.settings_widget_layout_description)
    )
    SettingsItem(item = uiModel, onClick = toWidgetLayout)
}

@Composable
private fun IconPackItem(toIconPacks: () -> Unit) {
    val uiModel = SettingsItemUiModel(
        title = stringResource(id = R.string.settings_icon_pack_title),
        description = stringResource(id = R.string.settings_icon_pack_description)
    )
    SettingsItem(item = uiModel, onClick = toIconPacks)
}

@Composable
private fun SuggestionsSection() {
    val uiModel = SettingsSectionUiModel(
        title = stringResource(id = R.string.settings_suggestions_section_title)
    )
    SettingsSection(item = uiModel)
}

@Composable
private fun BlacklistItem(toBlacklist: () -> Unit) {
    val uiModel = SettingsItemUiModel(
        title = stringResource(id = R.string.settings_blacklist_title),
        description = stringResource(id = R.string.settings_blacklist_description)
    )
    SettingsItem(item = uiModel, onClick = toBlacklist)
}

@Composable
private fun PrioritizeLocationItem(
    state: State.PrioritizeLocation,
    updatePrioritizeLocation: (Boolean) -> Unit
) {
    var isPrioritizingLocation by remember { mutableStateOf(state == State.PrioritizeLocation.True) }
    
    val uiModel = SettingsItemUiModel(
        title = stringResource(id = R.string.settings_prioritize_location_title),
        description = stringResource(id = R.string.settings_prioritize_location_description)
    )
    SettingsItem(item = uiModel, onClick = { isPrioritizingLocation = !isPrioritizingLocation }) {
        when (state) {
            State.PrioritizeLocation.Loading -> LoadingSpinner()
            State.PrioritizeLocation.False, State.PrioritizeLocation.True -> {
                Switch(checked = isPrioritizingLocation, onCheckedChange = { isChecked ->
                    isPrioritizingLocation = isChecked
                    updatePrioritizeLocation(isChecked)
                })
            }
        }
    }
}

@Composable
private fun PermissionsSection() {
    val uiModel = SettingsSectionUiModel(
        title = stringResource(id = R.string.settings_permissions_section_title)
    )
    SettingsSection(item = uiModel)
}

@Composable
private fun CheckPermissionsItem(state: State.Permissions, toPermissions: () -> Unit) {
    val uiModel = SettingsItemUiModel(
        title = stringResource(id = R.string.settings_check_permissions_title),
        description = stringResource(id = R.string.settings_check_permissions_description)
    )
    SettingsItem(item = uiModel, onClick = toPermissions) {
        when (state) {
            State.Permissions.Loading -> LoadingSpinner()
            State.Permissions.Denied -> Icon(
                painter = rememberVectorPainter(image = Icons.Rounded.Warning),
                tint = MaterialTheme.colorScheme.error,
                contentDescription = stringResource(R.string.settings_check_permissions_not_granted_description),
                modifier = Modifier.padding(end = Dimens.Margin.Small).size(Dimens.Icon.Small)
            )
            State.Permissions.Granted -> Icon(
                painter = rememberVectorPainter(image = Icons.Rounded.CheckCircle),
                tint = MaterialTheme.colorScheme.secondary,
                contentDescription = stringResource(R.string.settings_check_permissions_granted_description),
                modifier = Modifier.padding(end = Dimens.Margin.Small).size(Dimens.Icon.Small)
            )
        }
    }
}

@Composable
private fun SettingsSection(item: SettingsSectionUiModel) {
    Row(modifier = Modifier.padding(horizontal = Dimens.Margin.Medium, vertical = Dimens.Margin.Small)) {
        Text(text = item.title, style = MaterialTheme.typography.displaySmall)
    }
}

@Composable
private fun SettingsItem(
    item: SettingsItemUiModel,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = Dimens.Margin.Medium, vertical = Dimens.Margin.Small)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = item.title, style = MaterialTheme.typography.titleMedium)
            Text(text = item.description, style = MaterialTheme.typography.bodySmall)
        }
        Row(
            modifier = Modifier.padding(start = Dimens.Margin.Small),
            horizontalArrangement = Arrangement.End,
            content = content
        )
    }
}

@Composable
private fun AppVersionFooter(version: String) {
    Box(
        modifier = Modifier.fillMaxWidth().padding(Dimens.Margin.Large),
        contentAlignment = Alignment.BottomCenter
    ) {
        Text(text = stringResource(id = R.string.settings_footer_app_version, version))
    }
}

@Composable
@Preview(showBackground = true)
fun SettingsContentPreview() {
    MaterialTheme {
        SettingsContent(
            state = State.Loading,
            toBlacklist = {},
            toWidgetLayout = {},
            toIconPacks = {},
            toPermissions = {},
            updatePrioritizeLocation = {}
        )
    }
}

@Composable
@Preview(showBackground = true)
fun SettingsItemPreview() {
    MaterialTheme {
        val uiModel = SettingsItemUiModel(
            title = stringResource(id = R.string.settings_blacklist_title),
            description = stringResource(id = R.string.settings_blacklist_description)
        )
        SettingsItem(item = uiModel, onClick = {})
    }
}
