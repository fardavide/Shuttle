package shuttle.settings.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import shuttle.accessibility.usecase.IsLaunchCounterServiceEnabled
import shuttle.permissions.domain.usecase.HasAllLocationPermissions
import shuttle.settings.domain.usecase.ObservePrioritizeLocation
import shuttle.settings.domain.usecase.UpdatePrioritizeLocation
import shuttle.settings.presentation.viewmodel.SettingsViewModel.Action
import shuttle.settings.presentation.viewmodel.SettingsViewModel.State
import shuttle.util.android.viewmodel.ShuttleViewModel
import shuttle.utils.kotlin.GetAppVersion

@OptIn(ExperimentalPermissionsApi::class)
class SettingsViewModel(
    private val getAppVersion: GetAppVersion,
    private val hasAllLocationPermissions: HasAllLocationPermissions,
    private val isLaunchCounterServiceEnabled: IsLaunchCounterServiceEnabled,
    private val observePrioritizeLocation: ObservePrioritizeLocation,
    private val updatePrioritizeLocation: UpdatePrioritizeLocation
) : ShuttleViewModel<Action, State>(initialState = State.Loading) {

    init {
        viewModelScope.launch {
            val newState = state.value.copy(appVersion = getAppVersion().toString())
            emit(newState)
        }
        viewModelScope.launch {
            observePrioritizeLocation().collectLatest { prioritizeLocation ->
                val prioritizeLocationValue =
                    if (prioritizeLocation) State.PrioritizeLocation.True else State.PrioritizeLocation.False
                val newState = state.value.copy(prioritizeLocation = prioritizeLocationValue)
                emit(newState)
            }
        }
    }

    override fun submit(action: Action) {
        viewModelScope.launch {
            val newState = when (action) {
                is Action.UpdatePermissionsState -> onPermissionsStateUpdate(action.permissionsState)
                is Action.UpdatePrioritizeLocation -> onUpdatePrioritizeLocation(action.value)
            }
            emit(newState)
        }
    }

    private fun onPermissionsStateUpdate(permissionsState: MultiplePermissionsState): State {
        val hasPermissions = hasAllLocationPermissions(permissionsState) && isLaunchCounterServiceEnabled()
        val permissions = if (hasPermissions) State.Permissions.Granted else State.Permissions.Denied

        return state.value.copy(permissions = permissions)
    }

    private fun onUpdatePrioritizeLocation(value: Boolean): State {
        viewModelScope.launch {
            updatePrioritizeLocation(value)
        }

        val prioritizeLocation = if (value) State.PrioritizeLocation.True else State.PrioritizeLocation.False
        return state.value.copy(prioritizeLocation = prioritizeLocation)
    }

    sealed interface Action {

        data class UpdatePermissionsState(val permissionsState: MultiplePermissionsState): Action
        data class UpdatePrioritizeLocation(val value: Boolean): Action
    }

    data class State(
        val permissions: Permissions,
        val prioritizeLocation: PrioritizeLocation,
        val appVersion: String
    ) {

        sealed interface Permissions {

            object Loading : Permissions
            object Granted : Permissions
            object Denied : Permissions
        }

        sealed interface PrioritizeLocation {

            object Loading : PrioritizeLocation
            object False : PrioritizeLocation
            object True : PrioritizeLocation
        }

        companion object {

            val Loading = State(
                permissions = Permissions.Loading,
                prioritizeLocation = PrioritizeLocation.Loading,
                appVersion = ""
            )
        }
    }
}
