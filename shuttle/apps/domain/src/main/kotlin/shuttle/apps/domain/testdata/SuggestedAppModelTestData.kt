package shuttle.apps.domain.testdata

import shuttle.apps.domain.model.SuggestedAppModel
import shuttle.apps.domain.sample.AppNameSample

object SuggestedAppModelTestData {

    val Chrome = SuggestedAppModel(
        id = AppIdTestData.Chrome,
        name = AppNameSample.Chrome,
        isSuggested = true
    )
    val CineScout = SuggestedAppModel(
        id = AppIdTestData.CineScout,
        name = AppNameSample.CineScout,
        isSuggested = true
    )
    val GitHub = SuggestedAppModel(
        id = AppIdTestData.GitHub,
        name = AppNameSample.GitHub,
        isSuggested = true
    )
    val Gmail = SuggestedAppModel(
        id = AppIdTestData.Gmail,
        name = AppNameSample.Gmail,
        isSuggested = true
    )
    val MovieBase = SuggestedAppModel(
        id = AppIdTestData.MovieBase,
        name = AppNameSample.MovieBase,
        isSuggested = true
    )
    val Shuttle = SuggestedAppModel(
        id = AppIdTestData.Shuttle,
        name = AppNameSample.Shuttle,
        isSuggested = true
    )
    val Slack = SuggestedAppModel(
        id = AppIdTestData.Slack,
        name = AppNameSample.Slack,
        isSuggested = true
    )
    val Telegram = SuggestedAppModel(
        id = AppIdTestData.Telegram,
        name = AppNameSample.Telegram,
        isSuggested = true
    )
    val YouTube = SuggestedAppModel(
        id = AppIdTestData.YouTube,
        name = AppNameSample.YouTube,
        isSuggested = true
    )

    fun all() = listOf(
        Chrome,
        CineScout,
        GitHub,
        Gmail,
        MovieBase,
        Shuttle,
        Slack,
        Telegram,
        YouTube
    )
}

fun SuggestedAppModel.not() = copy(isSuggested = false)
