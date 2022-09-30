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
import androidx.compose.material3.Button
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
import androidx.compose.ui.composed
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import org.koin.androidx.compose.koinViewModel
import shuttle.design.theme.Dimens
import shuttle.design.theme.ShuttleTheme
import shuttle.design.ui.LoadingSpinner
import shuttle.design.util.collectAsStateLifecycleAware
import shuttle.onboarding.presentation.model.OnboardingState
import shuttle.onboarding.presentation.ui.OnboardingPage.Index
import shuttle.onboarding.presentation.viewmodel.OnboardingViewModel
import shuttle.utils.kotlin.exhaustive
import studio.forface.shuttle.design.R

@Composable
fun OnboardingPage(actions: OnboardingPage.Actions) {
    val viewModel: OnboardingViewModel = koinViewModel()
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
        is OnboardingState.ShowOnboarding -> OnboardingContent(state = state, actions)
    }
}

@Composable
private fun OnboardingContent(
    state: OnboardingState.ShowOnboarding,
    actions: OnboardingPage.Actions
) {
    val index = remember { mutableStateOf(Index.MAIN) }
    val navigationActions = OnboardingPage.NavigationActions(
        onPrevious = { index -= 1 },
        onNext = { index += 1 },
        onComplete = actions.onOnboardingComplete
    )

    Crossfade(targetState = index.value) { indexState ->

        when (indexState) {
            Index.MAIN -> OnboardingMainPage(navigationActions)
            Index.WIDGET -> OnboardingWidgetPage(state.widgetPreview, navigationActions)
            Index.WIDGET_LAYOUT -> OnboardingWidgetLayoutPage(navigationActions)
            Index.BLACKLIST -> OnboardingBlacklistPage(state.blacklist, navigationActions)
        }.exhaustive
    }
}

@Composable
internal fun OnboardingPageContent(
    index: Index,
    @StringRes title: Int,
    image: @Composable () -> Unit,
    @StringRes description: Int,
    navigationActions: OnboardingPage.NavigationActions
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
            if (index.isFirst().not()) {
                Button(onClick = navigationActions.onPrevious) {
                    Text(text = stringResource(id = R.string.onboarding_action_previous))
                }
            } else {
                Box(modifier = Modifier)
            }
            if (index.isLast().not()) {
                Button(onClick = navigationActions.onNext) {
                    Text(text = stringResource(id = R.string.onboarding_action_next))
                }
            } else {
                Button(onClick = navigationActions.onComplete) {
                    Text(text = stringResource(id = R.string.onboarding_action_complete))
                }
            }
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

internal fun Modifier.imageContainerBackground() = composed {
    background(
        color = MaterialTheme.colorScheme.surface,
        shape = MaterialTheme.shapes.large
    )
}

object OnboardingPage {

    const val TEST_TAG = "OnboardingPage"

    enum class Index {
        MAIN, WIDGET, WIDGET_LAYOUT, BLACKLIST;

        fun intValue() = values().indexOf(this)
        fun isFirst() = intValue() == 0
        fun isLast() = intValue() == values().lastIndex
    }

    data class Actions(
        val onOnboardingComplete: () -> Unit
    )

    internal data class NavigationActions(
        val onPrevious: () -> Unit,
        val onNext: () -> Unit,
        val onComplete: () -> Unit
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

@Composable
@Preview(showSystemUi = true)
private fun OnboardingPagePreview() {
    val actions = OnboardingPage.Actions(onOnboardingComplete = {})
    ShuttleTheme {
        OnboardingPage(actions = actions)
    }
}
