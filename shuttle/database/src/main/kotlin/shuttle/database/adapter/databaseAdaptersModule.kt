package shuttle.database.adapter

import com.squareup.sqldelight.ColumnAdapter
import org.koin.core.qualifier.named
import org.koin.dsl.module
import shuttle.database.App
import shuttle.database.AppBlacklistSetting
import shuttle.database.LastLocation
import shuttle.database.LocationStat
import shuttle.database.TimeStat
import shuttle.database.model.DatabaseAppId
import shuttle.database.model.DatabaseGeoHash
import shuttle.database.model.DatabaseTime

internal val databaseAdaptersModule = module {

    factory<ColumnAdapter<DatabaseAppId, String>>(Qualifier.AppIdAdapter) { AppIdAdapter() }
    factory<ColumnAdapter<DatabaseGeoHash, String>>(Qualifier.GeoHashAdapter) { GeoHashAdapter() }
    factory<ColumnAdapter<DatabaseTime, Long>>(Qualifier.TimeAdapter) { TimeAdapter() }

    factory { App.Adapter(idAdapter = get(Qualifier.AppIdAdapter)) }
    factory { AppBlacklistSetting.Adapter(appIdAdapter = get(Qualifier.AppIdAdapter)) }
    factory {
        LastLocation.Adapter(
            latitudeAdapter = get(Qualifier.LatitudeAdapter),
            longitudeAdapter = get(Qualifier.LongitudeAdapter)
        )
    }
    factory {
        LocationStat.Adapter(
            appIdAdapter = get(Qualifier.AppIdAdapter),
            geoHashAdapter = get(Qualifier.GeoHashAdapter)
        )
    }
    factory { TimeStat.Adapter(appIdAdapter = get(Qualifier.AppIdAdapter), timeAdapter = get(Qualifier.TimeAdapter)) }
}

private object Qualifier {

    val AppIdAdapter get() = named("AppIdAdapter")
    val GeoHashAdapter get() = named("GeoHashAdapter")
    val TimeAdapter get() = named("TimeAdapter")
}
