package shuttle.settings.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import shuttle.apps.domain.model.AppId
import shuttle.settings.domain.model.AppBlacklistSetting
import shuttle.settings.domain.usecase.AddToBlacklist
import shuttle.settings.domain.usecase.RemoveFromBlacklist
import shuttle.settings.domain.usecase.SearchAppsBlacklistSettings
import shuttle.settings.presentation.mapper.AppBlacklistSettingUiModelMapper
import shuttle.settings.presentation.model.AppBlacklistSettingUiModel
import shuttle.settings.presentation.viewmodel.BlacklistSettingsViewModel.Action
import shuttle.settings.presentation.viewmodel.BlacklistSettingsViewModel.State
import shuttle.util.android.viewmodel.ShuttleViewModel

internal class BlacklistSettingsViewModel(
    private val appUiModelMapper: AppBlacklistSettingUiModelMapper,
    private val addToBlacklist: AddToBlacklist,
    private val removeFromBlacklist: RemoveFromBlacklist,
    private val searchAppsBlacklistSettings: SearchAppsBlacklistSettings
) : ShuttleViewModel<Action, State>(initialState = State.Loading) {

    private var sortingOrder: List<AppId>? = null

    init {
        searchAppsBlacklistSettings()
            .map { list -> State.Data(appUiModelMapper.toUiModels(list.sortIfFirstData())) }
            .onEach(::emit)
            .launchIn(viewModelScope)
    }

    override fun submit(action: Action) {
        val currentState = state.value as? State.Data ?: return
        viewModelScope.launch {
            val newState = when (action) {
                is Action.AddToBlacklist -> onAddToBlacklist(currentState, action.appId)
                is Action.RemoveFromBlacklist -> onRemoveFromBlacklist(currentState, action.appId)
                is Action.Search -> onSearch(action.query)
            }
            emit(newState)
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

    private fun onSearch(query: String): State {
        searchAppsBlacklistSettings(query)
        return state.value
    }

    private fun List<AppBlacklistSetting>.sortIfFirstData(): List<AppBlacklistSetting> {
        return if (sortingOrder == null) {
            val sorted = sortedByDescending { it.inBlacklist }
            sortingOrder = sorted.map { it.app.id }
            sorted
        } else {
            sortingOrder!!.mapNotNull { sorted -> find { sorted == it.app.id } }
        }
    }

    sealed interface State {

        object Loading : State
        data class Data(val apps: List<AppBlacklistSettingUiModel>) : State
        data class Error(val message: String) : State
    }

    sealed interface Action {

        data class AddToBlacklist(val appId: AppId): Action
        data class RemoveFromBlacklist(val appId: AppId): Action
        data class Search(val query: String): Action
    }
}
