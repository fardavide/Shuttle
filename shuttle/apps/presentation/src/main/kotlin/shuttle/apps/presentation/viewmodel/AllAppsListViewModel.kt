package shuttle.apps.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import shuttle.apps.domain.AppsRepository
import shuttle.apps.domain.model.AppModel
import shuttle.apps.domain.usecase.GetAllInstalledApps
import shuttle.apps.presentation.model.AppUiModel
import shuttle.apps.presentation.util.GetIconForApp

internal class AllAppsListViewModel(
    private val getAllInstalledApps: GetAllInstalledApps,
    private val getIconForApp: GetIconForApp
) : ViewModel() {

    val state: StateFlow<State> =
        flow {
            val state = getAllInstalledApps().map(::toUiModels)
                .fold(
                    ifRight = State::Data,
                    ifLeft = { State.Error("Unknown") }
                )
            emit(state)
        }.stateIn(viewModelScope, SharingStarted.Eagerly, State.Loading)

    private fun toUiModels(appModels: Collection<AppModel>): List<AppUiModel> =
        appModels.map(::toUiModel)

    private fun toUiModel(appModel: AppModel) = AppUiModel(
        name = appModel.name.value,
        icon = getIconForApp(appModel.id)
    )

    sealed interface State {

        object Loading : State
        data class Data(val apps: List<AppUiModel>) : State
        data class Error(val message: String) : State
    }
}
