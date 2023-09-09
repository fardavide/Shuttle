package shuttle.database

import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import shuttle.database.adapter.AppIdAdapter
import shuttle.database.adapter.CounterIdAdapter
import shuttle.database.adapter.DateAdapter
import shuttle.database.adapter.GeoHashAdapter
import shuttle.database.adapter.TimeAdapter

@Module
internal class DatabaseAdaptersModule {
    
    @Factory
    fun appAdapter(@Named(Qualifier.AppIdAdapter) addIdAdapter: AppIdAdapter) =
        App.Adapter(idAdapter = addIdAdapter)
    
    @Factory
    fun appBlacklistSettingAdapter(@Named(Qualifier.AppIdAdapter) appIdAdapter: AppIdAdapter) =
        AppBlacklistSetting.Adapter(appIdAdapter = appIdAdapter)
    
    @Factory
    @Named(Qualifier.AppIdAdapter)
    fun appIdAdapter() = AppIdAdapter()

    @Factory
    @Named(Qualifier.CounterIdAdapter)
    fun counterIdAdapter() = CounterIdAdapter()
    
    @Factory
    @Named(Qualifier.DateAdapter)
    fun dateAdapter() = DateAdapter()

    @Factory
    fun counterAdapter(
        @Named(Qualifier.AppIdAdapter) appIdAdapter: AppIdAdapter,
        @Named(Qualifier.CounterIdAdapter) counterIdAdapter: CounterIdAdapter
    ) = Counter.Adapter(
        appIdAdapter = appIdAdapter,
        counterIdAdapter = counterIdAdapter
    )

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
    fun suggestionCacheAdapter(@Named(Qualifier.AppIdAdapter) appIdAdapter: AppIdAdapter) =
        SuggestionCache.Adapter(idAdapter = appIdAdapter)
    
    @Factory
    @Named(Qualifier.TimeAdapter)
    fun timeAdapter() = TimeAdapter()
}

private object Qualifier {

    const val AppIdAdapter = "AppIdAdapter"
    const val CounterIdAdapter = "CounterIdAdapter"
    const val DateAdapter = "DateAdapter"
    const val GeoHashAdapter = "GeoHashAdapter"
    const val TimeAdapter = "TimeAdapter"
}
