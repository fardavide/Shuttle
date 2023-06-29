package shuttle.settings.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import arrow.core.Either
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import shuttle.apps.domain.usecase.ObserveAllInstalledApps
import shuttle.design.model.WidgetPreviewAppUiModel
import shuttle.icons.domain.error.GetSystemIconError
import shuttle.settings.domain.model.Dp
import shuttle.settings.domain.model.Sp
import shuttle.settings.domain.usecase.ObserveCurrentIconPack
import shuttle.settings.domain.usecase.ObserveWidgetSettings
import shuttle.settings.domain.usecase.UpdateWidgetSettings
import shuttle.settings.presentation.action.WidgetLayoutAction
import shuttle.settings.presentation.mapper.WidgetPreviewAppUiModelMapper
import shuttle.settings.presentation.mapper.WidgetSettingsUiModelMapper
import shuttle.settings.presentation.state.WidgetLayoutState
import shuttle.util.android.viewmodel.ShuttleViewModel

@KoinViewModel
internal class WidgetLayoutViewModel(
    observeAllInstalledApps: ObserveAllInstalledApps,
    observeCurrentIconPack: ObserveCurrentIconPack,
    observeWidgetSettings: ObserveWidgetSettings,
    private val updateWidgetSettings: UpdateWidgetSettings,
    private val widgetPreviewAppUiModelMapper: WidgetPreviewAppUiModelMapper,
    private val widgetSettingsUiModelMapper: WidgetSettingsUiModelMapper
) : ShuttleViewModel<WidgetLayoutAction, WidgetLayoutState>(initialState = WidgetLayoutState.Loading) {

    init {
        combine(
            observeAllInstalledApps(),
            observeCurrentIconPack(),
            observeWidgetSettings()
        ) { installedApps, currentIconPack, widgetSettings ->
            WidgetLayoutState.Data(
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

    override fun submit(action: WidgetLayoutAction) {
        val currentState = state.value as? WidgetLayoutState.Data ?: return
        viewModelScope.launch {
            val newState = when (action) {
                is WidgetLayoutAction.UpdateAllowTwoLines -> updateAllowTwoLines(currentState, action.value)
                is WidgetLayoutAction.UpdateColumns -> updateColumns(currentState, action.value)
                is WidgetLayoutAction.UpdateHorizontalSpacing -> updateHorizontalSpacing(currentState, action.value)
                is WidgetLayoutAction.UpdateIconsSize -> updateIconsSize(currentState, action.value)
                is WidgetLayoutAction.UpdateRows -> updateRows(currentState, action.value)
                is WidgetLayoutAction.UpdateTextSize -> updateTextSize(currentState, action.value)
                is WidgetLayoutAction.UpdateTransparency -> updateTransparency(currentState, action.value)
                is WidgetLayoutAction.UpdateUseMaterialColors -> updateUseMaterialColors(currentState, action.value)
                is WidgetLayoutAction.UpdateVerticalSpacing -> updateVerticalSpacing(currentState, action.value)
            }
            emit(newState)
        }
    }

    private suspend fun updateAllowTwoLines(
        currentState: WidgetLayoutState.Data,
        value: Boolean
    ): WidgetLayoutState {
        val newSettings = currentState.widgetSettingsDomainModel.copy(allowTwoLines = value)
        updateWidgetSettings(newSettings)
        return currentState.copy(
            widgetSettingsDomainModel = newSettings,
            layout = widgetSettingsUiModelMapper.toUiModel(newSettings)
        )
    }

    private suspend fun updateColumns(currentState: WidgetLayoutState.Data, value: Int): WidgetLayoutState {
        val newSettings = currentState.widgetSettingsDomainModel.copy(columnCount = value)
        updateWidgetSettings(newSettings)
        return currentState.copy(
            widgetSettingsDomainModel = newSettings,
            layout = widgetSettingsUiModelMapper.toUiModel(newSettings)
        )
    }

    private suspend fun updateHorizontalSpacing(
        currentState: WidgetLayoutState.Data,
        value: Int
    ): WidgetLayoutState {
        val newSettings = currentState.widgetSettingsDomainModel.copy(horizontalSpacing = Dp(value))
        updateWidgetSettings(newSettings)
        return currentState.copy(
            widgetSettingsDomainModel = newSettings,
            layout = widgetSettingsUiModelMapper.toUiModel(newSettings)
        )
    }

    private suspend fun updateIconsSize(currentState: WidgetLayoutState.Data, value: Int): WidgetLayoutState {
        val newSettings = currentState.widgetSettingsDomainModel.copy(iconsSize = Dp(value))
        updateWidgetSettings(newSettings)
        return currentState.copy(
            widgetSettingsDomainModel = newSettings,
            layout = widgetSettingsUiModelMapper.toUiModel(newSettings)
        )
    }

    private suspend fun updateRows(currentState: WidgetLayoutState.Data, value: Int): WidgetLayoutState {
        val newSettings = currentState.widgetSettingsDomainModel.copy(rowCount = value)
        updateWidgetSettings(newSettings)
        return currentState.copy(
            widgetSettingsDomainModel = newSettings,
            layout = widgetSettingsUiModelMapper.toUiModel(newSettings)
        )
    }

    private suspend fun updateTextSize(currentState: WidgetLayoutState.Data, value: Int): WidgetLayoutState {
        val newSettings = currentState.widgetSettingsDomainModel.copy(textSize = Sp(value))
        updateWidgetSettings(newSettings)
        return currentState.copy(
            widgetSettingsDomainModel = newSettings,
            layout = widgetSettingsUiModelMapper.toUiModel(newSettings)
        )
    }

    private suspend fun updateTransparency(
        currentState: WidgetLayoutState.Data,
        value: Int
    ): WidgetLayoutState {
        val newSettings = currentState.widgetSettingsDomainModel.copy(transparency = value)
        updateWidgetSettings(newSettings)
        return currentState.copy(
            widgetSettingsDomainModel = newSettings,
            layout = widgetSettingsUiModelMapper.toUiModel(newSettings)
        )
    }

    private suspend fun updateUseMaterialColors(
        currentState: WidgetLayoutState.Data,
        value: Boolean
    ): WidgetLayoutState {
        val newSettings = currentState.widgetSettingsDomainModel.copy(useMaterialColors = value)
        updateWidgetSettings(newSettings)
        return currentState.copy(
            widgetSettingsDomainModel = newSettings,
            layout = widgetSettingsUiModelMapper.toUiModel(newSettings)
        )
    }

    private suspend fun updateVerticalSpacing(
        currentState: WidgetLayoutState.Data,
        value: Int
    ): WidgetLayoutState {
        val newSettings = currentState.widgetSettingsDomainModel.copy(verticalSpacing = Dp(value))
        updateWidgetSettings(newSettings)
        return currentState.copy(
            widgetSettingsDomainModel = newSettings,
            layout = widgetSettingsUiModelMapper.toUiModel(newSettings)
        )
    }

    private fun List<Either<GetSystemIconError, WidgetPreviewAppUiModel>>.filterRight(): List<WidgetPreviewAppUiModel> =
        mapNotNull { it.getOrNull() }

}
