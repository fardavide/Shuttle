package shuttle.settings.presentation.action

import com.google.accompanist.permissions.MultiplePermissionsState

sealed interface SettingsAction {

    data object ResetOnboardingShown : SettingsAction

    @JvmInline value class ToggleExperimentalAppSorting(val enable: Boolean) : SettingsAction

    @JvmInline value class UpdatePermissionsState(val permissionsState: MultiplePermissionsState) : SettingsAction
}
