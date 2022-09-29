package shuttle.stats.data.model

import shuttle.database.model.DatabaseDate
import shuttle.database.model.DatabaseTime

data class DatabaseDateAndTime(
    val date: DatabaseDate,
    val time: DatabaseTime
)
