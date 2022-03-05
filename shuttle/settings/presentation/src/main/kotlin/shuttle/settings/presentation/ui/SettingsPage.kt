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
import androidx.compose.ui.tooling.preview.Preview
import shuttle.design.theme.Dimens
import shuttle.settings.presentation.model.SettingItemUiModel
import shuttle.settings.presentation.resources.Strings

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SettingsPage(
    toBlacklist: () -> Unit
) {
    Scaffold(topBar = { SmallTopAppBar(title = { Text(Strings.SettingsTitle) }) }) {
        SettingsContent(toBlacklist = toBlacklist)
    }
}

@Composable
private fun SettingsContent(
    toBlacklist: () -> Unit
) {
    LazyColumn {
        item { BlacklistItem(toBlacklist) }
    }
}

@Composable
private fun BlacklistItem(toBlacklist: () -> Unit) {
    val uiModel = SettingItemUiModel(
        title = Strings.BlacklistTitle,
        description = Strings.BlacklistDescription
    )
    SettingsItem(uiModel, toBlacklist)
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
fun SettingsItemPreview() {
    MaterialTheme {
        val uiModel = SettingItemUiModel(
            title = Strings.BlacklistTitle,
            description = Strings.BlacklistDescription
        )
        SettingsItem(uiModel, onClick = {})
    }
}