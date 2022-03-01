package shuttle.database.testdata

import shuttle.database.model.DatabaseAppId
import shuttle.database.model.DatabaseLatitude
import shuttle.database.model.DatabaseLocation
import shuttle.database.model.DatabaseLongitude
import shuttle.database.model.DatabaseTime

object TestData {

    val ExactTime = DatabaseTime(100)
    val RangeStartTime = DatabaseTime(90)
    val RangeMidFirstTime = DatabaseTime(92)
    val RangeMidSecondTime = DatabaseTime(94)
    val RangeEndTime = DatabaseTime(110)
    val ExactLatitude = DatabaseLatitude(20.0)
    val RangeStartLatitude = DatabaseLatitude(10.0)
    val RangeEndLatitude = DatabaseLatitude(30.0)
    val ExactLongitude = DatabaseLongitude(30.0)
    val RangeStartLongitude = DatabaseLongitude(20.0)
    val RangeEndLongitude = DatabaseLongitude(40.0)
    val ExactLocation = DatabaseLocation(ExactLatitude, ExactLongitude)

    val FirstAppId = DatabaseAppId("app 1")
    val SecondAppId = DatabaseAppId("app 2")
    val ThirdAppId = DatabaseAppId("app 3")
    val FourthAppId = DatabaseAppId("app 4")
    val FifthAppId = DatabaseAppId("app 5")

    val AllAppsIds = listOf(
        FirstAppId,
        SecondAppId,
        ThirdAppId,
        FourthAppId,
        FifthAppId
    )
}
