package shuttle.settings.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import shuttle.accessibility.usecase.IsLaunchCounterServiceEnabled
import shuttle.permissions.domain.usecase.HasAllLocationPermissions
import shuttle.settings.domain.usecase.ObserveUseCurrentLocationOnly
import shuttle.settings.domain.usecase.UpdateUseCurrentLocationOnly
import shuttle.settings.presentation.viewmodel.SettingsViewModel.Action
import shuttle.settings.presentation.viewmodel.SettingsViewModel.State
import shuttle.util.android.viewmodel.ShuttleViewModel
import shuttle.utils.kotlin.GetAppVersion

@OptIn(ExperimentalPermissionsApi::class)
class SettingsViewModel(
    private val getAppVersion: GetAppVersion,
    private val hasAllLocationPermissions: HasAllLocationPermissions,
    private val isLaunchCounterServiceEnabled: IsLaunchCounterServiceEnabled,
    private val observeUseCurrentLocationOnly: ObserveUseCurrentLocationOnly,
    private val updateUseCurrentLocationOnly: UpdateUseCurrentLocationOnly
) : ShuttleViewModel<Action, State>(initialState = State.Loading) {

    init {
        viewModelScope.launch {
            val newState = state.value.copy(appVersion = getAppVersion().toString())
            emit(newState)
        }
        viewModelScope.launch {
            observeUseCurrentLocationOnly().collectLatest { useCurrentLocationOnly ->
                val currentLocationOnly =
                    if (useCurrentLocationOnly) State.CurrentLocationOnly.True else State.CurrentLocationOnly.False
                val newState = state.value.copy(currentLocationOnly = currentLocationOnly)
                emit(newState)
            }
        }
    }

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
        viewModelScope.launch {
            updateUseCurrentLocationOnly(value)
        }

        val currentLocationOnly = if (value) State.CurrentLocationOnly.True else State.CurrentLocationOnly.False
        return state.value.copy(currentLocationOnly = currentLocationOnly)
    }

    sealed interface Action {

        data class UpdatePermissionsState(val permissionsState: MultiplePermissionsState): Action
        data class UpdateCurrentLocationOnly(val value: Boolean): Action
    }

    data class State(
        val permissions: Permissions,
        val currentLocationOnly: CurrentLocationOnly,
        val appVersion: String
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
                currentLocationOnly = CurrentLocationOnly.Loading,
                appVersion = ""
            )
        }
    }
}
