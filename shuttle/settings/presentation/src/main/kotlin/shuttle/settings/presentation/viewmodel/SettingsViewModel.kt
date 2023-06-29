package shuttle.settings.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.google.accompanist.permissions.MultiplePermissionsState
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import shuttle.accessibility.usecase.IsLaunchCounterServiceEnabled
import shuttle.design.util.Effect
import shuttle.permissions.domain.usecase.HasAllLocationPermissions
import shuttle.settings.domain.usecase.ResetOnboardingShown
import shuttle.settings.presentation.action.SettingsAction
import shuttle.settings.presentation.state.SettingsState
import shuttle.util.android.viewmodel.ShuttleViewModel
import shuttle.utils.kotlin.GetAppVersion

@KoinViewModel
class SettingsViewModel(
    private val getAppVersion: GetAppVersion,
    private val hasAllLocationPermissions: HasAllLocationPermissions,
    private val isLaunchCounterServiceEnabled: IsLaunchCounterServiceEnabled,
    private val resetOnboardingShown: ResetOnboardingShown
) : ShuttleViewModel<SettingsAction, SettingsState>(initialState = SettingsState.Loading) {

    init {
        viewModelScope.launch {
            val newState = state.value.copy(appVersion = getAppVersion().toString())
            emit(newState)
        }
    }

    override fun submit(action: SettingsAction) {
        viewModelScope.launch {
            val newState = when (action) {
                SettingsAction.ResetOnboardingShown -> onResetOnboardingShown()
                is SettingsAction.UpdatePermissionsState -> onPermissionsStateUpdate(action.permissionsState)
            }
            emit(newState)
        }
    }

    private suspend fun onResetOnboardingShown(): SettingsState {
        resetOnboardingShown()
        return state.value.copy(openOnboardingEffect = Effect.of(Unit))
    }

    private fun onPermissionsStateUpdate(permissionsState: MultiplePermissionsState): SettingsState {
        val hasPermissions = hasAllLocationPermissions(permissionsState) && isLaunchCounterServiceEnabled()
        val permissions = if (hasPermissions) SettingsState.Permissions.Granted else SettingsState.Permissions.Denied

        return state.value.copy(permissions = permissions)
    }

}
