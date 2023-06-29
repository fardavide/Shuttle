package shuttle.settings.presentation.action

import com.google.accompanist.permissions.MultiplePermissionsState

sealed interface SettingsAction {

    object ResetOnboardingShown : SettingsAction
    data class UpdatePermissionsState(val permissionsState: MultiplePermissionsState) : SettingsAction
}
