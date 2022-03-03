package shuttle.permissions.viewmodel

import shuttle.permissions.model.LocationPermissionsState
import shuttle.permissions.viewmodel.PermissionsViewModel.Action
import shuttle.permissions.viewmodel.PermissionsViewModel.State
import shuttle.util.android.viewmodel.ShuttleViewModel

internal class PermissionsViewModel(

) : ShuttleViewModel<Action, State>(initialState = State.Ide) {

    override fun submit(action: Action) {
        TODO("Not yet implemented")
    }

    sealed interface Action

    sealed interface State {

        object Ide : State

        object AllGranted : State

        object AllPending : State

        object AccessibilityPending : State

        data class LocationPending(val locationState: LocationPermissionsState) : State
    }
}
