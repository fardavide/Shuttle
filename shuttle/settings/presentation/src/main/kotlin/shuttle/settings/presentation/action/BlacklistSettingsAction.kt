package shuttle.settings.presentation.action

import shuttle.apps.domain.model.AppId

sealed interface BlacklistSettingsAction {

    data class AddToBlacklist(val appId: AppId) : BlacklistSettingsAction
    data class RemoveFromBlacklist(val appId: AppId) : BlacklistSettingsAction
    data class Search(val query: String) : BlacklistSettingsAction
}
