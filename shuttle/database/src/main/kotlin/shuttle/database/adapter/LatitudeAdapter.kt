package shuttle.database.adapter

import com.squareup.sqldelight.ColumnAdapter
import shuttle.database.model.DatabaseLatitude

internal class LatitudeAdapter : ColumnAdapter<DatabaseLatitude, Double> {

    override fun decode(databaseValue: Double) = DatabaseLatitude(databaseValue)

    override fun encode(value: DatabaseLatitude): Double = value.value
}
