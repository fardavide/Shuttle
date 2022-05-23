package shuttle.settings.presentation.ui.page

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import shuttle.design.theme.Dimens
import shuttle.design.theme.ShuttleTheme
import shuttle.design.ui.BackIconButton
import studio.forface.shuttle.design.R.string

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AboutPage(onBack: () -> Unit) {
    val uriHandler = LocalUriHandler.current
    val ghProjectUrl = stringResource(id = string.settings_about_github_project_link)
    val ghIssuesUrl = stringResource(id = string.settings_about_github_issues_link)
    val ghDevUrl = stringResource(id = string.settings_about_github_dev_link)
    val twitterDevUrl = stringResource(id = string.settings_about_twitter_dev_link)

    val actions = Actions(
        toGitHubProject = { uriHandler.openUri(ghProjectUrl) },
        toGitHubIssues = { uriHandler.openUri(ghIssuesUrl) },
        toGitHubDev = { uriHandler.openUri(ghDevUrl) },
        toTwitterDev = { uriHandler.openUri(twitterDevUrl) },
        buyCoffee = { TODO() },
        buyMakeup = { TODO() },
    )

    Scaffold(topBar = {
        SmallTopAppBar(
            title = { Text(stringResource(id = string.settings_about_title)) },
            navigationIcon = { BackIconButton(onBack) }
        )
    }) { paddingValues ->
        AboutContent(actions, modifier = Modifier.padding(paddingValues))
    }
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
        modifier = Modifier.padding(bottom = Dimens.Margin.Medium),
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
