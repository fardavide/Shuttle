package shuttle.database.testutil

import shuttle.database.model.DatabaseAppId
import shuttle.database.model.DatabaseLatitude
import shuttle.database.model.DatabaseLongitude
import shuttle.database.model.DatabaseTime

val ExactTime = DatabaseTime(100L)
val RangeStartTime = DatabaseTime(90L)
val RangeEndTime = DatabaseTime(110L)
val ExactLatitude = DatabaseLatitude(20.0)
val RangeStartLatitude = DatabaseLatitude(10.0)
val RangeEndLatitude = DatabaseLatitude(30.0)
val ExactLongitude = DatabaseLongitude(30.0)
val RangeStartLongitude = DatabaseLongitude(20.0)
val RangeEndLongitude = DatabaseLongitude(40.0)

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
