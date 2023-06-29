package shuttle.widget.viewmodel

import arrow.core.Either
import arrow.core.Option
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.koin.core.annotation.Single
import shuttle.apps.domain.model.AppId
import shuttle.icons.domain.error.GetSystemIconError
import shuttle.predictions.domain.usecase.ObserveSuggestedApps
import shuttle.settings.domain.model.WidgetSettings
import shuttle.settings.domain.usecase.ObserveCurrentIconPack
import shuttle.settings.domain.usecase.ObserveWidgetSettings
import shuttle.utils.kotlin.takeOrFillWithNulls
import shuttle.widget.mapper.WidgetAppUiModelMapper
import shuttle.widget.mapper.WidgetSettingsUiModelMapper
import shuttle.widget.model.WidgetAppUiModel
import shuttle.widget.state.SuggestedAppsState

@Single
internal class SuggestedAppsWidgetViewModel(
    private val appUiModelMapper: WidgetAppUiModelMapper,
    private val observeCurrentIconPack: ObserveCurrentIconPack,
    private val observeSuggestedApps: ObserveSuggestedApps,
    private val observeWidgetSettings: ObserveWidgetSettings,
    private val widgetSettingsUiModelMapper: WidgetSettingsUiModelMapper,
    private val viewModelScope: CoroutineScope
) {

    suspend fun state(): StateFlow<SuggestedAppsState> = observeIconPackAndWidgetSettings()
        .flatMapConcat { (currentIconPack, widgetSettings) ->
            val takeAtLeast = widgetSettings.itemCount
            observeSuggestedApps(takeAtLeast).map { suggestedAppsEither ->
                suggestedAppsEither.fold(
                    ifRight = { suggestedApps ->
                        SuggestedAppsState.Data(
                            apps = appUiModelMapper
                                .toUiModels(suggestedApps, currentIconPack)
                                .filterRight()
                                .takeOrFillWithNulls(widgetSettings.itemCount)
                                .reversed(),
                            widgetSettings = widgetSettingsUiModelMapper.toUiModel(widgetSettings)
                        )
                    },
                    ifLeft = SuggestedAppsState.Error::from
                )
            }
        }.stateIn(scope = viewModelScope)

    private fun observeIconPackAndWidgetSettings(): Flow<Pair<Option<AppId>, WidgetSettings>> = combine(
        observeCurrentIconPack(),
        observeWidgetSettings(),
        ::Pair
    )

    private fun List<Either<GetSystemIconError, WidgetAppUiModel>>.filterRight(): List<WidgetAppUiModel> =
        mapNotNull { it.getOrNull() }
}
