package shuttle.database.adapter

import com.squareup.sqldelight.ColumnAdapter
import shuttle.database.model.DatabaseDate

internal class DateAdapter : ColumnAdapter<DatabaseDate, Long> {

    override fun decode(databaseValue: Long) = DatabaseDate(databaseValue.toInt())

    override fun encode(value: DatabaseDate): Long = value.dayNumber.toLong()
}
