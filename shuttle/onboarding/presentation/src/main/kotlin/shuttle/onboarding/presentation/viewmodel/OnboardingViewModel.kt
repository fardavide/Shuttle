package shuttle.onboarding.presentation.viewmodel

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import shuttle.apps.domain.usecase.ObserveAllInstalledApps
import shuttle.design.model.WidgetLayoutUiModel
import shuttle.design.model.WidgetPreviewAppUiModel
import shuttle.design.model.WidgetPreviewUiModel
import shuttle.icons.domain.error.GetSystemIconError
import shuttle.onboarding.presentation.mapper.WidgetPreviewAppUiModelMapper
import shuttle.onboarding.presentation.model.OnboardingState
import shuttle.util.android.viewmodel.ShuttleViewModel

internal class OnboardingViewModel(
    observeAllInstalledApps: ObserveAllInstalledApps,
    private val widgetPreviewAppUiModelMapper: WidgetPreviewAppUiModelMapper
) : ShuttleViewModel<OnboardingViewModel.Action, OnboardingState>(
    initialState = OnboardingState.Loading
) {

    init {
        observeAllInstalledApps().map { installedApps ->
            OnboardingState.Data(
                widgetPreview = WidgetPreviewUiModel(
                    apps = widgetPreviewAppUiModelMapper
                        .toUiModels(installedApps)
                        .filterRight()
                        .shuffled(),
                    layout = WidgetLayout
                )
            )
        }
            .onEach(::emit)
            .launchIn(viewModelScope)
    }

    override fun submit(action: Action) {

    }

    private fun List<Either<GetSystemIconError, WidgetPreviewAppUiModel>>.filterRight(): List<WidgetPreviewAppUiModel> =
        mapNotNull { it.orNull() }

    object Action

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
