package shuttle.widget.viewmodel

import arrow.core.Either
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import org.koin.core.annotation.Single
import shuttle.icons.domain.error.GetSystemIconError
import shuttle.predictions.domain.usecase.ObserveSuggestedApps
import shuttle.settings.domain.usecase.ObserveCurrentIconPack
import shuttle.settings.domain.usecase.ObserveWidgetSettings
import shuttle.widget.mapper.WidgetAppUiModelMapper
import shuttle.widget.mapper.WidgetSettingsUiModelMapper
import shuttle.widget.model.WidgetAppUiModel
import shuttle.widget.state.SuggestedAppsState

@Single
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
