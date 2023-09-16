package shuttle.settings.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.google.accompanist.permissions.MultiplePermissionsState
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import shuttle.accessibility.usecase.IsLaunchCounterServiceEnabled
import shuttle.design.util.Effect
import shuttle.permissions.domain.usecase.HasAllLocationPermissions
import shuttle.settings.domain.usecase.ObserveDidShowConsents
import shuttle.settings.domain.usecase.ObserveUseExperimentalAppSorting
import shuttle.settings.domain.usecase.ResetOnboardingShown
import shuttle.settings.domain.usecase.SetConsentsShown
import shuttle.settings.domain.usecase.SetIsDataCollectionEnabled
import shuttle.settings.domain.usecase.SetUseExperimentalAppSorting
import shuttle.settings.presentation.action.SettingsAction
import shuttle.settings.presentation.state.SettingsState
import shuttle.util.android.viewmodel.ShuttleViewModel
import shuttle.utils.kotlin.GetAppVersion

@KoinViewModel
class SettingsViewModel(
    private val getAppVersion: GetAppVersion,
    private val hasAllLocationPermissions: HasAllLocationPermissions,
    private val isLaunchCounterServiceEnabled: IsLaunchCounterServiceEnabled,
    private val observeDidShowConsents: ObserveDidShowConsents,
    private val observeUseExperimentalAppSorting: ObserveUseExperimentalAppSorting,
    private val resetOnboardingShown: ResetOnboardingShown,
    private val setConsentsShown: SetConsentsShown,
    private val setIsDataCollectionEnabled: SetIsDataCollectionEnabled,
    private val setUseExperimentalAppSorting: SetUseExperimentalAppSorting
) : ShuttleViewModel<SettingsAction, SettingsState>(initialState = SettingsState.Loading) {

    init {
        viewModelScope.launch {
            val appVersion = getAppVersion().toString()
            update { state -> state.copy(appVersion = appVersion) }
        }
        viewModelScope.launch {
            observeDidShowConsents().collect { didShowConsents ->
                update { state -> state.copy(shouldShowConsents = !didShowConsents) }
            }
        }
        viewModelScope.launch {
            observeUseExperimentalAppSorting().collect { useExperimentalAppSorting ->
                update { state ->
                    state.copy(
                        shouldShowStatisticsItem = useExperimentalAppSorting.not(),
                        useExperimentalAppSorting = useExperimentalAppSorting
                    )
                }
            }
        }
    }

    override fun submit(action: SettingsAction) {
        viewModelScope.launch {
            val newState = when (action) {
                SettingsAction.ResetOnboardingShown -> onResetOnboardingShown()
                SettingsAction.SetConsentsShown -> onSetConsentsShown()
                is SettingsAction.SetIsDataCollectionEnabled -> onSetIsDataCollectionEnabled(action.enable)
                is SettingsAction.ToggleExperimentalAppSorting -> onUseExperimentalAppSortingChanged(action.enable)
                is SettingsAction.UpdatePermissionsState -> onPermissionsStateUpdate(action.permissionsState)
            }
            emit(newState)
        }
    }

    private fun onPermissionsStateUpdate(permissionsState: MultiplePermissionsState): SettingsState {
        val hasPermissions = hasAllLocationPermissions(permissionsState) && isLaunchCounterServiceEnabled()
        val permissions = if (hasPermissions) SettingsState.Permissions.Granted else SettingsState.Permissions.Denied

        return state.value.copy(permissions = permissions)
    }

    private suspend fun onResetOnboardingShown(): SettingsState {
        resetOnboardingShown()
        return state.value.copy(openOnboardingEffect = Effect.of(Unit))
    }

    private suspend fun onSetConsentsShown(): SettingsState {
        setConsentsShown()
        return state.value.copy(shouldShowConsents = false)
    }

    private suspend fun onSetIsDataCollectionEnabled(enable: Boolean): SettingsState {
        setIsDataCollectionEnabled(enable)
        return state.value
    }

    private suspend fun onUseExperimentalAppSortingChanged(enable: Boolean): SettingsState {
        setUseExperimentalAppSorting(enable)
        return state.value.copy(
            shouldShowStatisticsItem = enable.not(),
            useExperimentalAppSorting = enable
        )
    }
}
