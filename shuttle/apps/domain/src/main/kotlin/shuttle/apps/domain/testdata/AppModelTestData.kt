package shuttle.apps.domain.testdata

import shuttle.apps.domain.model.AppModel
import shuttle.apps.domain.sample.AppNameSample

object AppModelTestData {

    val Chrome = AppModel(id = AppIdTestData.Chrome, name = AppNameSample.Chrome)
    val CineScout = AppModel(id = AppIdTestData.CineScout, name = AppNameSample.CineScout)
    val GitHub = AppModel(id = AppIdTestData.GitHub, name = AppNameSample.GitHub)
    val Gmail = AppModel(id = AppIdTestData.Gmail, name = AppNameSample.Gmail)
    val MovieBase = AppModel(id = AppIdTestData.MovieBase, name = AppNameSample.MovieBase)
    val Shuttle = AppModel(id = AppIdTestData.Shuttle, name = AppNameSample.Shuttle)
    val Slack = AppModel(id = AppIdTestData.Slack, name = AppNameSample.Slack)
    val Telegram = AppModel(id = AppIdTestData.Telegram, name = AppNameSample.Telegram)
    val YouTube = AppModel(id = AppIdTestData.YouTube, name = AppNameSample.YouTube)

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
