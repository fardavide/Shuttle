package shuttle.settings.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import shuttle.apps.domain.usecase.ObserveAllInstalledApps
import shuttle.settings.domain.model.Dp
import shuttle.settings.domain.model.Sp
import shuttle.settings.domain.model.WidgetSettings
import shuttle.settings.domain.usecase.ObserveWidgetSettings
import shuttle.settings.domain.usecase.UpdateWidgetSettings
import shuttle.settings.presentation.mapper.WidgetPreviewAppUiModelMapper
import shuttle.settings.presentation.mapper.WidgetSettingsUiModelMapper
import shuttle.settings.presentation.model.WidgetPreviewAppUiModel
import shuttle.settings.presentation.model.WidgetSettingsUiModel
import shuttle.settings.presentation.viewmodel.WidgetSettingsViewModel.Action
import shuttle.settings.presentation.viewmodel.WidgetSettingsViewModel.State
import shuttle.util.android.viewmodel.ShuttleViewModel

internal class WidgetSettingsViewModel(
    observeAllInstalledApps: ObserveAllInstalledApps,
    observeWidgetSettings: ObserveWidgetSettings,
    private val updateWidgetSettings: UpdateWidgetSettings,
    private val widgetPreviewAppUiModelMapper: WidgetPreviewAppUiModelMapper,
    private val widgetSettingsUiModelMapper: WidgetSettingsUiModelMapper,
) : ShuttleViewModel<Action, State>(initialState = State.Loading) {

    init {
        combine(observeWidgetSettings(), observeAllInstalledApps()) { widgetSettings, installedAppsEither ->
            installedAppsEither.fold(
                ifRight = { installedApps ->
                    State.Data(
                        previewApps = widgetPreviewAppUiModelMapper.toUiModels(installedApps).shuffled(),
                        widgetSettingsDomainModel = widgetSettings,
                        widgetSettingsUiModel = widgetSettingsUiModelMapper.toUiModel(widgetSettings)
                    )
                },
                ifLeft = { State.Error("Unknown error") }
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
                is Action.UpdateIconsSize -> updateIconsSize(currentState, action.value)
                is Action.UpdateVerticalSpacing -> updateVerticalSpacing(currentState, action.value)
                is Action.UpdateHorizontalSpacing -> updateHorizontalSpacing(currentState, action.value)
                is Action.UpdateTextSize -> updateTextSize(currentState, action.value)
            }
            emit(newState)
        }
    }

    private suspend fun updateRows(currentState: State.Data, value: Int): State {
        val newSettings = currentState.widgetSettingsDomainModel.copy(rowsCount = value)
        updateWidgetSettings(newSettings)
        return currentState.copy(
            widgetSettingsDomainModel = newSettings,
            widgetSettingsUiModel = widgetSettingsUiModelMapper.toUiModel(newSettings)
        )
    }

    private suspend fun updateColumns(currentState: State.Data, value: Int): State {
        val newSettings = currentState.widgetSettingsDomainModel.copy(columnsCount = value)
        updateWidgetSettings(newSettings)
        return currentState.copy(
            widgetSettingsDomainModel = newSettings,
            widgetSettingsUiModel = widgetSettingsUiModelMapper.toUiModel(newSettings)
        )
    }

    private suspend fun updateIconsSize(currentState: State.Data, value: Int): State {
        val newSettings = currentState.widgetSettingsDomainModel.copy(iconsSize = Dp(value))
        updateWidgetSettings(newSettings)
        return currentState.copy(
            widgetSettingsDomainModel = newSettings,
            widgetSettingsUiModel = widgetSettingsUiModelMapper.toUiModel(newSettings)
        )
    }

    private suspend fun updateHorizontalSpacing(currentState: State.Data, value: Int): State {
        val newSettings = currentState.widgetSettingsDomainModel.copy(horizontalSpacing = Dp(value))
        updateWidgetSettings(newSettings)
        return currentState.copy(
            widgetSettingsDomainModel = newSettings,
            widgetSettingsUiModel = widgetSettingsUiModelMapper.toUiModel(newSettings)
        )
    }

    private suspend fun updateVerticalSpacing(currentState: State.Data, value: Int): State {
        val newSettings = currentState.widgetSettingsDomainModel.copy(verticalSpacing = Dp(value))
        updateWidgetSettings(newSettings)
        return currentState.copy(
            widgetSettingsDomainModel = newSettings,
            widgetSettingsUiModel = widgetSettingsUiModelMapper.toUiModel(newSettings)
        )
    }

    private suspend fun updateTextSize(currentState: State.Data, value: Int): State {
        val newSettings = currentState.widgetSettingsDomainModel.copy(textSize = Sp(value))
        updateWidgetSettings(newSettings)
        return currentState.copy(
            widgetSettingsDomainModel = newSettings,
            widgetSettingsUiModel = widgetSettingsUiModelMapper.toUiModel(newSettings)
        )
    }

    sealed interface State {

        object Loading : State
        data class Data(
            val previewApps: List<WidgetPreviewAppUiModel>,
            val widgetSettingsDomainModel: WidgetSettings,
            val widgetSettingsUiModel: WidgetSettingsUiModel
        ) : State

        data class Error(val message: String) : State
    }

    sealed interface Action {

        data class UpdateRows(val value: Int) : Action
        data class UpdateColumns(val value: Int) : Action
        data class UpdateIconsSize(val value: Int) : Action
        data class UpdateHorizontalSpacing(val value: Int) : Action
        data class UpdateVerticalSpacing(val value: Int) : Action
        data class UpdateTextSize(val value: Int) : Action
    }
}
