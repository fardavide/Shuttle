package shuttle.database.adapter

import com.squareup.sqldelight.ColumnAdapter
import org.koin.core.qualifier.named
import org.koin.dsl.module
import shuttle.database.App
import shuttle.database.AppBlacklistSetting
import shuttle.database.LastLocation
import shuttle.database.Stat
import shuttle.database.model.DatabaseAppId
import shuttle.database.model.DatabaseDate
import shuttle.database.model.DatabaseGeoHash
import shuttle.database.model.DatabaseTime

internal val databaseAdaptersModule = module {

    factory<ColumnAdapter<DatabaseAppId, String>>(Qualifier.AppIdAdapter) { AppIdAdapter() }
    factory<ColumnAdapter<DatabaseDate, Long>>(Qualifier.DateAdapter) { DateAdapter() }
    factory<ColumnAdapter<DatabaseGeoHash, String>>(Qualifier.GeoHashAdapter) { GeoHashAdapter() }
    factory<ColumnAdapter<DatabaseTime, Long>>(Qualifier.TimeAdapter) { TimeAdapter() }

    factory { App.Adapter(idAdapter = get(Qualifier.AppIdAdapter)) }
    factory { AppBlacklistSetting.Adapter(appIdAdapter = get(Qualifier.AppIdAdapter)) }
    factory { LastLocation.Adapter(geoHashAdapter = get(Qualifier.GeoHashAdapter)) }
    factory {
        Stat.Adapter(
            appIdAdapter = get(Qualifier.AppIdAdapter),
            dateAdapter = get(Qualifier.DateAdapter),
            geoHashAdapter = get(Qualifier.GeoHashAdapter),
            timeAdapter = get(Qualifier.TimeAdapter)
        )
    }
}

private object Qualifier {

    val AppIdAdapter get() = named("AppIdAdapter")
    val DateAdapter get() = named("DateAdapter")
    val GeoHashAdapter get() = named("GeoHashAdapter")
    val TimeAdapter get() = named("TimeAdapter")
}
