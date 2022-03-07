package shuttle.database.adapter

import com.squareup.sqldelight.ColumnAdapter
import shuttle.database.model.DatabaseGeoHash

internal class GeoHashAdapter : ColumnAdapter<DatabaseGeoHash, String> {

    override fun decode(databaseValue: String) = DatabaseGeoHash(databaseValue)

    override fun encode(value: DatabaseGeoHash): String = value.value
}
