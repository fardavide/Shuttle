package shuttle.predictions.presentation.viewmodel

import arrow.core.Either
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import shuttle.icons.domain.error.GetSystemIconError
import shuttle.predictions.domain.usecase.ObserveSuggestedApps
import shuttle.predictions.presentation.mapper.WidgetAppUiModelMapper
import shuttle.predictions.presentation.mapper.WidgetSettingsUiModelMapper
import shuttle.predictions.presentation.model.SuggestedAppsState
import shuttle.predictions.presentation.model.WidgetAppUiModel
import shuttle.settings.domain.usecase.ObserveCurrentIconPack
import shuttle.settings.domain.usecase.ObserveWidgetSettings

internal class SuggestedAppsWidgetViewModel(
    private val appUiModelMapper: WidgetAppUiModelMapper,
    observeCurrentIconPack: ObserveCurrentIconPack,
    observeSuggestedApps: ObserveSuggestedApps,
    observeWidgetSettings: ObserveWidgetSettings,
    private val widgetSettingsUiModelMapper: WidgetSettingsUiModelMapper,
    viewModelScope: CoroutineScope
) {

    val state: StateFlow<SuggestedAppsState> = combine(
        observeCurrentIconPack(),
        observeSuggestedApps(),
        observeWidgetSettings()
    ) { currentIconPack, suggestedAppsEither, widgetSettings ->
        suggestedAppsEither.fold(
            ifRight = { suggestedApps ->
                SuggestedAppsState.Data(
                    apps = appUiModelMapper.toUiModels(suggestedApps, currentIconPack).filterRight(),
                    widgetSettings = widgetSettingsUiModelMapper.toUiModel(widgetSettings)
                )
            },
            ifLeft = SuggestedAppsState.Error::from
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(500),
        initialValue = SuggestedAppsState.Loading
    )

    private fun List<Either<GetSystemIconError, WidgetAppUiModel>>.filterRight(): List<WidgetAppUiModel> =
        mapNotNull { it.getOrNull() }
}
