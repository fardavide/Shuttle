package shuttle.onboarding.presentation.viewmodel

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import shuttle.apps.domain.model.AppModel
import shuttle.apps.domain.usecase.ObserveAllInstalledApps
import shuttle.design.model.WidgetLayoutUiModel
import shuttle.design.model.WidgetPreviewAppUiModel
import shuttle.design.model.WidgetPreviewUiModel
import shuttle.icons.domain.error.GetSystemIconError
import shuttle.onboarding.domain.usecase.DidShowOnboarding
import shuttle.onboarding.domain.usecase.SetOnboardingShown
import shuttle.onboarding.presentation.mapper.OnboardingBlacklistUiModelMapper
import shuttle.onboarding.presentation.mapper.WidgetPreviewAppUiModelMapper
import shuttle.onboarding.presentation.model.OnboardingBlacklistState
import shuttle.onboarding.presentation.model.OnboardingState
import shuttle.onboarding.presentation.model.OnboardingWidgetPreviewState
import shuttle.util.android.viewmodel.ShuttleViewModel

@KoinViewModel
internal class OnboardingViewModel(
    didShowOnboarding: DidShowOnboarding,
    observeAllInstalledApps: ObserveAllInstalledApps,
    private val onboardingBlacklistUiModelMapper: OnboardingBlacklistUiModelMapper,
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
                emit(OnboardingState.ShowOnboarding.Loading)
                observeAllInstalledApps().map { installedApps ->
                    OnboardingState.ShowOnboarding(
                        widgetPreview = buildOnboardingWidgetPreviewData(installedApps),
                        buildOnboardingBlacklistState(installedApps)
                    )
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

    private suspend fun buildOnboardingWidgetPreviewData(apps: List<AppModel>) = OnboardingWidgetPreviewState.Data(
        widgetPreview = WidgetPreviewUiModel(
            apps = widgetPreviewAppUiModelMapper
                .toUiModels(apps)
                .filterRight()
                .shuffled(),
            layout = WidgetLayout
        )
    )

    private suspend fun buildOnboardingBlacklistState(apps: List<AppModel>) = OnboardingBlacklistState.Data(
        onboardingBlacklistUiModelMapper.toUiModel(apps, take = 4)
    )

    private fun List<Either<GetSystemIconError, WidgetPreviewAppUiModel>>.filterRight(): List<WidgetPreviewAppUiModel> =
        mapNotNull { it.getOrNull() }

    sealed interface Action {

        object SetOnboardingShown : Action
    }

    companion object {

        val WidgetLayout = WidgetLayoutUiModel(
            allowTwoLines = true,
            columnsCount = 4,
            horizontalSpacing = 8.dp,
            iconSize = 48.dp,
            rowsCount = 2,
            textSize = 10.sp,
            transparency = 70,
            useMaterialColors = false,
            verticalSpacing = 8.dp
        )
    }
}
