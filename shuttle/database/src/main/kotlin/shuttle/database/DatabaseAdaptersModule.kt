package shuttle.database

import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import shuttle.database.adapter.AppIdAdapter
import shuttle.database.adapter.DateAdapter
import shuttle.database.adapter.GeoHashAdapter
import shuttle.database.adapter.TimeAdapter

@Module
internal class DatabaseAdaptersModule {
    
    @Factory
    fun appAdapter(@Named(Qualifier.AppIdAdapter) addIdAdapter: AppIdAdapter) = App.Adapter(idAdapter = addIdAdapter)
    
    @Factory
    fun appBlacklistSettingAdapter(@Named(Qualifier.AppIdAdapter) appIdAdapter: AppIdAdapter) =
        AppBlacklistSetting.Adapter(appIdAdapter = appIdAdapter)
    
    @Factory
    @Named(Qualifier.AppIdAdapter)
    fun appIdAdapter() = AppIdAdapter()
    
    @Factory
    @Named(Qualifier.DateAdapter)
    fun dateAdapter() = DateAdapter()

    @Factory
    @Named(Qualifier.GeoHashAdapter)
    fun geoHashAdapter() = GeoHashAdapter()
    
    @Factory
    fun lastLocationAdapter(@Named(Qualifier.GeoHashAdapter) geoHashAdapter: GeoHashAdapter) =
        LastLocation.Adapter(geoHashAdapter = geoHashAdapter)
    
    @Factory
    fun statAdapter(
        @Named(Qualifier.AppIdAdapter) appIdAdapter: AppIdAdapter,
        @Named(Qualifier.DateAdapter) dateAdapter: DateAdapter,
        @Named(Qualifier.GeoHashAdapter) geoHashAdapter: GeoHashAdapter,
        @Named(Qualifier.TimeAdapter) timeAdapter: TimeAdapter
    ) = Stat.Adapter(
        appIdAdapter = appIdAdapter,
        dateAdapter = dateAdapter,
        geoHashAdapter = geoHashAdapter,
        timeAdapter = timeAdapter
    )
    
    @Factory
    @Named(Qualifier.TimeAdapter)
    fun timeAdapter() = TimeAdapter()
}

private object Qualifier {

    const val AppIdAdapter = "AppIdAdapter"
    const val DateAdapter = "DateAdapter"
    const val GeoHashAdapter = "GeoHashAdapter"
    const val TimeAdapter = "TimeAdapter"
}
