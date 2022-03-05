package shuttle.settings.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import shuttle.apps.domain.model.AppId
import shuttle.settings.domain.model.Dp
import shuttle.settings.domain.model.Sp
import shuttle.settings.domain.model.WidgetSettings
import shuttle.settings.domain.usecase.ObserveWidgetSettings
import shuttle.settings.domain.usecase.UpdateWidgetSettings
import shuttle.settings.presentation.mapper.WidgetSettingsUiModelMapper
import shuttle.settings.presentation.model.WidgetSettingsUiModel
import shuttle.settings.presentation.viewmodel.WidgetSettingsViewModel.Action
import shuttle.settings.presentation.viewmodel.WidgetSettingsViewModel.State
import shuttle.util.android.viewmodel.ShuttleViewModel

internal class WidgetSettingsViewModel(
    observeWidgetSettings: ObserveWidgetSettings,
    private val updateWidgetSettings: UpdateWidgetSettings,
    private val widgetSettingsUiModelMapper: WidgetSettingsUiModelMapper,
) : ShuttleViewModel<Action, State>(initialState = State.Loading) {

    private var sortingOrder: List<AppId>? = null

    init {
        observeWidgetSettings()
            .map { widgetSettings ->
                State.Data(
                    domainModel = widgetSettings,
                    uiModel = widgetSettingsUiModelMapper.toUiModel(widgetSettings)
                )
            }
            .onEach(::emit)
            .launchIn(viewModelScope)
    }

    override fun submit(action: Action) {
        val currentState = state.value as? State.Data ?: return
        viewModelScope.launch {
            val newState = when (action) {
                is Action.UpdateRows -> updateRows(currentState, action.value)
                is Action.UpdateColumns -> updateColumns(currentState, action.value)
                is Action.UpdateIconSize -> updateIconSize(currentState, action.value)
                is Action.UpdateSpacing -> updateSpacing(currentState, action.value)
                is Action.UpdateTextSize -> updateTextSize(currentState, action.value)
            }
            emit(newState)
        }
    }

    private suspend fun updateRows(currentState: State.Data, value: Int): State {
        val newSettings = currentState.domainModel.copy(rowsCount = value)
        updateWidgetSettings(newSettings)
        return State.Data(domainModel = newSettings, uiModel = widgetSettingsUiModelMapper.toUiModel(newSettings))
    }

    private suspend fun updateColumns(currentState: State.Data, value: Int): State {
        val newSettings = currentState.domainModel.copy(columnsCount = value)
        updateWidgetSettings(newSettings)
        return State.Data(domainModel = newSettings, uiModel = widgetSettingsUiModelMapper.toUiModel(newSettings))
    }

    private suspend fun updateIconSize(currentState: State.Data, value: Int): State {
        val newSettings = currentState.domainModel.copy(iconSize = Dp(value))
        updateWidgetSettings(newSettings)
        return State.Data(domainModel = newSettings, uiModel = widgetSettingsUiModelMapper.toUiModel(newSettings))
    }

    private suspend fun updateSpacing(currentState: State.Data, value: Int): State {
        val newSettings = currentState.domainModel.copy(spacing = Dp(value))
        updateWidgetSettings(newSettings)
        return State.Data(domainModel = newSettings, uiModel = widgetSettingsUiModelMapper.toUiModel(newSettings))
    }

    private suspend fun updateTextSize(currentState: State.Data, value: Int): State {
        val newSettings = currentState.domainModel.copy(textSize = Sp(value))
        updateWidgetSettings(newSettings)
        return State.Data(domainModel = newSettings, uiModel = widgetSettingsUiModelMapper.toUiModel(newSettings))
    }

    sealed interface State {

        object Loading : State
        data class Data(
            val domainModel: WidgetSettings,
            val uiModel: WidgetSettingsUiModel
        ) : State
        data class Error(val message: String) : State
    }

    sealed interface Action {

        data class UpdateRows(val value: Int): Action
        data class UpdateColumns(val value: Int): Action
        data class UpdateIconSize(val value: Int): Action
        data class UpdateSpacing(val value: Int): Action
        data class UpdateTextSize(val value: Int): Action
    }
}
