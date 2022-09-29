package shuttle.apps.domain.testdata

import shuttle.apps.domain.model.AppModel

object AppModelTestData {

    val Chrome = AppModel(id = AppIdTestData.Chrome, name = AppNameTestData.Chrome)
    val CineScout = AppModel(id = AppIdTestData.CineScout, name = AppNameTestData.CineScout)
    val GitHub = AppModel(id = AppIdTestData.GitHub, name = AppNameTestData.GitHub)
    val Gmail = AppModel(id = AppIdTestData.Gmail, name = AppNameTestData.Gmail)
    val MovieBase = AppModel(id = AppIdTestData.MovieBase, name = AppNameTestData.MovieBase)
    val Shuttle = AppModel(id = AppIdTestData.Shuttle, name = AppNameTestData.Shuttle)
    val Slack = AppModel(id = AppIdTestData.Slack, name = AppNameTestData.Slack)
    val Telegram = AppModel(id = AppIdTestData.Telegram, name = AppNameTestData.Telegram)
    val YouTube = AppModel(id = AppIdTestData.YouTube, name = AppNameTestData.YouTube)

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
