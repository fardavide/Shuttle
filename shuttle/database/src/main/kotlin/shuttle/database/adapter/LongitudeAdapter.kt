package shuttle.database.adapter

import com.squareup.sqldelight.ColumnAdapter
import shuttle.database.model.DatabaseLongitude

internal class LongitudeAdapter : ColumnAdapter<DatabaseLongitude, Double> {

    override fun decode(databaseValue: Double) = DatabaseLongitude(databaseValue)

    override fun encode(value: DatabaseLongitude): Double = value.value
}
