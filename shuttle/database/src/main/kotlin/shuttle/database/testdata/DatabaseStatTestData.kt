package shuttle.database.testdata

import shuttle.database.model.DatabaseAppId
import shuttle.database.model.DatabaseDate
import shuttle.database.model.DatabaseGeoHash
import shuttle.database.model.DatabaseStat
import shuttle.database.model.DatabaseTime

object DatabaseStatTestData {

    fun buildDatabaseStat(
        appId: DatabaseAppId,
        date: DatabaseDate = DatabaseDateTestData.Today,
        geoHash: DatabaseGeoHash = DatabaseGeoHashTestData.Home,
        time: DatabaseTime = DatabaseTimeTestData.Midnight
    ) = DatabaseStat(
        appId = appId,
        date = date,
        geoHash = geoHash,
        time = time
    )
}
