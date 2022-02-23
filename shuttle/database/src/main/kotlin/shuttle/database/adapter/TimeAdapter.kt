package shuttle.database.adapter

import com.squareup.sqldelight.ColumnAdapter
import shuttle.database.model.DatabaseTime

internal class TimeAdapter : ColumnAdapter<DatabaseTime, Long> {

    override fun decode(databaseValue: Long) = DatabaseTime(databaseValue.toInt())

    override fun encode(value: DatabaseTime): Long = value.minute.toLong()
}
