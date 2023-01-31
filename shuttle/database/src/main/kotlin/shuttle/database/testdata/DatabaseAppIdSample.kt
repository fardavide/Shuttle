package shuttle.database.testdata

import shuttle.database.model.DatabaseAppId

object DatabaseAppIdSample {

    val Chrome = DatabaseAppId("chrome")
    val CineScout = DatabaseAppId("cinescout")
    val Deliveroo = DatabaseAppId("deliveroo")
    val GitHub = DatabaseAppId("github")
    val Gmail = DatabaseAppId("gmail")
    val MovieBase = DatabaseAppId("moviebase")
    val Shuttle = DatabaseAppId("shuttle")
    val Slack = DatabaseAppId("slack")
    val Stocard = DatabaseAppId("stocard")
    val Telegram = DatabaseAppId("telegram")
    val YouTube = DatabaseAppId("youtube")

    fun all() = listOf(
        Chrome,
        CineScout,
        Deliveroo,
        GitHub,
        Gmail,
        MovieBase,
        Shuttle,
        Slack,
        Stocard,
        Telegram,
        YouTube
    )
}
