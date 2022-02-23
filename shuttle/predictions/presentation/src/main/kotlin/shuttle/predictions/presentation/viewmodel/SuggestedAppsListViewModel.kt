package shuttle.predictions.presentation.viewmodel

import android.content.Intent
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import shuttle.apps.domain.model.AppId
import shuttle.apps.presentation.mapper.AppUiModelMapper
import shuttle.apps.presentation.model.AppUiModel
import shuttle.predictions.domain.model.Constraints
import shuttle.predictions.domain.usecase.ObserveSuggestedApps
import shuttle.stats.domain.usecase.IncrementOpenCounter

internal class SuggestedAppsListViewModel(
    private val appUiModelMapper: AppUiModelMapper,
    private val incrementOpenCounter: IncrementOpenCounter,
    observeSuggestedApps: ObserveSuggestedApps,
    private val packageManager: PackageManager
) : ViewModel() {

    val state: StateFlow<State>
        get() = mutableState.asStateFlow()

    private val mutableState: MutableStateFlow<State> =
        MutableStateFlow(State.Loading)

    init {
        observeSuggestedApps(constraints = TODO("Get constraints")).map { either ->
            either.map(appUiModelMapper::toUiModels)
                .fold(
                    ifRight = State::Data,
                    ifLeft = { State.Error("Unknown") }
                )
        }.onEach { mutableState.emit(it) }
            .launchIn(viewModelScope)
    }

    fun submit(action: Action) {
        viewModelScope.launch {
            val newState = when (action) {
                is Action.OnAppClicked -> onAppClicked(action.appId)
            }
            mutableState.emit(newState)
        }
    }

    private suspend fun onAppClicked(appId: AppId): State {
        val constraints: Constraints = TODO("Get constraints")
        incrementOpenCounter(appId, constraints.location, constraints.time)
        val intent = packageManager.getLaunchIntentForPackage(appId.value)!!
        return State.RequestOpenApp(intent)
    }

    sealed interface State {

        object Loading : State
        data class Data(val apps: List<AppUiModel>) : State
        data class Error(val message: String) : State
        data class RequestOpenApp(val intent: Intent) : State
    }

    sealed interface Action {

        data class OnAppClicked(val appId: AppId) : Action
    }
}
