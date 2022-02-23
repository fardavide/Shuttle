package shuttle.database.adapter

import org.koin.dsl.module
import shuttle.database.App
import shuttle.database.LocationStat
import shuttle.database.TimeStat

internal val databaseAdaptersModule = module {

    factory { IdAdapter() }
    factory { LatitudeAdapter() }
    factory { LongitudeAdapter() }
    factory { TimeAdapter() }

    factory { App.Adapter(idAdapter = get()) }
    factory { LocationStat.Adapter(appIdAdapter = get(), latitudeAdapter = get(), longitudeAdapter = get()) }
    factory { TimeStat.Adapter(appIdAdapter = get(), timeAdapter = get()) }
}
