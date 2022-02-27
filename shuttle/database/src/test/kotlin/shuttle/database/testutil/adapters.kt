package shuttle.database.testutil

import shuttle.database.App
import shuttle.database.AppBlacklistSetting
import shuttle.database.LocationStat
import shuttle.database.TimeStat
import shuttle.database.adapter.AppIdAdapter
import shuttle.database.adapter.LatitudeAdapter
import shuttle.database.adapter.LongitudeAdapter
import shuttle.database.adapter.TimeAdapter

internal val AppAdapter get() = App.Adapter(AppIdAdapter)
internal val AppIdAdapter get() = AppIdAdapter()
internal val AppBlacklistSettingAdapter get() = AppBlacklistSetting.Adapter(AppIdAdapter)
internal val LatitudeAdapter get() = LatitudeAdapter()
internal val LocationStatAdapter get() = LocationStat.Adapter(AppIdAdapter, LatitudeAdapter, LongitudeAdapter)
internal val LongitudeAdapter get() = LongitudeAdapter()
internal val TimeAdapter get() = TimeAdapter()
internal val TimeStatAdapter get() = TimeStat.Adapter(AppIdAdapter, TimeAdapter)
