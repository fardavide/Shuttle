package shuttle.permissions.viewmodel

import android.content.ComponentName
import android.content.ContentResolver
import android.provider.Settings
import android.text.TextUtils
import androidx.lifecycle.viewModelScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import kotlinx.coroutines.launch
import shuttle.permissions.mapper.PermissionItemsUiModelMapper
import shuttle.permissions.model.PermissionItemsUiModel
import shuttle.permissions.viewmodel.PermissionsViewModel.Action
import shuttle.permissions.viewmodel.PermissionsViewModel.State
import shuttle.util.android.viewmodel.ShuttleViewModel

@OptIn(ExperimentalPermissionsApi::class)
internal class PermissionsViewModel(
    private val accessibilityServiceComponentName: ComponentName,
    private val contentResolver: ContentResolver,
    private val permissionItemsUiModelMapper: PermissionItemsUiModelMapper,
) : ShuttleViewModel<Action, State>(initialState = State.Loading) {

    override fun submit(action: Action) {
        viewModelScope.launch {
            val newState = when (action) {
                is Action.UpdatePermissionsState -> onPermissionsStateUpdate(action.permissionsState)
            }
            emit(newState)
        }
    }

    private fun onPermissionsStateUpdate(permissionsState: MultiplePermissionsState): State {
        val uiModel = permissionItemsUiModelMapper.toUiModel(
            permissionsState = permissionsState,
            isAccessibilityServiceEnabled = isAccessibilityServiceEnabled()
        )
        return if (uiModel.areAllGranted()) State.AllGranted
        else State.Pending(uiModel)
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

        object Loading : State

        object AllGranted : State

        data class Pending(val permissionItemsUiModel: PermissionItemsUiModel) : State
    }
}
