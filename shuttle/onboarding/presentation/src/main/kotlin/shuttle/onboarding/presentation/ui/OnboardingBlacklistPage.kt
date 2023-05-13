package shuttle.onboarding.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import arrow.core.nonEmptyListOf
import shuttle.design.PreviewUtils
import shuttle.design.theme.Dimens
import shuttle.design.theme.ShuttleTheme
import shuttle.design.ui.CheckableListItem
import shuttle.design.ui.LoadingSpinner
import shuttle.design.util.NoContentDescription
import shuttle.onboarding.presentation.model.OnboardingBlacklistAppUiModel
import shuttle.onboarding.presentation.model.OnboardingBlacklistState
import shuttle.onboarding.presentation.model.OnboardingBlacklistUiModel
import shuttle.resources.R.string
import shuttle.resources.TextRes
import shuttle.utils.kotlin.exhaustive

@Composable
internal fun OnboardingBlacklistPage(state: OnboardingBlacklistState, actions: OnboardingPage.NavigationActions) {
    OnboardingPageContent(
        index = OnboardingPage.Index.BLACKLIST,
        title = string.onboarding_blacklist_title,
        content = {
            Box(modifier = Modifier.imageContainerBackground().padding(Dimens.Margin.Small)) {
                when (state) {
                    OnboardingBlacklistState.Loading -> LoadingSpinner()
                    is OnboardingBlacklistState.Data -> OnboardingBlacklistImage(uiModel = state.blacklist)
                }.exhaustive
            }
        },
        description = string.onboarding_blacklist_description,
        navigationActions = actions
    )
}

@Composable
private fun OnboardingBlacklistImage(uiModel: OnboardingBlacklistUiModel) {
    LazyColumn {
        items(uiModel.apps) { app ->
            CheckableListItem.LargeIcon(
                title = TextRes(app.name),
                iconDrawable = app.icon,
                contentDescription = NoContentDescription,
                isChecked = app.isBlacklisted,
                onCheckChange = {}
            )
        }
    }
}

@Composable
@Preview(showSystemUi = true)
private fun OnboardingBlacklistPagePreview() {
    val apps = listOf(
        OnboardingBlacklistAppUiModel("Shuttle", PreviewUtils.ShuttleIconDrawable, isBlacklisted = true),
        OnboardingBlacklistAppUiModel("Proton Mail", PreviewUtils.ShuttleIconDrawable, isBlacklisted = false),
        OnboardingBlacklistAppUiModel("Proton Drive", PreviewUtils.ShuttleIconDrawable, isBlacklisted = false)
    )
    val state = OnboardingBlacklistState.Data(
        blacklist = OnboardingBlacklistUiModel(
            apps = nonEmptyListOf(apps[0], apps[1], apps[2])
        )
    )
    val actions = OnboardingPage.NavigationActions(onPrevious = {}, onNext = {}, onComplete = {})
    ShuttleTheme {
        OnboardingBlacklistPage(state = state, actions = actions)
    }
}
