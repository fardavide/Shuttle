package shuttle.database.testutil

import shuttle.database.App
import shuttle.database.LocationStat
import shuttle.database.TimeStat
import shuttle.database.adapter.IdAdapter
import shuttle.database.adapter.LatitudeAdapter
import shuttle.database.adapter.LongitudeAdapter
import shuttle.database.adapter.TimeAdapter

internal val AppAdapter get() = App.Adapter(IdAdapter)
internal val IdAdapter get() = IdAdapter()
internal val LatitudeAdapter get() = LatitudeAdapter()
internal val LocationStatAdapter get() = LocationStat.Adapter(IdAdapter, LatitudeAdapter, LongitudeAdapter)
internal val LongitudeAdapter get() = LongitudeAdapter()
internal val TimeAdapter get() = TimeAdapter()
internal val TimeStatAdapter get() = TimeStat.Adapter(IdAdapter, TimeAdapter)
