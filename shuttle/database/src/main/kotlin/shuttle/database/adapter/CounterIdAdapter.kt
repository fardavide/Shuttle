package shuttle.database.adapter

import com.squareup.sqldelight.ColumnAdapter
import shuttle.database.model.DatabaseCounterId

internal class CounterIdAdapter : ColumnAdapter<DatabaseCounterId, String> {

    override fun decode(databaseValue: String) = DatabaseCounterId.fromString(databaseValue)

    override fun encode(value: DatabaseCounterId): String = value.asString()
}
