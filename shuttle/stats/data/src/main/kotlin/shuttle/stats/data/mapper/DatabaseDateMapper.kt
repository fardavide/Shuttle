package shuttle.stats.data.mapper

import com.soywiz.klock.Date
import com.soywiz.klock.DateTime
import shuttle.database.model.DatabaseDate

internal class DatabaseDateMapper {

    fun toDatabaseDate(dateTime: DateTime) =
        toDatabaseDate(dateTime.date)

    fun toDatabaseDate(date: Date) =
        DatabaseDate(date.dayOfYear + (date.year - 2022) * 365)
}
