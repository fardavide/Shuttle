package shuttle.settings.presentation.ui.page

import android.app.Activity
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import arrow.core.Either
import kotlinx.coroutines.launch
import shuttle.design.theme.Dimens
import shuttle.design.theme.ShuttleTheme
import shuttle.design.ui.BackIconButton
import shuttle.payments.domain.model.Product
import shuttle.payments.domain.model.PurchaseError
import shuttle.payments.domain.model.PurchaseSuccess
import shuttle.payments.presentation.launchPurchaseFlow
import studio.forface.shuttle.design.R.string

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AboutPage(onBack: () -> Unit) {

    val activity = LocalContext.current as Activity
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val uriHandler = LocalUriHandler.current

    val actionsStrings = ActionsStrings()

    val actions = Actions(
        toGitHubProject = { uriHandler.openUri(actionsStrings.gitHubProjectUrl) },
        toGitHubIssues = { uriHandler.openUri(actionsStrings.gitHubIssuesUrl) },
        toGitHubDev = { uriHandler.openUri(actionsStrings.gitHubDevUrl) },
        toTwitterDev = { uriHandler.openUri(actionsStrings.twitterDevUrl) },
        buyCoffee = { scope.launch { launchPurchaseFlow(activity, Product.Small).handle(snackbarHostState) } },
        buyMakeup = { scope.launch { launchPurchaseFlow(activity, Product.Large).handle(snackbarHostState) } }
    )

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text(stringResource(id = string.settings_about_title)) },
                navigationIcon = { BackIconButton(onBack) }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        AboutContent(actions, modifier = Modifier.padding(paddingValues))
    }
}

private suspend fun Either<PurchaseError, PurchaseSuccess>.handle(snackbarHostState: SnackbarHostState) {
    with(snackbarHostState) {
        tapLeft { showSnackbarIfNone("$it") }
    }
}

private suspend fun SnackbarHostState.showSnackbarIfNone(message: String) {
    if (currentSnackbarData == null) showSnackbar(message)
}

@Composable
private fun AboutContent(actions: Actions, modifier: Modifier) {
    LazyColumn(modifier = modifier, contentPadding = PaddingValues(Dimens.Margin.Medium)) {
        item { TitleText(textRes = string.settings_about_dev_info_title) }
        item { DescriptionText(textRes = string.settings_about_dev_info_description) }
        item { TitleText(textRes = string.settings_about_app_info_title) }
        item { DescriptionText(textRes = string.settings_about_app_info_description) }
        item { ClickableItem(textRes = string.settings_about_github_project_title, actions.toGitHubProject) }
        item { ClickableItem(textRes = string.settings_about_github_issues_title, actions.toGitHubIssues) }
        item { ClickableItem(textRes = string.settings_about_github_dev_title, actions.toGitHubDev) }
        item { ClickableItem(textRes = string.settings_about_twitter_dev_title, actions.toTwitterDev) }
        item { ClickableItem(textRes = string.settings_about_coffee_title, actions.buyCoffee) }
        item { ClickableItem(textRes = string.settings_about_makeup_title, actions.buyMakeup) }
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
    ElevatedButton(modifier = Modifier.padding(bottom = Dimens.Margin.Small), onClick = onClick) {
        Text(text = stringResource(id = textRes))
    }
}

@Composable
@Preview(showSystemUi = true)
private fun AboutContentPreview() {
    val actions = Actions(
        toGitHubProject = {},
        toGitHubIssues = {},
        toGitHubDev = {},
        toTwitterDev = {},
        buyCoffee = {},
        buyMakeup = {},
    )
    ShuttleTheme {
        AboutContent(actions, Modifier)
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
