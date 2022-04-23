package shuttle.settings.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import kotlinx.coroutines.launch
import shuttle.accessibility.usecase.IsLaunchCounterServiceEnabled
import shuttle.permissions.domain.usecase.HasAllLocationPermissions
import shuttle.settings.presentation.viewmodel.SettingsViewModel.Action
import shuttle.settings.presentation.viewmodel.SettingsViewModel.State
import shuttle.util.android.viewmodel.ShuttleViewModel

@OptIn(ExperimentalPermissionsApi::class)
class SettingsViewModel(
    private val hasAllLocationPermissions: HasAllLocationPermissions,
    private val isLaunchCounterServiceEnabled: IsLaunchCounterServiceEnabled
) : ShuttleViewModel<Action, State>(initialState = State.Loading) {

    override fun submit(action: Action) {
        viewModelScope.launch {
            val newState = when (action) {
                is Action.UpdatePermissionsState -> onPermissionsStateUpdate(action.permissionsState)
                is Action.UpdateCurrentLocationOnly -> updateCurrentLocationOnly(action.value)
            }
            emit(newState)
        }
    }

    private fun onPermissionsStateUpdate(permissionsState: MultiplePermissionsState): State {
        val hasPermissions = hasAllLocationPermissions(permissionsState) && isLaunchCounterServiceEnabled()
        val permissions = if (hasPermissions) State.Permissions.Granted else State.Permissions.Denied

        return state.value.copy(permissions = permissions)
    }

    private fun updateCurrentLocationOnly(value: Boolean): State {
        val currentLocationOnly = if (value) State.CurrentLocationOnly.True else State.CurrentLocationOnly.False

        return state.value.copy(currentLocationOnly = currentLocationOnly)
    }

    sealed interface Action {

        data class UpdatePermissionsState(val permissionsState: MultiplePermissionsState): Action
        data class UpdateCurrentLocationOnly(val value: Boolean): Action
    }

    data class State(
        val permissions: Permissions,
        val currentLocationOnly: CurrentLocationOnly
    ) {

        sealed interface Permissions {

            object Loading : Permissions
            object Granted : Permissions
            object Denied : Permissions
        }

        sealed interface CurrentLocationOnly {

            object Loading : CurrentLocationOnly
            object False : CurrentLocationOnly
            object True : CurrentLocationOnly
        }

        companion object {

            val Loading = State(
                permissions = Permissions.Loading,
                currentLocationOnly = CurrentLocationOnly.Loading
            )
        }
    }
}
