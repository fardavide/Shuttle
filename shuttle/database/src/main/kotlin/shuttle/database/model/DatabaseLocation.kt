package shuttle.database.model

data class DatabaseLocation(
    val latitude: DatabaseLatitude,
    val longitude: DatabaseLongitude
)

@JvmInline
value class DatabaseLatitude(val value: Double)

@JvmInline
value class DatabaseLongitude(val value: Double)
