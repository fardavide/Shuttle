package shuttle.onboarding.presentation.ui

import androidx.annotation.StringRes
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
    val index = remember { mutableStateOf(Index.MAIN) }
    val (onPrevious, onNext) = { index -= 1 } to { index += 1 }

    Crossfade(targetState = index.value) { indexState ->

        when (indexState) {
            Index.MAIN -> OnboardingMainPage(
                actions = OnboardingMainPage.Actions(
                    onNextPage = onNext
                )
            )
            Index.WIDGET -> OnboardingWidgetPage(
                state = state,
                actions = OnboardingWidgetPage.Actions(
                    onPreviousPage = onPrevious,
                    onNextPage = onNext
                )
            )
            Index.WIDGET_LAYOUT -> OnboardingWidgetLayoutPage(
                actions = OnboardingWidgetLayoutPage.Actions(
                    onPreviousPage = onPrevious,
                    onNextPage = actions.onOnboardingComplete
                )
            )
        }
    }
}

@Composable
internal fun OnboardingPageContent(
    index: Index,
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
            .padding(Dimens.Margin.XXLarge)
            .padding(vertical = Dimens.Margin.XXLarge),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Section(weight = 1) {
            Text(
                text = stringResource(id = title),
                style = MaterialTheme.typography.displaySmall,
            )
        }

        Section(weight = 2) {
            image()
        }

        Section(weight = 1) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(id = description),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }

        Section(weight = 1) {
            Slider(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = Dimens.Margin.XXLarge),
                value = Index.values().indexOf(index).toFloat(),
                valueRange = 0f..Index.values().lastIndex.toFloat(),
                steps = Index.values().size - 2,
                onValueChange = {}
            )
        }
        
        Section(weight = 1, horizontalArrangement = Arrangement.SpaceBetween) {
            previousButton()
            nextButton()
        }
    }
}

@Composable
private fun ColumnScope.Section(
    weight: Int,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Center,
    content: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .weight(weight.toFloat())
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = horizontalArrangement
    ) {
        content()
    }
}

object OnboardingPage {

    const val TEST_TAG = "OnboardingPage"

    enum class Index {
        MAIN, WIDGET, WIDGET_LAYOUT
    }

    data class Actions(
        val onOnboardingComplete: () -> Unit
    )
}

private operator fun MutableState<Index>.plusAssign(intValue: Int) {
    val newIntValue = value.intValue() + intValue
    value = Index.values()[newIntValue]
}

private operator fun MutableState<Index>.minusAssign(intValue: Int) {
    val newIntValue = value.intValue() - intValue
    value = Index.values()[newIntValue]
}

private fun Index.intValue() = Index.values().indexOf(this)

@Composable
@Preview(showSystemUi = true)
private fun OnboardingPagePreview() {
    val actions = OnboardingPage.Actions(onOnboardingComplete = {})
    ShuttleTheme {
        OnboardingPage(actions = actions)
    }
}
