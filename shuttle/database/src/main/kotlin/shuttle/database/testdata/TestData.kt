package shuttle.database.testdata

import shuttle.database.model.DatabaseAppId
import shuttle.database.model.DatabaseDate
import shuttle.database.model.DatabaseGeoHash
import shuttle.database.model.DatabaseTime

@Deprecated("Use Database*TestData instead")
object TestData {

    @Deprecated("Use Database*TestData instead")
    val Date = DatabaseDate(34)
    @Deprecated("Use Database*TestData instead")
    val GeoHash = DatabaseGeoHash("123")
    @Deprecated("Use Database*TestData instead")
    val AnotherGeoHash = DatabaseGeoHash("abc")
    @Deprecated("Use Database*TestData instead")
    val ExactTime = DatabaseTime(100)
    @Deprecated("Use Database*TestData instead")
    val RangeStartTime = DatabaseTime(90)
    @Deprecated("Use Database*TestData instead")
    val RangeEndTime = DatabaseTime(110)
    @Deprecated("Use Database*TestData instead")
    val AnotherTime = DatabaseTime(200)

    @Deprecated("Use Database*TestData instead")
    val FirstAppId = DatabaseAppId("app 1")
    @Deprecated("Use Database*TestData instead")
    val SecondAppId = DatabaseAppId("app 2")
    @Deprecated("Use Database*TestData instead")
    val ThirdAppId = DatabaseAppId("app 3")
    @Deprecated("Use Database*TestData instead")
    val FourthAppId = DatabaseAppId("app 4")
    @Deprecated("Use Database*TestData instead")
    val FifthAppId = DatabaseAppId("app 5")

    @Deprecated("Use Database*TestData instead")
    val AllAppsIds = listOf(
        FirstAppId,
        SecondAppId,
        ThirdAppId,
        FourthAppId,
        FifthAppId
    )
}
