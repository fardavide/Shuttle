package shuttle.database.model

sealed interface DatabaseCounterId {

    fun asString(): String = when (this) {
        is All -> "all"
        is ForTime -> "time:${hour.hourOfTheDay}"
        is ForLocation -> "location:${location.value}"
        is ForTimeAndLocation -> "time:${hour.hourOfTheDay}_location:${location.value}"
    }

    data object All : DatabaseCounterId

    @JvmInline
    value class ForTime(val hour: DatabaseHour) : DatabaseCounterId

    @JvmInline
    value class ForLocation(val location: DatabaseGeoHash) : DatabaseCounterId

    data class ForTimeAndLocation(val hour: DatabaseHour, val location: DatabaseGeoHash) : DatabaseCounterId

    companion object {

        fun fromString(string: String): DatabaseCounterId {
            return when {
                "all" in string -> All
                "time:" in string && "location:" in string -> {
                    val hour = string.substringAfter("time:").substringBefore("_").toInt()
                    val location = string.substringAfter("location:")
                    ForTimeAndLocation(DatabaseHour(hour), DatabaseGeoHash(location))
                }
                "time:" in string -> {
                    val hour = string.substringAfter("time:").toInt()
                    ForTime(DatabaseHour(hour))
                }
                "location:" in string -> {
                    val location = string.substringAfter("location:")
                    ForLocation(DatabaseGeoHash(location))
                }
                else -> error("Unknown counter id: $string")
            }
        }
    }
}
