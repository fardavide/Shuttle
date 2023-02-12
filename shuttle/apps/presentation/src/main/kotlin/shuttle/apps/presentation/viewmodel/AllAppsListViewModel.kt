package shuttle.apps.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import shuttle.apps.domain.usecase.ObserveAllInstalledApps
import shuttle.apps.presentation.mapper.AppUiModelMapper
import shuttle.apps.presentation.model.AppUiModel
import shuttle.icons.domain.error.GetSystemIconError

internal class AllAppsListViewModel(
    private val appUiModelMapper: AppUiModelMapper,
    observeAllInstalledApps: ObserveAllInstalledApps
) : ViewModel() {

    val state: StateFlow<State> =
        observeAllInstalledApps().map { list ->
            State.Data(appUiModelMapper.toUiModels(list).filterRight())
        }.stateIn(viewModelScope, SharingStarted.Eagerly, State.Loading)

    private fun List<Either<GetSystemIconError, AppUiModel>>.filterRight(): ImmutableList<AppUiModel> =
        mapNotNull { it.orNull() }.toImmutableList()

    sealed interface State {

        object Loading : State
        data class Data(val apps: ImmutableList<AppUiModel>) : State
        data class Error(val message: String) : State
    }
}
