package shuttle.settings.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import arrow.core.None
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import shuttle.apps.domain.model.AppId
import shuttle.icons.domain.usecase.ObserveInstalledIconPacks
import shuttle.settings.domain.usecase.ObserveCurrentIconPack
import shuttle.settings.domain.usecase.SetCurrentIconPack
import shuttle.settings.presentation.mapper.IconPackSettingsUiModelMapper
import shuttle.settings.presentation.model.IconPackSettingsUiModel
import shuttle.settings.presentation.viewmodel.IconPacksSettingsViewModel.Action
import shuttle.settings.presentation.viewmodel.IconPacksSettingsViewModel.State
import shuttle.util.android.viewmodel.ShuttleViewModel

internal class IconPacksSettingsViewModel(
    private val iconPackSettingsMapper: IconPackSettingsUiModelMapper,
    observeInstalledIconPacks: ObserveInstalledIconPacks,
    observeCurrentIconPack: ObserveCurrentIconPack,
    private val setCurrentIconPack: SetCurrentIconPack
) : ShuttleViewModel<Action, State>(initialState = State.Loading) {

    init {
        combine(
            observeInstalledIconPacks(),
            observeCurrentIconPack().onStart { emit(None) }
        ) { iconPacks, currentIconPack ->
            iconPackSettingsMapper.toUiModels(iconPacks, currentIconPack)
        }
        observeAppsBlacklistSettings()
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

    sealed interface State {

        object Loading : State
        data class Data(val apps: List<IconPackSettingsUiModel>) : State
        data class Error(val message: String) : State
    }

    sealed interface Action {

        data class SetCurrentIconPack(val appId: AppId): Action
    }
}
