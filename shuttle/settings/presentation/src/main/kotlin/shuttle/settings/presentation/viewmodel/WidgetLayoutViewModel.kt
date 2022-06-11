package shuttle.settings.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import arrow.core.Either
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import shuttle.apps.domain.usecase.ObserveAllInstalledApps
import shuttle.design.model.WidgetLayoutUiModel
import shuttle.design.model.WidgetPreviewAppUiModel
import shuttle.icons.domain.error.GetSystemIconError
import shuttle.settings.domain.model.Dp
import shuttle.settings.domain.model.Sp
import shuttle.settings.domain.model.WidgetSettings
import shuttle.settings.domain.usecase.ObserveCurrentIconPack
import shuttle.settings.domain.usecase.ObserveWidgetSettings
import shuttle.settings.domain.usecase.UpdateWidgetSettings
import shuttle.settings.presentation.mapper.WidgetPreviewAppUiModelMapper
import shuttle.settings.presentation.mapper.WidgetSettingsUiModelMapper
import shuttle.settings.presentation.viewmodel.WidgetLayoutViewModel.Action
import shuttle.settings.presentation.viewmodel.WidgetLayoutViewModel.State
import shuttle.util.android.viewmodel.ShuttleViewModel

internal class WidgetLayoutViewModel(
    observeAllInstalledApps: ObserveAllInstalledApps,
    observeCurrentIconPack: ObserveCurrentIconPack,
    observeWidgetSettings: ObserveWidgetSettings,
    private val updateWidgetSettings: UpdateWidgetSettings,
    private val widgetPreviewAppUiModelMapper: WidgetPreviewAppUiModelMapper,
    private val widgetSettingsUiModelMapper: WidgetSettingsUiModelMapper,
) : ShuttleViewModel<Action, State>(initialState = State.Loading) {

    init {
        combine(
            observeAllInstalledApps(),
            observeCurrentIconPack(),
            observeWidgetSettings(),
        ) { installedApps, currentIconPack, widgetSettings ->
            State.Data(
                previewApps = widgetPreviewAppUiModelMapper
                    .toUiModels(installedApps, currentIconPack)
                    .filterRight()
                    .shuffled(),
                widgetSettingsDomainModel = widgetSettings,
                layout = widgetSettingsUiModelMapper.toUiModel(widgetSettings)
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
                is Action.UpdateAllowTwoLines -> updateAllowTwoLines(currentState, action.value)
            }
            emit(newState)
        }
    }

    private suspend fun updateRows(currentState: State.Data, value: Int): State {
        val newSettings = currentState.widgetSettingsDomainModel.copy(rowsCount = value)
        updateWidgetSettings(newSettings)
        return currentState.copy(
            widgetSettingsDomainModel = newSettings,
            layout = widgetSettingsUiModelMapper.toUiModel(newSettings)
        )
    }

    private suspend fun updateColumns(currentState: State.Data, value: Int): State {
        val newSettings = currentState.widgetSettingsDomainModel.copy(columnsCount = value)
        updateWidgetSettings(newSettings)
        return currentState.copy(
            widgetSettingsDomainModel = newSettings,
            layout = widgetSettingsUiModelMapper.toUiModel(newSettings)
        )
    }

    private suspend fun updateIconsSize(currentState: State.Data, value: Int): State {
        val newSettings = currentState.widgetSettingsDomainModel.copy(iconsSize = Dp(value))
        updateWidgetSettings(newSettings)
        return currentState.copy(
            widgetSettingsDomainModel = newSettings,
            layout = widgetSettingsUiModelMapper.toUiModel(newSettings)
        )
    }

    private suspend fun updateHorizontalSpacing(currentState: State.Data, value: Int): State {
        val newSettings = currentState.widgetSettingsDomainModel.copy(horizontalSpacing = Dp(value))
        updateWidgetSettings(newSettings)
        return currentState.copy(
            widgetSettingsDomainModel = newSettings,
            layout = widgetSettingsUiModelMapper.toUiModel(newSettings)
        )
    }

    private suspend fun updateVerticalSpacing(currentState: State.Data, value: Int): State {
        val newSettings = currentState.widgetSettingsDomainModel.copy(verticalSpacing = Dp(value))
        updateWidgetSettings(newSettings)
        return currentState.copy(
            widgetSettingsDomainModel = newSettings,
            layout = widgetSettingsUiModelMapper.toUiModel(newSettings)
        )
    }

    private suspend fun updateTextSize(currentState: State.Data, value: Int): State {
        val newSettings = currentState.widgetSettingsDomainModel.copy(textSize = Sp(value))
        updateWidgetSettings(newSettings)
        return currentState.copy(
            widgetSettingsDomainModel = newSettings,
            layout = widgetSettingsUiModelMapper.toUiModel(newSettings)
        )
    }

    private suspend fun updateAllowTwoLines(currentState: State.Data, value: Boolean): State {
        val newSettings = currentState.widgetSettingsDomainModel.copy(allowTwoLines = value)
        updateWidgetSettings(newSettings)
        return currentState.copy(
            widgetSettingsDomainModel = newSettings,
            layout = widgetSettingsUiModelMapper.toUiModel(newSettings)
        )
    }

    private fun List<Either<GetSystemIconError, WidgetPreviewAppUiModel>>.filterRight(): List<WidgetPreviewAppUiModel> =
        mapNotNull { it.orNull() }

    sealed interface State {

        object Loading : State
        data class Data(
            val previewApps: List<WidgetPreviewAppUiModel>,
            val widgetSettingsDomainModel: WidgetSettings,
            val layout: WidgetLayoutUiModel
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
        data class UpdateAllowTwoLines(val value: Boolean) : Action
    }
}
