package shuttle.settings.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import arrow.core.None
import arrow.core.Option
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import shuttle.apps.domain.model.AppId
import shuttle.icons.domain.usecase.ObserveInstalledIconPacks
import shuttle.settings.domain.usecase.ObserveCurrentIconPack
import shuttle.settings.domain.usecase.SetCurrentIconPack
import shuttle.settings.presentation.mapper.IconPackSettingsUiModelMapper
import shuttle.settings.presentation.model.IconPackSettingsItemUiModel
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
            val uiModels = iconPackSettingsMapper.toUiModels(iconPacks, currentIconPack)
            State.Data(uiModels)
        }
            .onEach(::emit)
            .launchIn(viewModelScope)
    }

    override fun submit(action: Action) {
        val currentState = state.value as? State.Data ?: return
        viewModelScope.launch {
            val newState = when (action) {
                is Action.SetCurrentIconPack -> setCurrentIconPack(currentState, action.iconPackId)
            }
            emit(newState)
        }
    }

    private suspend fun setCurrentIconPack(currentState: State.Data, iconPackId: Option<AppId>): State {
        val newData = currentState.iconPackSettingItems.map { uiModel ->
            when (uiModel) {
                is IconPackSettingsItemUiModel.SystemDefault -> uiModel.copy(isSelected = iconPackId.isEmpty())
                is IconPackSettingsItemUiModel.FromApp -> uiModel.copy(isSelected = uiModel.id == iconPackId.orNull())
            }
        }
        viewModelScope.launch {
            setCurrentIconPack(iconPackId)
        }
        return State.Data(newData)
    }

    sealed interface State {

        object Loading : State
        data class Data(val iconPackSettingItems: List<IconPackSettingsItemUiModel>) : State
    }

    sealed interface Action {

        data class SetCurrentIconPack(val iconPackId: Option<AppId>): Action
    }
}
