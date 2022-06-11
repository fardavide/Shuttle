package shuttle.onboarding.presentation.viewmodel

import app.cash.turbine.FlowTurbine
import app.cash.turbine.test
import arrow.core.Either.Right
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import shuttle.apps.domain.usecase.ObserveAllInstalledApps
import shuttle.design.model.WidgetPreviewAppUiModel
import shuttle.design.model.WidgetPreviewUiModel
import shuttle.onboarding.domain.usecase.DidShowOnboarding
import shuttle.onboarding.presentation.mapper.WidgetPreviewAppUiModelMapper
import shuttle.onboarding.presentation.model.OnboardingState
import shuttle.onboarding.presentation.model.OnboardingWidgetPreviewState
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class OnboardingViewModelTest {

    private val didShowOnboarding: DidShowOnboarding = mockk()
    private val observeAllInstalledApps: ObserveAllInstalledApps = mockk()
    private val widgetPreviewAppUiModelMapper: WidgetPreviewAppUiModelMapper = mockk {
        coEvery { toUiModels(any()) } returns PreviewApps.map(::Right)
    }
    private val viewModel by lazy {
        OnboardingViewModel(
            didShowOnboarding = didShowOnboarding,
            observeAllInstalledApps = observeAllInstalledApps,
            widgetPreviewAppUiModelMapper = widgetPreviewAppUiModelMapper
        )
    }

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `emits loading state at the start`() = runTest {
        viewModel.state.test {
            assertEquals(OnboardingState.Loading, awaitItem())
        }
    }

    @Test
    fun `emits right value when onboarding has been already shown`() = runTest {
        // given
        coEvery { didShowOnboarding() } returns true

        // when
        viewModel.state.test {
            awaitLoading()

            // then
            assertEquals(OnboardingState.OnboardingAlreadyShown, awaitItem())
        }
    }

    @Test
    fun `emits right value when onboarding has not been shown and preview is loading`() = runTest {
        // given
        coEvery { didShowOnboarding() } returns false

        // when
        viewModel.state.test {
            awaitLoading()

            // then
            assertEquals(OnboardingState.ShowOnboarding(OnboardingWidgetPreviewState.Loading), awaitItem())
        }
    }

    @Test
    fun `emits right value when onboarding has not been shown and has loaded preview`() = runTest {
        // given
        coEvery { didShowOnboarding() } returns false
        every { observeAllInstalledApps() } returns flowOf(emptyList())

        // when
        viewModel.state.test {
            awaitLoading()
            awaitPreviewLoading()

            // then
            val previewUiModel = WidgetPreviewUiModel(PreviewApps, OnboardingViewModel.WidgetLayout)
            val previewState = OnboardingWidgetPreviewState.Data(previewUiModel)
            assertEquals(OnboardingState.ShowOnboarding(previewState), awaitItem().sortApps())
        }
    }

    private suspend fun FlowTurbine<OnboardingState>.awaitLoading() {
        assertEquals(OnboardingState.Loading, awaitItem())
    }

    private suspend fun FlowTurbine<OnboardingState>.awaitPreviewLoading() {
        assertEquals(OnboardingState.ShowOnboarding(OnboardingWidgetPreviewState.Loading), awaitItem())
    }

    private fun OnboardingState.sortApps(): OnboardingState {
        val showOnboardingState = this as? OnboardingState.ShowOnboarding
            ?: return this
        val widgetPreviewData = showOnboardingState.widgetPreview as? OnboardingWidgetPreviewState.Data
            ?: return this
        val widgetPreviewUiModel = widgetPreviewData.widgetPreview.copy(
            apps = widgetPreviewData.widgetPreview.apps.sortedBy { it.name }
        )
        return showOnboardingState.copy(widgetPreview = widgetPreviewData.copy(widgetPreview = widgetPreviewUiModel))
    }

    companion object TestData {

        private val PreviewApps = listOf(
            "Proton Drive",
            "Proton Mail",
            "Shuttle",
            "Telegram"
        ).map(::buildPreviewApp)

        private fun buildPreviewApp(name: String) = WidgetPreviewAppUiModel(
            name = name,
            icon = mockk()
        )
    }
}
