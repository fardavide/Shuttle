package shuttle.settings.presentation.ui.page

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import arrow.core.Option
import coil.compose.rememberAsyncImagePainter
import kotlinx.collections.immutable.ImmutableList
import org.koin.androidx.compose.getViewModel
import shuttle.apps.domain.model.AppId
import shuttle.design.theme.Dimens
import shuttle.design.ui.BackIconButton
import shuttle.design.ui.LoadingSpinner
import shuttle.design.util.collectAsStateLifecycleAware
import shuttle.resources.R.string
import shuttle.settings.presentation.action.IconPacksSettingsAction
import shuttle.settings.presentation.model.IconPackSettingsItemUiModel
import shuttle.settings.presentation.model.IconPackSettingsItemUiModel.FromApp
import shuttle.settings.presentation.model.IconPackSettingsItemUiModel.SystemDefault
import shuttle.settings.presentation.state.IconPacksSettingsState
import shuttle.settings.presentation.viewmodel.IconPacksSettingsViewModel

@Composable
fun IconPackSettingsPage(onBack: () -> Unit) {
    Scaffold(
        modifier = Modifier.statusBarsPadding().navigationBarsPadding(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = string.settings_icon_pack_title)) },
                navigationIcon = { BackIconButton(onBack) }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            IconPacksSettingsContent()
        }
    }
}

@Composable
private fun IconPacksSettingsContent() {
    val viewModel: IconPacksSettingsViewModel = getViewModel()

    val s by viewModel.state.collectAsStateLifecycleAware()
    when (val state = s) {
        IconPacksSettingsState.Loading -> LoadingSpinner()
        is IconPacksSettingsState.Data -> IconPackList(
            state.iconPackSettingItems,
            onSetCurrentIconPack = { viewModel.submit(IconPacksSettingsAction.SetCurrentIconPack(it)) }
        )
    }
}

@Composable
private fun IconPackList(
    iconPackItems: ImmutableList<IconPackSettingsItemUiModel>,
    onSetCurrentIconPack: (Option<AppId>) -> Unit
) {
    LazyColumn(contentPadding = PaddingValues(Dimens.Margin.small)) {
        items(iconPackItems) { iconPackItem ->
            FromAppIconPackItem(
                iconPackItem = iconPackItem,
                name = when (iconPackItem) {
                    is FromApp -> iconPackItem.name
                    is SystemDefault -> stringResource(id = iconPackItem.name)
                },
                icon = (iconPackItem as? FromApp)?.icon,
                onSetCurrentIconPack = onSetCurrentIconPack
            )
        }
    }
}

@Composable
private fun FromAppIconPackItem(
    iconPackItem: IconPackSettingsItemUiModel,
    name: String,
    icon: Drawable?,
    onSetCurrentIconPack: (Option<AppId>) -> Unit
) {
    val toggleAction = { isChecked: Boolean ->
        if (isChecked) onSetCurrentIconPack(iconPackItem.idOrNone())
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = Dimens.Margin.XXSmall, horizontal = Dimens.Margin.small)
    ) {
        if (icon != null) {
            Image(
                painter = rememberAsyncImagePainter(model = icon),
                contentDescription = stringResource(id = string.app_icon_description),
                modifier = Modifier.size(Dimens.Icon.Medium)
            )
            Spacer(modifier = Modifier.width(Dimens.Margin.medium))
        }
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium
        )
        Column(horizontalAlignment = Alignment.End, modifier = Modifier.fillMaxWidth()) {
            Checkbox(checked = iconPackItem.isSelected, onCheckedChange = toggleAction)
        }
    }
}
