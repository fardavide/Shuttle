package shuttle.predictions.presentation.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import shuttle.predictions.domain.usecase.ObserveSuggestedApps
import shuttle.predictions.presentation.mapper.WidgetAppUiModelMapper
import shuttle.predictions.presentation.model.WidgetAppUiModel

internal class SuggestedAppsWidgetViewModel(
    private val appUiModelMapper: WidgetAppUiModelMapper,
    observeSuggestedApps: ObserveSuggestedApps,
    private val viewModelScope: CoroutineScope
) {

    val state: State = runBlocking(viewModelScope.coroutineContext) {
        observeSuggestedApps().map { either ->
            either.fold(
                ifRight = { list ->
                    val uiModels = appUiModelMapper.toUiModels(list)
                    State.Data(uiModels)
                },
                ifLeft = { State.Error.Unknown }
            )
        }.first()
    }

    sealed interface State {

        data class Data(val apps: List<WidgetAppUiModel>) : State
        sealed interface Error : State {
            object Unknown : Error
        }
    }

}
