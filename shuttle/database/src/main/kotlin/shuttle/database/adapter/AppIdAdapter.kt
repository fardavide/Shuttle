package shuttle.database.adapter

import com.squareup.sqldelight.ColumnAdapter
import shuttle.database.model.DatabaseAppId

internal class AppIdAdapter : ColumnAdapter<DatabaseAppId, String> {

    override fun decode(databaseValue: String) = DatabaseAppId(databaseValue)

    override fun encode(value: DatabaseAppId): String = value.value
}
