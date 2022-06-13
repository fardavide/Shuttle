package shuttle.database.model

@JvmInline
value class DatabaseAppId(val value: String)

/**
 * @param dayNumber the number of the day since the start of 2022
 *  Calculated by:
 *   day of the year * 1 +
 *   (current year - 2022) * 365
 *  This number won't be extremely precise as some year have 366 days, but the threshold is of 1 every 4 years
 */
@JvmInline
value class DatabaseDate(val dayNumber: Int) {

    operator fun minus(other: DatabaseDate) = DatabaseDate(dayNumber - other.dayNumber)
}

/**
 * @param minuteOfTheDay the number of minute of the current day
 *  This can cause minor problems across the start of the new day ( midnight )
 */
@JvmInline
value class DatabaseTime(val minuteOfTheDay: Int)
