package shuttle.database

import com.squareup.sqldelight.ColumnAdapter
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import shuttle.database.adapter.AppIdAdapter
import shuttle.database.adapter.DateAdapter
import shuttle.database.adapter.GeoHashAdapter
import shuttle.database.adapter.TimeAdapter
import shuttle.database.model.DatabaseAppId
import shuttle.database.model.DatabaseDate
import shuttle.database.model.DatabaseGeoHash
import shuttle.database.model.DatabaseTime

@Module
internal class DatabaseAdaptersModule {
    
    @Factory
    fun appAdapter(addIdAdapter: AppIdAdapter) = App.Adapter(idAdapter = addIdAdapter)
    
    @Factory
    fun appBlacklistSettingAdapter(appIdAdapter: AppIdAdapter) =
        AppBlacklistSetting.Adapter(appIdAdapter = appIdAdapter)
    
    @Factory
    @Named(Qualifier.AppIdAdapter)
    fun appIdAdapter(): ColumnAdapter<DatabaseAppId, String> = AppIdAdapter()
    
    @Factory
    @Named(Qualifier.DateAdapter)
    fun dateAdapter(): ColumnAdapter<DatabaseDate, Long> = DateAdapter()

    @Factory
    @Named(Qualifier.GeoHashAdapter)
    fun geoHashAdapter(): ColumnAdapter<DatabaseGeoHash, String> = GeoHashAdapter()
    
    @Factory
    fun lastLocationAdapter(geoHashAdapter: GeoHashAdapter) = LastLocation.Adapter(geoHashAdapter = geoHashAdapter)
    
    @Factory
    fun statAdapter(
        appIdAdapter: AppIdAdapter,
        dateAdapter: DateAdapter,
        geoHashAdapter: GeoHashAdapter,
        timeAdapter: TimeAdapter
    ) = Stat.Adapter(
        appIdAdapter = appIdAdapter,
        dateAdapter = dateAdapter,
        geoHashAdapter = geoHashAdapter,
        timeAdapter = timeAdapter
    )
    
    @Factory
    @Named(Qualifier.TimeAdapter)
    fun timeAdapter(): ColumnAdapter<DatabaseTime, Long> = TimeAdapter()
}

private object Qualifier {

    const val AppIdAdapter = "AppIdAdapter"
    const val DateAdapter = "DateAdapter"
    const val GeoHashAdapter = "GeoHashAdapter"
    const val TimeAdapter = "TimeAdapter"
}
