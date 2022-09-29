package shuttle.apps.domain.testdata

import shuttle.apps.domain.model.SuggestedAppModel

object SuggestedAppModelTestData {

    val Chrome = SuggestedAppModel(
        id = AppIdTestData.Chrome,
        name = AppNameTestData.Chrome,
        isSuggested = true
    )
    val CineScout = SuggestedAppModel(
        id = AppIdTestData.CineScout,
        name = AppNameTestData.CineScout,
        isSuggested = true
    )
    val GitHub = SuggestedAppModel(
        id = AppIdTestData.GitHub,
        name = AppNameTestData.GitHub,
        isSuggested = true
    )
    val Gmail = SuggestedAppModel(
        id = AppIdTestData.Gmail,
        name = AppNameTestData.Gmail,
        isSuggested = true
    )
    val MovieBase = SuggestedAppModel(
        id = AppIdTestData.MovieBase,
        name = AppNameTestData.MovieBase,
        isSuggested = true
    )
    val Shuttle = SuggestedAppModel(
        id = AppIdTestData.Shuttle,
        name = AppNameTestData.Shuttle,
        isSuggested = true
    )
    val Slack = SuggestedAppModel(
        id = AppIdTestData.Slack,
        name = AppNameTestData.Slack,
        isSuggested = true
    )
    val Telegram = SuggestedAppModel(
        id = AppIdTestData.Telegram,
        name = AppNameTestData.Telegram,
        isSuggested = true
    )
    val YouTube = SuggestedAppModel(
        id = AppIdTestData.YouTube,
        name = AppNameTestData.YouTube,
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
