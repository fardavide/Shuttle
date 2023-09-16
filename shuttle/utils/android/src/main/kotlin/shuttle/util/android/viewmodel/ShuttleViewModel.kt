package shuttle.util.android.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class ShuttleViewModel<Action, State>(
    initialState: State
) : ViewModel() {

    val state: StateFlow<State>
        get() = mutableState.asStateFlow()

    private val mutableState: MutableStateFlow<State> =
        MutableStateFlow(initialState)

    abstract fun submit(action: Action)

    protected suspend fun emit(state: State) {
        mutableState.emit(state)
    }

    fun update(transform: (State) -> State) {
        mutableState.update(transform)
    }
}
