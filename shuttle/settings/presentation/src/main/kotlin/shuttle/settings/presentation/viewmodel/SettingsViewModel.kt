package shuttle.settings.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import kotlinx.coroutines.launch
import shuttle.accessibility.usecase.IsLaunchCounterServiceEnabled
import shuttle.permissions.domain.usecase.HasAllLocationPermissions
import shuttle.settings.presentation.viewmodel.SettingsViewModel.Action
import shuttle.settings.presentation.viewmodel.SettingsViewModel.State
import shuttle.settings.presentation.viewmodel.SettingsViewModel.State.Loading
import shuttle.settings.presentation.viewmodel.SettingsViewModel.State.PermissionsState
import shuttle.util.android.viewmodel.ShuttleViewModel

@OptIn(ExperimentalPermissionsApi::class)
class SettingsViewModel(
    private val hasAllLocationPermissions: HasAllLocationPermissions,
    private val isLaunchCounterServiceEnabled: IsLaunchCounterServiceEnabled
) : ShuttleViewModel<Action, State>(initialState = Loading) {

    override fun submit(action: Action) {
        viewModelScope.launch {
            val newState = when (action) {
                is Action.UpdatePermissionsState -> onPermissionsStateUpdate(action.permissionsState)
            }
            emit(newState)
        }
    }

    private fun onPermissionsStateUpdate(permissionsState: MultiplePermissionsState): State =
        if (hasAllLocationPermissions(permissionsState) && isLaunchCounterServiceEnabled()) PermissionsState.Granted
        else PermissionsState.Denied

    sealed interface Action {

        data class UpdatePermissionsState(val permissionsState: MultiplePermissionsState): Action
    }

    sealed interface State {

        object Loading : State

        sealed interface PermissionsState : State {
            object Granted : PermissionsState
            object Denied : PermissionsState
        }
    }
}
