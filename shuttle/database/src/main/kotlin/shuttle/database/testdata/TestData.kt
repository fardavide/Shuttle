package shuttle.database.testdata

import shuttle.database.model.DatabaseAppId
import shuttle.database.model.DatabaseGeoHash
import shuttle.database.model.DatabaseTime

object TestData {

    val GeoHash = DatabaseGeoHash("123")
    val AnotherGeoHash = DatabaseGeoHash("abc")
    val ExactTime = DatabaseTime(100)
    val RangeStartTime = DatabaseTime(90)
    val RangeMidFirstTime = DatabaseTime(92)
    val RangeMidSecondTime = DatabaseTime(94)
    val RangeEndTime = DatabaseTime(110)

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
