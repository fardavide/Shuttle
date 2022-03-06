package shuttle.apps.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import shuttle.apps.domain.usecase.ObserveAllInstalledApps
import shuttle.apps.presentation.mapper.AppUiModelMapper
import shuttle.apps.presentation.model.AppUiModel

internal class AllAppsListViewModel(
    private val appUiModelMapper: AppUiModelMapper,
    observeAllInstalledApps: ObserveAllInstalledApps
) : ViewModel() {

    val state: StateFlow<State> =
        observeAllInstalledApps().map { list ->
            State.Data(appUiModelMapper.toUiModels(list))
        }.stateIn(viewModelScope, SharingStarted.Eagerly, State.Loading)

    sealed interface State {

        object Loading : State
        data class Data(val apps: List<AppUiModel>) : State
        data class Error(val message: String) : State
    }
}
