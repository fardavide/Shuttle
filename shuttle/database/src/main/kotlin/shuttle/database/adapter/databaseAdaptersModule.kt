package shuttle.database.adapter

import com.squareup.sqldelight.ColumnAdapter
import org.koin.core.qualifier.named
import org.koin.dsl.module
import shuttle.database.LocationStat
import shuttle.database.TimeStat
import shuttle.database.model.DatabaseAppId
import shuttle.database.model.DatabaseLatitude
import shuttle.database.model.DatabaseLongitude
import shuttle.database.model.DatabaseTime

internal val databaseAdaptersModule = module {

    factory<ColumnAdapter<DatabaseAppId, String>>(Qualifier.IdAdapter) { IdAdapter() }
    factory<ColumnAdapter<DatabaseLatitude, Double>>(Qualifier.LatitudeAdapter) { LatitudeAdapter() }
    factory<ColumnAdapter<DatabaseLongitude, Double>>(Qualifier.LongitudeAdapter) { LongitudeAdapter() }
    factory<ColumnAdapter<DatabaseTime, Long>>(Qualifier.TimeAdapter) { TimeAdapter() }

    factory {
        LocationStat.Adapter(
            appIdAdapter = get(Qualifier.IdAdapter),
            latitudeAdapter = get(Qualifier.LatitudeAdapter),
            longitudeAdapter = get(Qualifier.LongitudeAdapter)
        )
    }
    factory { TimeStat.Adapter(appIdAdapter = get(Qualifier.IdAdapter), timeAdapter = get(Qualifier.TimeAdapter)) }
}

private object Qualifier {

    val IdAdapter get() = named("IdAdapter")
    val LatitudeAdapter get() = named("LatitudeAdapter")
    val LongitudeAdapter get() = named("LongitudeAdapter")
    val TimeAdapter get() = named("TimeAdapter")
}
