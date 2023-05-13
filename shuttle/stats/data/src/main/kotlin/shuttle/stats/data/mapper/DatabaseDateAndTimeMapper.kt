package shuttle.stats.data.mapper

import korlibs.time.Date
import korlibs.time.DateTime
import korlibs.time.Time
import org.koin.core.annotation.Factory
import shuttle.database.model.DatabaseDate
import shuttle.database.model.DatabaseTime
import shuttle.stats.data.model.DatabaseDateAndTime

@Factory
internal class DatabaseDateAndTimeMapper {

    fun toDatabaseDateAndTime(dateTime: DateTime) = DatabaseDateAndTime(
        date = toDatabaseDate(dateTime.date),
        time = toDatabaseTime(dateTime.time)
    )

    fun toDatabaseDate(date: Date) = DatabaseDate(dayNumber = date.dayOfYear + (date.year - YearOffset) * DaysInOneYear)

    fun toDatabaseTime(time: Time) = DatabaseTime(minuteOfTheDay = time.hourAdjusted * OneHourInMinutes + time.minute)

    companion object {

        private const val DaysInOneYear = 365

        private const val OneHourInMinutes = 60

        /**
         * Release year of the app, used as offset to reduce the size of the [DatabaseDate.dayNumber]
         */
        private const val YearOffset = 2022
    }
}
