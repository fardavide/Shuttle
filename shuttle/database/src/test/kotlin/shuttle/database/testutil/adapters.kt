package shuttle.database.testutil

import shuttle.database.App
import shuttle.database.AppBlacklistSetting
import shuttle.database.Counter
import shuttle.database.LastLocation
import shuttle.database.Stat
import shuttle.database.SuggestionCache
import shuttle.database.adapter.AppIdAdapter
import shuttle.database.adapter.CounterIdAdapter
import shuttle.database.adapter.DateAdapter
import shuttle.database.adapter.GeoHashAdapter
import shuttle.database.adapter.TimeAdapter

internal val AppAdapter get() = App.Adapter(AppIdAdapter)
internal val AppIdAdapter get() = AppIdAdapter()
internal val AppBlacklistSettingAdapter get() = AppBlacklistSetting.Adapter(AppIdAdapter)
internal val CounterAdapter get() = Counter.Adapter(CounterIdAdapter, AppIdAdapter)
internal val CounterIdAdapter get() = CounterIdAdapter()
internal val DateAdapter get() = DateAdapter()
internal val GeoHashAdapter get() = GeoHashAdapter()
internal val LastLocationAdapter get() = LastLocation.Adapter(GeoHashAdapter)
internal val StatAdapter get() = Stat.Adapter(AppIdAdapter, GeoHashAdapter, DateAdapter, TimeAdapter)
internal val SuggestionCacheAdapter get() = SuggestionCache.Adapter(AppIdAdapter)
internal val TimeAdapter get() = TimeAdapter()
