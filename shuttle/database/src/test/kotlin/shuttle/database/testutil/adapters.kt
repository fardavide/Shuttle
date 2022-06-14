package shuttle.database.testutil

import shuttle.database.App
import shuttle.database.AppBlacklistSetting
import shuttle.database.LastLocation
import shuttle.database.LocationStat
import shuttle.database.Stat
import shuttle.database.TimeStat
import shuttle.database.adapter.AppIdAdapter
import shuttle.database.adapter.DateAdapter
import shuttle.database.adapter.GeoHashAdapter
import shuttle.database.adapter.TimeAdapter

internal val AppAdapter get() = App.Adapter(AppIdAdapter)
internal val AppIdAdapter get() = AppIdAdapter()
internal val AppBlacklistSettingAdapter get() = AppBlacklistSetting.Adapter(AppIdAdapter)
internal val DateAdapter get() = DateAdapter()
internal val GeoHashAdapter get() = GeoHashAdapter()
internal val LastLocationAdapter get() = LastLocation.Adapter(GeoHashAdapter)
internal val LocationStatAdapter get() = LocationStat.Adapter(AppIdAdapter, GeoHashAdapter)
internal val StatAdapter get() = Stat.Adapter(AppIdAdapter, GeoHashAdapter, DateAdapter, TimeAdapter)
internal val TimeAdapter get() = TimeAdapter()
internal val TimeStatAdapter get() = TimeStat.Adapter(AppIdAdapter, TimeAdapter)
