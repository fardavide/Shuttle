package shuttle.utils.compose

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.AndroidUiDispatcher
import app.cash.molecule.RecompositionMode
import app.cash.molecule.launchMolecule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class MoleculeViewModel<Action, State> : ViewModel() {

    private val actions = MutableSharedFlow<Action>(extraBufferCapacity = 20)
    private val moleculeScope = CoroutineScope(viewModelScope.coroutineContext + AndroidUiDispatcher.Main)
    val state: StateFlow<State> by lazy(LazyThreadSafetyMode.NONE) {
        moleculeScope.launchMolecule(mode = RecompositionMode.ContextClock) {
            models(actions)
        }
    }

    @Composable
    protected abstract fun models(actions: Flow<Action>): State

    fun submit(action: Action) {
        if (!actions.tryEmit(action)) {
            error("Event buffer overflow.")
        }
    }

    protected fun launchInScope(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        body: suspend CoroutineScope.() -> Unit
    ) {
        viewModelScope.launch(context, start, body)
    }
}
