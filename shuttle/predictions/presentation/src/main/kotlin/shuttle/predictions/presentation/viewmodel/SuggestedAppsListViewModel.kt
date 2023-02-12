package shuttle.predictions.presentation.viewmodel

import android.content.Intent
import android.content.pm.PackageManager
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import shuttle.apps.domain.model.AppId
import shuttle.icons.domain.error.GetSystemIconError
import shuttle.predictions.domain.error.ObserveSuggestedAppsError
import shuttle.predictions.domain.usecase.ObserveSuggestedApps
import shuttle.predictions.presentation.mapper.AppUiModelMapper
import shuttle.predictions.presentation.model.AppUiModel
import studio.forface.shuttle.design.R

internal class SuggestedAppsListViewModel(
    private val appUiModelMapper: AppUiModelMapper,
    observeSuggestedApps: ObserveSuggestedApps,
    private val packageManager: PackageManager
) : ViewModel() {

    val state: StateFlow<State>
        get() = mutableState
            .stateIn(viewModelScope, SharingStarted.Lazily, State.Loading)

    private val mutableState: MutableSharedFlow<State> =
        MutableSharedFlow(replay = 2)

    init {
        observeSuggestedApps().map { either ->
            either.map { appUiModelMapper.toUiModels(it) }
                .fold(
                    ifRight = { State.Data(it.filterRight()) },
                    ifLeft = { error -> State.Error(error.toMessage()) }
                )
        }.onEach { mutableState.emit(it) }
            .launchIn(viewModelScope)
    }

    fun submit(action: Action) {
        viewModelScope.launch {
            val newState = when (action) {
                is Action.OnAppClicked -> onAppClicked(action.appId)
            }
            mutableState.emit(newState)
        }
    }

    private fun onAppClicked(appId: AppId): State {
        val intent = packageManager.getLaunchIntentForPackage(appId.value)!!
        return State.RequestOpenApp(intent)
    }

    private fun List<Either<GetSystemIconError, AppUiModel>>.filterRight(): ImmutableList<AppUiModel> =
        mapNotNull { it.orNull() }.toImmutableList()

    private fun ObserveSuggestedAppsError.toMessage() = when (this) {
        ObserveSuggestedAppsError.LocationNotAvailable -> R.string.predictions_location_not_available
    }

    sealed interface State {

        object Loading : State
        data class Data(val apps: ImmutableList<AppUiModel>) : State
        data class Error(@StringRes val message: Int) : State
        data class RequestOpenApp(val intent: Intent) : State
    }

    sealed interface Action {

        data class OnAppClicked(val appId: AppId) : Action
    }
}
