package shuttle.settings.presentation.state

import kotlinx.collections.immutable.ImmutableList
import shuttle.settings.presentation.model.AppBlacklistSettingUiModel

sealed interface BlacklistSettingsState {

    object Loading : BlacklistSettingsState
    data class Data(val apps: ImmutableList<AppBlacklistSettingUiModel>) : BlacklistSettingsState
    data class Error(val message: String) : BlacklistSettingsState
}
