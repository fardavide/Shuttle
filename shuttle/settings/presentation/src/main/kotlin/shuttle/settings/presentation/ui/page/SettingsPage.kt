package shuttle.settings.presentation.ui.page

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
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
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import org.koin.androidx.compose.getViewModel
import shuttle.consents.presentation.ui.ConsentsModal
import shuttle.design.TestTag
import shuttle.design.theme.Dimens
import shuttle.design.theme.ShuttleTheme
import shuttle.design.ui.LoadingSpinner
import shuttle.design.util.ConsumableLaunchedEffect
import shuttle.design.util.Effect
import shuttle.design.util.collectAsStateLifecycleAware
import shuttle.permissions.domain.model.backgroundPermissionsList
import shuttle.resources.R.string
import shuttle.settings.presentation.action.SettingsAction
import shuttle.settings.presentation.model.SettingsItemUiModel
import shuttle.settings.presentation.model.SettingsSectionUiModel
import shuttle.settings.presentation.state.SettingsState
import shuttle.settings.presentation.viewmodel.SettingsViewModel

@Composable
fun SettingsPage(navigationActions: SettingsPage.NavigationActions) {
    val viewModel = getViewModel<SettingsViewModel>()
    val state by viewModel.state.collectAsStateLifecycleAware()

    val actions = navigationActions.toActions(
        toggleConsents = { enable ->
            viewModel.submit(SettingsAction.SetIsDataCollectionEnabled(enable))
        },
        toggleExperimentalAppSorting = { enable ->
            viewModel.submit(SettingsAction.ToggleExperimentalAppSorting(enable))
        }
    )

    ConsumableLaunchedEffect(effect = state.openOnboardingEffect) {
        actions.toOnboarding()
    }

    if (state.shouldShowConsents) {
        val action = ConsentsModal.Actions(
            consent = { viewModel.submit(SettingsAction.SetIsDataCollectionEnabled(true)) },
            decline = { viewModel.submit(SettingsAction.SetIsDataCollectionEnabled(false)) },
            dismiss = { viewModel.submit(SettingsAction.SetConsentsShown) }
        )
        ConsentsModal(actions = action)
    }

    val backgroundLocationPermissionsState = rememberMultiplePermissionsState(backgroundPermissionsList)
    viewModel.submit(SettingsAction.UpdatePermissionsState(backgroundLocationPermissionsState))

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
            resetOnboardingShown = { viewModel.submit(SettingsAction.ResetOnboardingShown) },
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
    LazyColumn(
        modifier = modifier
            .fillMaxHeight()
            .testTag(TestTag.Settings)
    ) {
        item { CustomizeWidgetSection() }
        item { WidgetLayoutItem(actions.toWidgetLayout) }
        item { IconPackItem(actions.toIconPacks) }
        item { BlacklistItem(actions.toBlacklist) }

        item { DataSection() }
        item { ExperimentalAppSortingItem(state.useExperimentalAppSorting, actions.toggleExperimentalAppSorting) }
        item {
            AnimatedVisibility(visible = state.shouldShowStatisticsItem) {
                StatisticsItem(actions.toStatistics)
            }
        }
        item { ConsentsItem(state.isDataCollectionEnabled, actions.toggleConsents) }

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
private fun DataSection() {
    val uiModel = SettingsSectionUiModel(
        title = stringResource(id = string.settings_data_section_title)
    )
    SettingsSection(item = uiModel)
}

@Composable
private fun ExperimentalAppSortingItem(
    useExperimentalAppSorting: Boolean,
    toggleExperimentalAppSorting: (Boolean) -> Unit
) {
    val uiModel = SettingsItemUiModel(
        title = stringResource(id = string.settings_experimental_app_sorting_title),
        description = stringResource(id = string.settings_experimental_app_sorting_description)
    )
    SettingsItem(item = uiModel, onClick = { toggleExperimentalAppSorting(!useExperimentalAppSorting) }) {
        Switch(checked = useExperimentalAppSorting, onCheckedChange = null)
    }
}

@Composable
private fun StatisticsItem(toDataSettings: () -> Unit) {
    val uiModel = SettingsItemUiModel(
        title = stringResource(id = string.settings_statistics_title),
        description = stringResource(id = string.settings_statistics_description)
    )
    SettingsItem(item = uiModel, onClick = toDataSettings)
}

@Composable
private fun ConsentsItem(isDataCollectionEnabled: Boolean, toggleConsents: (Boolean) -> Unit) {
    val uiModel = SettingsItemUiModel(
        title = stringResource(id = string.consents_data_title),
        description = stringResource(id = string.settings_consents_description)
    )
    SettingsItem(item = uiModel, onClick = { toggleConsents(!isDataCollectionEnabled) }) {
        Switch(checked = isDataCollectionEnabled, onCheckedChange = null)
    }
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
                    .padding(end = Dimens.Margin.small)
                    .size(Dimens.Icon.Small)
            )
            SettingsState.Permissions.Granted -> Icon(
                painter = rememberVectorPainter(image = Icons.Rounded.CheckCircle),
                tint = MaterialTheme.colorScheme.secondary,
                contentDescription = stringResource(string.settings_check_permissions_granted_description),
                modifier = Modifier
                    .padding(end = Dimens.Margin.small)
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
    Row(modifier = Modifier.padding(horizontal = Dimens.Margin.medium, vertical = Dimens.Margin.small)) {
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
            .padding(horizontal = Dimens.Margin.medium, vertical = Dimens.Margin.small)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = item.title, style = MaterialTheme.typography.titleMedium)
            Text(text = item.description, style = MaterialTheme.typography.bodySmall)
        }
        Row(
            modifier = Modifier.padding(start = Dimens.Margin.small),
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
            .padding(Dimens.Margin.large),
        contentAlignment = Alignment.BottomCenter
    ) {
        Text(text = stringResource(id = string.settings_footer_app_version, version))
    }
}

object SettingsPage {

    internal data class Actions(
        val toAbout: () -> Unit,
        val toBlacklist: () -> Unit,
        val toIconPacks: () -> Unit,
        val toOnboarding: () -> Unit,
        val toPermissions: () -> Unit,
        val toStatistics: () -> Unit,
        val toWidgetLayout: () -> Unit,
        val toggleConsents: (Boolean) -> Unit,
        val toggleExperimentalAppSorting: (Boolean) -> Unit
    ) {

        companion object {

            val Empty = Actions(
                toAbout = {},
                toBlacklist = {},
                toIconPacks = {},
                toOnboarding = {},
                toPermissions = {},
                toStatistics = {},
                toWidgetLayout = {},
                toggleConsents = {},
                toggleExperimentalAppSorting = {}
            )
        }
    }

    data class NavigationActions(
        val toAbout: () -> Unit,
        val toBlacklist: () -> Unit,
        val toIconPacks: () -> Unit,
        val toOnboarding: () -> Unit,
        val toPermissions: () -> Unit,
        val toStatistics: () -> Unit,
        val toWidgetLayout: () -> Unit
    ) {

        internal fun toActions(
            toggleConsents: (Boolean) -> Unit,
            toggleExperimentalAppSorting: (Boolean) -> Unit
        ) = Actions(
            toAbout = toAbout,
            toBlacklist = toBlacklist,
            toIconPacks = toIconPacks,
            toOnboarding = toOnboarding,
            toPermissions = toPermissions,
            toStatistics = toStatistics,
            toWidgetLayout = toWidgetLayout,
            toggleConsents = toggleConsents,
            toggleExperimentalAppSorting = toggleExperimentalAppSorting
        )
    }
}

@Preview
@Composable
private fun SettingsContentPreview() {
    val state = SettingsState(
        appVersion = "123",
        isDataCollectionEnabled = true,
        openOnboardingEffect = Effect.empty(),
        permissions = SettingsState.Permissions.Granted,
        shouldShowConsents = false,
        shouldShowStatisticsItem = true,
        useExperimentalAppSorting = false
    )
    ShuttleTheme {
        SettingsContent(
            state = state,
            actions = SettingsPage.Actions.Empty,
            resetOnboardingShown = {},
            modifier = Modifier
        )
    }
}

@Preview
@Composable
private fun SettingsItemPreview() {
    ShuttleTheme {
        val uiModel = SettingsItemUiModel(
            title = stringResource(id = string.settings_blacklist_title),
            description = stringResource(id = string.settings_blacklist_description)
        )
        SettingsItem(item = uiModel, onClick = {})
    }
}
