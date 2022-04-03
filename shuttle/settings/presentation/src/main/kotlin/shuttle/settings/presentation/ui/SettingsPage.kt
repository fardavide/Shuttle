package shuttle.settings.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import shuttle.design.theme.Dimens
import shuttle.settings.presentation.model.SettingItemUiModel
import shuttle.settings.presentation.resources.Strings
import shuttle.settings.presentation.resources.get
import studio.forface.shuttle.design.R

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SettingsPage(
    toBlacklist: () -> Unit,
    toWidgetLayout: () -> Unit,
    toIconPacks: () -> Unit,
    toPermissions: () -> Unit
) {
    Scaffold(topBar = { SmallTopAppBar(title = { Text(stringResource(id = R.string.settings_title)) }) }) {
        SettingsContent(
            toBlacklist = toBlacklist,
            toWidgetLayout = toWidgetLayout,
            toIconPacks = toIconPacks,
            toPermissions = toPermissions
        )
    }
}

@Composable
private fun SettingsContent(
    toBlacklist: () -> Unit,
    toWidgetLayout: () -> Unit,
    toIconPacks: () -> Unit,
    toPermissions: () -> Unit
) {
    LazyColumn {
        item { BlacklistItem(toBlacklist) }
        item { WidgetLayoutItem(toWidgetLayout) }
        item { IconPackItem(toIconPacks) }
        item { CheckPermissionsItem(toPermissions) }
    }
}

@Composable
private fun BlacklistItem(toBlacklist: () -> Unit) {
    val uiModel = SettingItemUiModel(
        title = Strings.Blacklist::Title.get(),
        description = Strings.Blacklist::Description.get()
    )
    SettingsItem(uiModel, toBlacklist)
}

@Composable
private fun WidgetLayoutItem(toWidgetLayout: () -> Unit) {
    val uiModel = SettingItemUiModel(
        title = Strings.WidgetLayout::Title.get(),
        description = Strings.WidgetLayout::Description.get()
    )
    SettingsItem(uiModel, toWidgetLayout)
}

@Composable
private fun IconPackItem(toIconPacks: () -> Unit) {
    val uiModel = SettingItemUiModel(
        title = Strings.IconPack::Title.get(),
        description = Strings.IconPack::Description.get()
    )
    SettingsItem(uiModel, toIconPacks)
}

@Composable
private fun CheckPermissionsItem(toPermissions: () -> Unit) {
    val uiModel = SettingItemUiModel(
        title = Strings.CheckPermissions::Title.get(),
        description = Strings.CheckPermissions::Description.get()
    )
    SettingsItem(uiModel, toPermissions)
}

@Composable
private fun SettingsItem(item: SettingItemUiModel, onClick: () -> Unit) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .clickable(onClick = onClick)
        .padding(horizontal = Dimens.Margin.Medium, vertical = Dimens.Margin.Small)
    ) {
        Text(text = item.title, style = MaterialTheme.typography.titleMedium)
        Text(text = item.description, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
@Preview(showBackground = true)
fun SettingsPagePreview() {
    MaterialTheme {
        SettingsPage(toBlacklist = {}, toWidgetLayout = {}, toIconPacks = {}, toPermissions = {})
    }
}

@Composable
@Preview(showBackground = true)
fun SettingsItemPreview() {
    MaterialTheme {
        val uiModel = SettingItemUiModel(
            title = Strings.Blacklist::Title.get(),
            description = Strings.Blacklist::Description.get()
        )
        SettingsItem(uiModel, onClick = {})
    }
}
