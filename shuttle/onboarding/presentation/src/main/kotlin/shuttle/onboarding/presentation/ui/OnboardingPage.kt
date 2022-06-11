package shuttle.onboarding.presentation.ui

import androidx.annotation.StringRes
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import org.koin.androidx.compose.viewModel
import shuttle.design.theme.Dimens
import shuttle.design.theme.ShuttleTheme
import shuttle.design.ui.LoadingSpinner
import shuttle.design.util.collectAsStateLifecycleAware
import shuttle.onboarding.presentation.model.OnboardingState
import shuttle.onboarding.presentation.model.OnboardingWidgetPreviewState
import shuttle.onboarding.presentation.ui.OnboardingPage.Index
import shuttle.onboarding.presentation.viewmodel.OnboardingViewModel

@Composable
fun OnboardingPage(actions: OnboardingPage.Actions) {
    val viewModel: OnboardingViewModel by viewModel()
    val stateWrapper = viewModel.state.collectAsStateLifecycleAware()

    @Suppress("NAME_SHADOWING")
    val actions = actions.copy(
        onOnboardingComplete = {
            viewModel.submit(OnboardingViewModel.Action.SetOnboardingShown)
            actions.onOnboardingComplete()
        }
    )

    when (val state = stateWrapper.value) {
        OnboardingState.Loading -> LoadingSpinner()
        OnboardingState.OnboardingAlreadyShown -> LaunchedEffect(key1 = 0) { actions.onOnboardingComplete() }
        is OnboardingState.ShowOnboarding -> OnboardingContent(state = state.widgetPreview, actions)
    }
}

@Composable
private fun OnboardingContent(
    state: OnboardingWidgetPreviewState,
    actions: OnboardingPage.Actions
) {
    var index by remember { mutableStateOf(Index.MAIN) }
    Crossfade(targetState = index) { indexState ->

        when (indexState) {
            Index.MAIN -> OnboardingMainPage(
                actions = OnboardingMainPage.Actions(
                    onNextPage = { index = Index.WIDGET }
                )
            )
            Index.WIDGET -> OnboardingWidgetPage(
                state = state,
                actions = OnboardingWidgetPage.Actions(
                    onPreviousPage = { index = Index.MAIN },
                    onNextPage = actions.onOnboardingComplete
                )
            )
        }
    }
}

@Composable
internal fun OnboardingPageContent(
    @StringRes title: Int,
    image: @Composable () -> Unit,
    @StringRes description: Int,
    previousButton: @Composable () -> Unit = { Box(Modifier) },
    nextButton: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .testTag(OnboardingPage.TEST_TAG)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(Dimens.Margin.XLarge),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(id = title),
            style = MaterialTheme.typography.displayMedium
        )

        image()

        Text(
            text = stringResource(id = description),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            previousButton()
            nextButton()
        }
    }
}

object OnboardingPage {

    const val TEST_TAG = "OnboardingPage"

    enum class Index {
        MAIN, WIDGET
    }

    data class Actions(
        val onOnboardingComplete: () -> Unit
    )
}

@Composable
@Preview(showSystemUi = true)
private fun OnboardingPagePreview() {
    val actions = OnboardingPage.Actions(onOnboardingComplete = {})
    ShuttleTheme {
        OnboardingPage(actions = actions)
    }
}
