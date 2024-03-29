package shuttle.onboarding.presentation.viewmodel

import app.cash.turbine.ReceiveTurbine
import app.cash.turbine.test
import arrow.core.Either.Right
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import shuttle.apps.domain.usecase.ObserveAllInstalledApps
import shuttle.design.model.WidgetPreviewAppUiModel
import shuttle.design.model.WidgetPreviewUiModel
import shuttle.onboarding.domain.usecase.DidShowOnboarding
import shuttle.onboarding.presentation.mapper.OnboardingBlacklistUiModelMapper
import shuttle.onboarding.presentation.mapper.WidgetPreviewAppUiModelMapper
import shuttle.onboarding.presentation.model.OnboardingBlacklistAppUiModel
import shuttle.onboarding.presentation.model.OnboardingBlacklistState
import shuttle.onboarding.presentation.model.OnboardingBlacklistUiModel
import shuttle.onboarding.presentation.model.OnboardingState
import shuttle.onboarding.presentation.model.OnboardingWidgetPreviewState

class OnboardingViewModelTest : AnnotationSpec() {

    private val didShowOnboarding: DidShowOnboarding = mockk {
        coEvery { this@mockk() } returns true
    }
    private val observeAllInstalledApps: ObserveAllInstalledApps = mockk {
        every { this@mockk() } returns emptyFlow()
    }
    private val onboardingBlacklistUiModelMapper: OnboardingBlacklistUiModelMapper = mockk {
        coEvery { toUiModel(apps = any(), take = any()) } returns BlacklistApps
    }
    private val widgetPreviewAppUiModelMapper: WidgetPreviewAppUiModelMapper = mockk {
        coEvery { toUiModels(any()) } returns PreviewApps.map(::Right)
    }
    private val viewModel get() = OnboardingViewModel(
        didShowOnboarding = didShowOnboarding,
        observeAllInstalledApps = observeAllInstalledApps,
        onboardingBlacklistUiModelMapper = onboardingBlacklistUiModelMapper,
        setOnboardingShown = mockk(relaxUnitFun = true),
        widgetPreviewAppUiModelMapper = widgetPreviewAppUiModelMapper
    )

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `emits loading state at the start`() = runTest {
        viewModel.state.test {
            awaitItem() shouldBe OnboardingState.Loading
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
            awaitItem() shouldBe OnboardingState.OnboardingAlreadyShown
        }
    }

    @Test
    fun `emits right value when onboarding has not been shown and apps are loading`() = runTest {
        // given
        coEvery { didShowOnboarding() } returns false

        // when
        viewModel.state.test {
            awaitLoading()

            // then
            awaitItem() shouldBe OnboardingState.ShowOnboarding.Loading
        }
    }

    @Test
    fun `emits right value when onboarding has not been shown and has loaded apps`() = runTest {
        // given
        val expected = run {
            val previewUiModel = WidgetPreviewUiModel(PreviewApps, OnboardingViewModel.WidgetLayout)
            val previewState = OnboardingWidgetPreviewState.Data(previewUiModel)
            val blacklistState = OnboardingBlacklistState.Data(BlacklistApps)
            OnboardingState.ShowOnboarding(previewState, blacklistState)
        }
        coEvery { didShowOnboarding() } returns false
        every { observeAllInstalledApps() } returns flowOf(emptyList())

        // when
        viewModel.state.test {
            awaitLoading()
            awaitPreviewLoading()

            // then
            awaitItem().sortApps() shouldBe expected
        }
    }

    private suspend fun ReceiveTurbine<OnboardingState>.awaitLoading() {
        awaitItem() shouldBe OnboardingState.Loading
    }

    private suspend fun ReceiveTurbine<OnboardingState>.awaitPreviewLoading() {
        awaitItem() shouldBe OnboardingState.ShowOnboarding.Loading
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

        val appsList = listOf(
            "Proton Drive",
            "Proton Mail",
            "Shuttle",
            "Telegram"
        )
        private val PreviewApps = appsList.map(::buildPreviewApp)
        private val BlacklistApps = OnboardingBlacklistUiModel(appsList.map(::buildBlacklistApp))

        private fun buildPreviewApp(name: String) = WidgetPreviewAppUiModel(
            name = name,
            icon = mockk()
        )

        private fun buildBlacklistApp(name: String) = OnboardingBlacklistAppUiModel(
            name = name,
            icon = mockk(),
            isBlacklisted = true
        )
    }
}
