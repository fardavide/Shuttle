package shuttle.settings.presentation.ui.page

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import org.koin.androidx.compose.getViewModel
import shuttle.design.TestTag
import shuttle.design.theme.Dimens
import shuttle.design.theme.ShuttleTheme
import shuttle.design.ui.LoadingSpinner
import shuttle.design.util.ConsumableLaunchedEffect
import shuttle.design.util.Effect
import shuttle.design.util.collectAsStateLifecycleAware
import shuttle.permissions.domain.model.backgroundPermissionsList
import shuttle.resources.R.string
import shuttle.settings.presentation.model.SettingsItemUiModel
import shuttle.settings.presentation.model.SettingsSectionUiModel
import shuttle.settings.presentation.model.SettingsState
import shuttle.settings.presentation.viewmodel.SettingsViewModel
import shuttle.settings.presentation.viewmodel.SettingsViewModel.Action

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
fun SettingsPage(actions: SettingsPage.Actions) {
    val viewModel = getViewModel<SettingsViewModel>()
    val state by viewModel.state.collectAsStateLifecycleAware()

    ConsumableLaunchedEffect(effect = state.openOnboardingEffect) {
        actions.toOnboarding()
    }

    val backgroundLocationPermissionsState = rememberMultiplePermissionsState(backgroundPermissionsList)
    viewModel.submit(Action.UpdatePermissionsState(backgroundLocationPermissionsState))

    Scaffold(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding(),
        topBar = {
            TopAppBar(title = { Text(stringResource(id = string.settings_title)) })
        }
    ) { paddingValues ->
        SettingsContent(
            state = state,
            actions = actions,
            resetOnboardingShown = { viewModel.submit(Action.ResetOnboardingShown) },
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
private fun SettingsContent(
    state: SettingsState,
    actions: SettingsPage.Actions,
    resetOnboardingShown: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.fillMaxHeight().testTag(TestTag.Settings)) {
        item { CustomizeWidgetSection() }
        item { WidgetLayoutItem(actions.toWidgetLayout) }
        item { IconPackItem(actions.toIconPacks) }
        item { BlacklistItem(actions.toBlacklist) }

        item { InfoSection() }
        item { RestartOnboardingItem(resetOnboardingShown) }
        item { CheckPermissionsItem(state.permissions, actions.toPermissions) }
        item { AboutItem(actions.toAbout) }

        item { AppVersionFooter(version = state.appVersion) }
    }
}

@Composable
fun CustomizeWidgetSection() {
    val uiModel = SettingsSectionUiModel(
        title = stringResource(id = string.settings_customize_widget_section_title)
    )
    SettingsSection(item = uiModel)
}

@Composable
private fun WidgetLayoutItem(toWidgetLayout: () -> Unit) {
    val uiModel = SettingsItemUiModel(
        title = stringResource(id = string.settings_widget_layout_title),
        description = stringResource(id = string.settings_widget_layout_description)
    )
    SettingsItem(item = uiModel, onClick = toWidgetLayout)
}

@Composable
private fun IconPackItem(toIconPacks: () -> Unit) {
    val uiModel = SettingsItemUiModel(
        title = stringResource(id = string.settings_icon_pack_title),
        description = stringResource(id = string.settings_icon_pack_description)
    )
    SettingsItem(item = uiModel, onClick = toIconPacks)
}

@Composable
private fun BlacklistItem(toBlacklist: () -> Unit) {
    val uiModel = SettingsItemUiModel(
        title = stringResource(id = string.settings_blacklist_title),
        description = stringResource(id = string.settings_blacklist_description)
    )
    SettingsItem(item = uiModel, onClick = toBlacklist)
}

@Composable
private fun InfoSection() {
    val uiModel = SettingsSectionUiModel(
        title = stringResource(id = string.settings_info_section_title)
    )
    SettingsSection(item = uiModel)
}

@Composable
private fun RestartOnboardingItem(toOnboarding: () -> Unit) {
    val uiModel = SettingsItemUiModel(
        title = stringResource(id = string.settings_restart_onboarding_title),
        description = stringResource(id = string.settings_restart_onboarding_description)
    )
    SettingsItem(item = uiModel, onClick = toOnboarding)
}

@Composable
private fun CheckPermissionsItem(state: SettingsState.Permissions, toPermissions: () -> Unit) {
    val uiModel = SettingsItemUiModel(
        title = stringResource(id = string.settings_check_permissions_title),
        description = stringResource(id = string.settings_check_permissions_description)
    )
    SettingsItem(item = uiModel, onClick = toPermissions) {
        when (state) {
            SettingsState.Permissions.Loading -> LoadingSpinner()
            SettingsState.Permissions.Denied -> Icon(
                painter = rememberVectorPainter(image = Icons.Rounded.Warning),
                tint = MaterialTheme.colorScheme.error,
                contentDescription = stringResource(string.settings_check_permissions_not_granted_description),
                modifier = Modifier
                    .padding(end = Dimens.Margin.Small)
                    .size(Dimens.Icon.Small)
            )
            SettingsState.Permissions.Granted -> Icon(
                painter = rememberVectorPainter(image = Icons.Rounded.CheckCircle),
                tint = MaterialTheme.colorScheme.secondary,
                contentDescription = stringResource(string.settings_check_permissions_granted_description),
                modifier = Modifier
                    .padding(end = Dimens.Margin.Small)
                    .size(Dimens.Icon.Small)
            )
        }
    }
}

@Composable
private fun AboutItem(toAbout: () -> Unit) {
    val uiModel = SettingsItemUiModel(
        title = stringResource(id = string.settings_about_title),
        description = stringResource(id = string.settings_about_description)
    )
    SettingsItem(item = uiModel, onClick = toAbout)
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.Margin.Large),
        contentAlignment = Alignment.BottomCenter
    ) {
        Text(text = stringResource(id = string.settings_footer_app_version, version))
    }
}

object SettingsPage {

    data class Actions(
        val toBlacklist: () -> Unit,
        val toWidgetLayout: () -> Unit,
        val toIconPacks: () -> Unit,
        val toOnboarding: () -> Unit,
        val toPermissions: () -> Unit,
        val toAbout: () -> Unit
    )
}

@Composable
@Preview
fun SettingsContentPreview() {
    val state = SettingsState(
        permissions = SettingsState.Permissions.Granted,
        appVersion = "123",
        openOnboardingEffect = Effect.empty()
    )
    ShuttleTheme {
        SettingsContent(
            state = state,
            actions = SettingsPage.Actions(
                toBlacklist = {},
                toWidgetLayout = {},
                toIconPacks = {},
                toOnboarding = {},
                toPermissions = {},
                toAbout = {}
            ),
            resetOnboardingShown = {},
            modifier = Modifier
        )
    }
}

@Composable
@Preview
fun SettingsItemPreview() {
    ShuttleTheme {
        val uiModel = SettingsItemUiModel(
            title = stringResource(id = string.settings_blacklist_title),
            description = stringResource(id = string.settings_blacklist_description)
        )
        SettingsItem(item = uiModel, onClick = {})
    }
}
