package shuttle.predictions.domain.testdata

import com.soywiz.klock.Time
import shuttle.apps.domain.model.AppId
import shuttle.apps.domain.model.AppModel
import shuttle.apps.domain.model.AppName
import shuttle.coordinates.domain.model.CoordinatesResult
import shuttle.coordinates.domain.model.Location

object TestData {

    val CurrentLocation = Location(latitude = 10.0, longitude = 20.0)
    val AnotherLocation = Location(latitude = 21.0, longitude = 31.0)

    val CurrentTime = Time(hour = 14)
    val AnotherTime = Time(hour = 6)

    val TestConstraints = CoordinatesResult(
        location = CurrentLocation,
        time = CurrentTime
    )

    val ProtonMail = AppModel(AppId("proton.protonmail"), AppName("ProtonMail"))
    val Shuttle = AppModel(AppId("forface.shuttle"), AppName("Shuttle"))
    val Telegram = AppModel(AppId("telegram"), AppName("Telegram"))

    val AllApps = listOf(
        ProtonMail,
        Shuttle,
        Telegram
    )

}
