package shuttle.permissions.viewmodel

import android.content.ComponentName
import android.content.ContentResolver
import android.provider.Settings
import android.text.TextUtils
import androidx.lifecycle.viewModelScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import kotlinx.coroutines.launch
import shuttle.permissions.mapper.LocationPermissionsStateMapper
import shuttle.permissions.model.LocationPermissionsState
import shuttle.permissions.viewmodel.PermissionsViewModel.Action
import shuttle.permissions.viewmodel.PermissionsViewModel.State
import shuttle.util.android.viewmodel.ShuttleViewModel

@OptIn(ExperimentalPermissionsApi::class)
internal class PermissionsViewModel(
    private val accessibilityServiceComponentName: ComponentName,
    private val contentResolver: ContentResolver,
    private val mapper: LocationPermissionsStateMapper
) : ShuttleViewModel<Action, State>(initialState = State.Ide) {

    override fun submit(action: Action) {
        viewModelScope.launch {
            val newState = when (action) {
                is Action.UpdatePermissionsState -> onPermissionsStateUpdate(action.permissionsState)
            }
            emit(newState)
        }
    }

    private fun onPermissionsStateUpdate(permissionsState: MultiplePermissionsState): State {
        val locationPermissionsState = mapper.toLocationPermissionState(permissionsState)
        val isLocationGranted = locationPermissionsState == LocationPermissionsState.AllGranted
        val isAccessibilityGranted = isAccessibilityServiceEnabled()
        return when {
            isLocationGranted && isAccessibilityGranted -> State.AllGranted
            isLocationGranted && isAccessibilityGranted.not() -> State.AccessibilityPending
            else -> State.LocationPending(locationPermissionsState as LocationPermissionsState.Pending)
        }
    }

    private fun isAccessibilityServiceEnabled(): Boolean {
        val enabledServicesSetting: String =
            Settings.Secure.getString(contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
                ?: return false
        val colonSplitter = TextUtils.SimpleStringSplitter(':')
        colonSplitter.setString(enabledServicesSetting)
        while (colonSplitter.hasNext()) {
            val componentNameString: String = colonSplitter.next()
            val enabledService = ComponentName.unflattenFromString(componentNameString)
            if (enabledService != null && enabledService == accessibilityServiceComponentName) return true
        }
        return false
    }

    sealed interface Action {

        data class UpdatePermissionsState(val permissionsState: MultiplePermissionsState): Action
    }

    sealed interface State {

        object Ide : State

        object AllGranted : State

        object AccessibilityPending : State

        data class LocationPending(val locationState: LocationPermissionsState.Pending) : State
    }
}
