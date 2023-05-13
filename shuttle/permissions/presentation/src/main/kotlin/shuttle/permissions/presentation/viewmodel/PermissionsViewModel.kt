package shuttle.permissions.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import shuttle.accessibility.usecase.IsLaunchCounterServiceEnabled
import shuttle.permissions.presentation.mapper.PermissionItemsUiModelMapper
import shuttle.permissions.presentation.model.PermissionItemsUiModel
import shuttle.permissions.presentation.viewmodel.PermissionsViewModel.Action
import shuttle.permissions.presentation.viewmodel.PermissionsViewModel.State
import shuttle.util.android.viewmodel.ShuttleViewModel

@KoinViewModel
@OptIn(ExperimentalPermissionsApi::class)
internal class PermissionsViewModel(
    private val isLaunchCounterServiceEnabled: IsLaunchCounterServiceEnabled,
    private val permissionItemsUiModelMapper: PermissionItemsUiModelMapper
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
            isAccessibilityServiceEnabled = isLaunchCounterServiceEnabled()
        )
        return if (uiModel.areAllGranted()) State.AllGranted
        else State.Pending(uiModel)
    }

    sealed interface Action {

        data class UpdatePermissionsState(val permissionsState: MultiplePermissionsState) : Action
    }

    sealed interface State {

        object Loading : State

        object AllGranted : State

        data class Pending(val permissionItemsUiModel: PermissionItemsUiModel) : State
    }
}
