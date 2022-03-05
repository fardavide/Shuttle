package shuttle.predictions.presentation.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import shuttle.predictions.domain.usecase.ObserveSuggestedApps
import shuttle.predictions.presentation.mapper.WidgetAppUiModelMapper
import shuttle.predictions.presentation.mapper.WidgetSettingsUiModelMapper
import shuttle.predictions.presentation.model.WidgetAppUiModel
import shuttle.predictions.presentation.model.WidgetSettingsUiModel
import shuttle.settings.domain.usecase.ObserveWidgetSettings

internal class SuggestedAppsWidgetViewModel(
    private val appUiModelMapper: WidgetAppUiModelMapper,
    observeSuggestedApps: ObserveSuggestedApps,
    observeWidgetSettings: ObserveWidgetSettings,
    private val widgetSettingsUiModelMapper: WidgetSettingsUiModelMapper,
    viewModelScope: CoroutineScope
) {

    val state: State = runBlocking(viewModelScope.coroutineContext) {
        combine(observeSuggestedApps(), observeWidgetSettings()) { suggestedAppsEither, widgetSettings ->
            suggestedAppsEither.fold(
                ifRight = { suggestedApps ->
                    State.Data(
                        apps = appUiModelMapper.toUiModels(suggestedApps),
                        widgetSettings = widgetSettingsUiModelMapper.toUiModel(widgetSettings)
                    )
                },
                ifLeft = { State.Error.Unknown }
            )
        }.first()
    }

    sealed interface State {

        data class Data(
            val apps: List<WidgetAppUiModel>,
            val widgetSettings: WidgetSettingsUiModel
        ) : State

        sealed interface Error : State {
            object Unknown : Error
        }
    }
}
