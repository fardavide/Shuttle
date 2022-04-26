package shuttle.predictions.presentation.viewmodel

import androidx.annotation.StringRes
import arrow.core.Either
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import shuttle.icons.domain.error.GetSystemIconError
import shuttle.predictions.domain.error.ObserveSuggestedAppsError
import shuttle.predictions.domain.usecase.ObserveSuggestedApps
import shuttle.predictions.presentation.mapper.WidgetAppUiModelMapper
import shuttle.predictions.presentation.mapper.WidgetSettingsUiModelMapper
import shuttle.predictions.presentation.model.WidgetAppUiModel
import shuttle.predictions.presentation.model.WidgetSettingsUiModel
import shuttle.settings.domain.usecase.ObserveCurrentIconPack
import shuttle.settings.domain.usecase.ObserveWidgetSettings
import studio.forface.shuttle.design.R

internal class SuggestedAppsWidgetViewModel(
    private val appUiModelMapper: WidgetAppUiModelMapper,
    observeCurrentIconPack: ObserveCurrentIconPack,
    observeSuggestedApps: ObserveSuggestedApps,
    observeWidgetSettings: ObserveWidgetSettings,
    private val widgetSettingsUiModelMapper: WidgetSettingsUiModelMapper,
    viewModelScope: CoroutineScope
) {

    val state: State = runBlocking(viewModelScope.coroutineContext) {
        combine(
            observeCurrentIconPack(),
            observeSuggestedApps(),
            observeWidgetSettings()
        ) { currentIconPack, suggestedAppsEither, widgetSettings ->
            suggestedAppsEither.fold(
                ifRight = { suggestedApps ->
                    State.Data(
                        apps = appUiModelMapper.toUiModels(suggestedApps, currentIconPack).filterRight(),
                        widgetSettings = widgetSettingsUiModelMapper.toUiModel(widgetSettings)
                    )
                },
                ifLeft = State.Error::from
            )
        }.first()
    }

    private fun List<Either<GetSystemIconError, WidgetAppUiModel>>.filterRight(): List<WidgetAppUiModel> =
        mapNotNull { it.orNull() }

    sealed interface State {

        data class Data(
            val apps: List<WidgetAppUiModel>,
            val widgetSettings: WidgetSettingsUiModel
        ) : State

        data class Error(@StringRes val message: Int) : State {

            companion object {

                fun from(error: ObserveSuggestedAppsError): Error {
                    val message = when (error) {
                        ObserveSuggestedAppsError.LocationNotAvailable -> R.string.predictions_location_not_available
                    }
                    return Error(message)
                }
            }
        }
    }
}
