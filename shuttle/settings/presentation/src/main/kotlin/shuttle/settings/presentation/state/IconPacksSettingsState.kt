package shuttle.settings.presentation.state

import kotlinx.collections.immutable.ImmutableList
import shuttle.settings.presentation.model.IconPackSettingsItemUiModel

internal sealed interface IconPacksSettingsState {

    object Loading : IconPacksSettingsState
    data class Data(val iconPackSettingItems: ImmutableList<IconPackSettingsItemUiModel>) : IconPacksSettingsState
}
