package shuttle.database.model

/**
 * @param minuteOfTheDay the number of minute of the current day
 *  This can cause minor problems across the start of the new day ( midnight )
 */
@JvmInline
value class DatabaseTime(val minuteOfTheDay: Int)
