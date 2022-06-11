package shuttle.onboarding.presentation.viewmodel

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import shuttle.apps.domain.usecase.ObserveAllInstalledApps
import shuttle.design.model.WidgetLayoutUiModel
import shuttle.design.model.WidgetPreviewAppUiModel
import shuttle.design.model.WidgetPreviewUiModel
import shuttle.icons.domain.error.GetSystemIconError
import shuttle.onboarding.domain.usecase.DidShowOnboarding
import shuttle.onboarding.domain.usecase.SetOnboardingShown
import shuttle.onboarding.presentation.mapper.WidgetPreviewAppUiModelMapper
import shuttle.onboarding.presentation.model.OnboardingState
import shuttle.onboarding.presentation.model.OnboardingWidgetPreviewState
import shuttle.util.android.viewmodel.ShuttleViewModel

internal class OnboardingViewModel(
    didShowOnboarding: DidShowOnboarding,
    observeAllInstalledApps: ObserveAllInstalledApps,
    private val setOnboardingShown: SetOnboardingShown,
    private val widgetPreviewAppUiModelMapper: WidgetPreviewAppUiModelMapper
) : ShuttleViewModel<OnboardingViewModel.Action, OnboardingState>(
    initialState = OnboardingState.Loading
) {

    init {
        viewModelScope.launch {
            if (didShowOnboarding()) {
                emit(OnboardingState.OnboardingAlreadyShown)
            } else {
                emit(OnboardingState.ShowOnboarding(OnboardingWidgetPreviewState.Loading))
                observeAllInstalledApps().map { installedApps ->
                    val widgetState = OnboardingWidgetPreviewState.Data(
                        widgetPreview = WidgetPreviewUiModel(
                            apps = widgetPreviewAppUiModelMapper
                                .toUiModels(installedApps)
                                .filterRight()
                                .shuffled(),
                            layout = WidgetLayout
                        )
                    )
                    OnboardingState.ShowOnboarding(widgetState)
                }.onEach(::emit).launchIn(this)
            }
        }
    }

    override fun submit(action: Action) {
        viewModelScope.launch {
            when (action) {
                Action.SetOnboardingShown -> onSetOnboardingShown()
            }
        }
    }

    private suspend fun onSetOnboardingShown() {
        setOnboardingShown()
    }

    private fun List<Either<GetSystemIconError, WidgetPreviewAppUiModel>>.filterRight(): List<WidgetPreviewAppUiModel> =
        mapNotNull { it.orNull() }

    sealed interface Action {

        object SetOnboardingShown : Action
    }

    companion object {

        val WidgetLayout = WidgetLayoutUiModel(
            rowsCount = 2,
            columnsCount = 4,
            iconSize = 48.dp,
            horizontalSpacing = 8.dp,
            verticalSpacing = 8.dp,
            textSize = 10.sp,
            allowTwoLines = true
        )
    }
}
