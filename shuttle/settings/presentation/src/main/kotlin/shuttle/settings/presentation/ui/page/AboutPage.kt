package shuttle.settings.presentation.ui.page

import android.app.Activity
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import arrow.core.Either
import org.koin.androidx.compose.koinViewModel
import shuttle.design.R.string
import shuttle.design.theme.Dimens
import shuttle.design.theme.ShuttleTheme
import shuttle.design.ui.BackIconButton
import shuttle.design.ui.LoadingSpinner
import shuttle.design.ui.TextError
import shuttle.design.util.ConsumableLaunchedEffect
import shuttle.design.util.Effect
import shuttle.design.util.collectAsStateLifecycleAware
import shuttle.payments.domain.model.PaymentError
import shuttle.payments.domain.model.Product
import shuttle.payments.domain.model.PurchaseSuccess
import shuttle.settings.presentation.viewmodel.AboutViewModel
import shuttle.settings.presentation.viewmodel.AboutViewModel.Action
import shuttle.settings.presentation.viewmodel.AboutViewModel.State

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AboutPage(onBack: () -> Unit) {

    val activity = LocalContext.current as Activity
    val snackbarHostState = remember { SnackbarHostState() }
    val viewModel: AboutViewModel = koinViewModel()
    val uriHandler = LocalUriHandler.current

    val actionsStrings = ActionsStrings()

    val actions = Actions(
        toGitHubProject = { uriHandler.openUri(actionsStrings.gitHubProjectUrl) },
        toGitHubIssues = { uriHandler.openUri(actionsStrings.gitHubIssuesUrl) },
        toGitHubDev = { uriHandler.openUri(actionsStrings.gitHubDevUrl) },
        toTwitterDev = { uriHandler.openUri(actionsStrings.twitterDevUrl) },
        buyCoffee = { viewModel.submit(Action.LaunchPurchase(activity, Product.Small)) },
        buyMakeup = { viewModel.submit(Action.LaunchPurchase(activity, Product.Large)) }
    )
    val state = viewModel.state.collectAsStateLifecycleAware()

    Scaffold(
        modifier = Modifier.statusBarsPadding().navigationBarsPadding(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = string.settings_about_title)) },
                navigationIcon = { BackIconButton(onBack) }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        when (val s = state.value) {
            is State.Data -> AboutContent(
                modifier = Modifier.padding(paddingValues),
                state = s,
                actions = actions,
                snackbarHostState = snackbarHostState
            )
            State.Loading -> LoadingSpinner()
            State.Error -> TextError(text = stringResource(id = string.x_generic_error))
        }
    }
}

private suspend fun Either<PaymentError, PurchaseSuccess>.handle(snackbarHostState: SnackbarHostState) {
    with(snackbarHostState) {
        onLeft { showSnackbarIfNone("$it") }
    }
}

private suspend fun SnackbarHostState.showSnackbarIfNone(message: String) {
    if (currentSnackbarData == null) showSnackbar(message)
}

@Composable
private fun AboutContent(
    state: State.Data,
    actions: Actions,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    ConsumableLaunchedEffect(effect = state.purchaseResult) { result ->
        result.handle(snackbarHostState)
    }

    LazyColumn(modifier = modifier, contentPadding = PaddingValues(Dimens.Margin.Medium)) {
        item { TitleText(textRes = string.settings_about_dev_info_title) }
        item { DescriptionText(textRes = string.settings_about_dev_info_description) }
        item { TitleText(textRes = string.settings_about_app_info_title) }
        item { DescriptionText(textRes = string.settings_about_app_info_description) }
        item { ClickableItem(textRes = string.settings_about_github_project_title, actions.toGitHubProject) }
        item { ClickableItem(textRes = string.settings_about_github_issues_title, actions.toGitHubIssues) }
        item { ClickableItem(textRes = string.settings_about_github_dev_title, actions.toGitHubDev) }
        item { ClickableItem(textRes = string.settings_about_twitter_dev_title, actions.toTwitterDev) }
        item {
            ClickableItem(
                text = stringResource(id = string.settings_about_coffee_title, state.smallProductFormattedPrice),
                onClick = actions.buyCoffee
            )
        }
        item {
            ClickableItem(
                text = stringResource(id = string.settings_about_makeup_title, state.largeProductFormattedPrice),
                onClick = actions.buyMakeup
            )
        }
    }
}

@Composable
private fun TitleText(@StringRes textRes: Int) {
    Text(
        modifier = Modifier.padding(bottom = Dimens.Margin.Small),
        text = stringResource(id = textRes),
        style = MaterialTheme.typography.displaySmall
    )
}

@Composable
private fun DescriptionText(@StringRes textRes: Int) {
    Text(
        modifier = Modifier.padding(bottom = Dimens.Margin.XLarge),
        text = stringResource(id = textRes),
        style = MaterialTheme.typography.bodyLarge
    )
}

@Composable
private fun ClickableItem(@StringRes textRes: Int, onClick: () -> Unit) {
    ClickableItem(text = stringResource(id = textRes), onClick = onClick)
}

@Composable
private fun ClickableItem(text: String, onClick: () -> Unit) {
    ElevatedButton(modifier = Modifier.padding(bottom = Dimens.Margin.Small), onClick = onClick) {
        Text(text = text)
    }
}

@Composable
@Preview(showSystemUi = true)
private fun AboutContentPreview() {
    val state = State.Data(
        smallProductFormattedPrice = "1.19 $",
        largeProductFormattedPrice = "5.49 $",
        purchaseResult = Effect.empty()
    )
    val actions = Actions(
        toGitHubProject = {},
        toGitHubIssues = {},
        toGitHubDev = {},
        toTwitterDev = {},
        buyCoffee = {},
        buyMakeup = {}
    )
    ShuttleTheme {
        AboutContent(
            modifier = Modifier,
            state = state,
            actions = actions,
            snackbarHostState = SnackbarHostState()
        )
    }
}

private class Actions(
    val toGitHubProject: () -> Unit,
    val toGitHubIssues: () -> Unit,
    val toGitHubDev: () -> Unit,
    val toTwitterDev: () -> Unit,
    val buyCoffee: () -> Unit,
    val buyMakeup: () -> Unit
)

private class ActionsStrings(
    val gitHubProjectUrl: String,
    val gitHubIssuesUrl: String,
    val gitHubDevUrl: String,
    val twitterDevUrl: String
) {

    companion object {

        @Composable
        operator fun invoke() = ActionsStrings(
            gitHubProjectUrl = stringResource(id = string.settings_about_github_project_link),
            gitHubIssuesUrl = stringResource(id = string.settings_about_github_issues_link),
            gitHubDevUrl = stringResource(id = string.settings_about_github_dev_link),
            twitterDevUrl = stringResource(id = string.settings_about_twitter_dev_link)
        )
    }
}
