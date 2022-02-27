package shuttle.settings.presentation.viewmodel

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
import shuttle.settings.domain.usecase.AddToBlacklist
import shuttle.settings.domain.usecase.ObserveAppsBlacklistSettings
import shuttle.settings.domain.usecase.RemoveFromBlacklist
import shuttle.settings.presentation.mapper.AppBlacklistSettingUiModelMapper
import shuttle.settings.presentation.model.AppBlacklistSettingUiModel

internal class BlacklistSettingsViewModel(
    private val appUiModelMapper: AppBlacklistSettingUiModelMapper,
    observeAppsBlacklistSettings: ObserveAppsBlacklistSettings,
    private val addToBlacklist: AddToBlacklist,
    private val removeFromBlacklist: RemoveFromBlacklist
) : ViewModel() {

    val state: StateFlow<State>
        get() = mutableState.asStateFlow()

    private val mutableState: MutableStateFlow<State> =
        MutableStateFlow(State.Loading)

    init {
        observeAppsBlacklistSettings().map { list ->
            State.Data(appUiModelMapper.toUiModels(list))
        }.onEach { mutableState.emit(it) }
            .launchIn(viewModelScope)
    }

    fun submit(action: Action) {
        val currentState = state.value as? State.Data ?: return
        viewModelScope.launch {
            val newState = when (action) {
                is Action.AddToBlacklist -> onAddToBlacklist(currentState, action.appId)
                is Action.RemoveFromBlacklist -> onRemoveFromBlacklist(currentState, action.appId)
            }
            mutableState.emit(newState)
        }
    }

    private suspend fun onAddToBlacklist(currentState: State.Data, appId: AppId): State {
        val newData = currentState.apps.map { app ->
            val shouldBeBlacklisted =
                if (app.id == appId) true
                else app.isBlacklisted
            app.copy(isBlacklisted = shouldBeBlacklisted)
        }
        viewModelScope.launch {
            addToBlacklist(appId)
        }
        return State.Data(newData)
    }

    private suspend fun onRemoveFromBlacklist(currentState: State.Data, appId: AppId): State {
        val newData = currentState.apps.map { app ->
            val shouldBeBlacklisted =
                if (app.id == appId) false
                else app.isBlacklisted
            app.copy(isBlacklisted = shouldBeBlacklisted)
        }
        viewModelScope.launch {
            removeFromBlacklist(appId)
        }
        return State.Data(newData)
    }

    sealed interface State {

        object Loading : State
        data class Data(val apps: List<AppBlacklistSettingUiModel>) : State
        data class Error(val message: String) : State
    }

    sealed interface Action {

        data class AddToBlacklist(val appId: AppId): Action
        data class RemoveFromBlacklist(val appId: AppId): Action
    }
}
