package shuttle.apps.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import shuttle.apps.domain.usecase.GetAllInstalledApps
import shuttle.apps.presentation.mapper.AppUiModelMapper
import shuttle.apps.presentation.model.AppUiModel

internal class AllAppsListViewModel(
    private val appUiModelMapper: AppUiModelMapper,
    private val getAllInstalledApps: GetAllInstalledApps
) : ViewModel() {

    val state: StateFlow<State> =
        flow {
            val state = getAllInstalledApps().map(appUiModelMapper::toUiModels)
                .fold(
                    ifRight = State::Data,
                    ifLeft = { State.Error("Unknown") }
                )
            emit(state)
        }.stateIn(viewModelScope, SharingStarted.Eagerly, State.Loading)

    sealed interface State {

        object Loading : State
        data class Data(val apps: List<AppUiModel>) : State
        data class Error(val message: String) : State
    }
}
