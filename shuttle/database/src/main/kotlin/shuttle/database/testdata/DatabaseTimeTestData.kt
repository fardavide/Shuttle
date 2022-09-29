package shuttle.database.testdata

import shuttle.database.model.DatabaseTime

object DatabaseTimeTestData {

    val Afternoon = DatabaseTime(minuteOfTheDay = 960) // 16:00
    val Evening = DatabaseTime(minuteOfTheDay = 1260) // 21:00
    val Midnight = DatabaseTime(minuteOfTheDay = 0) // 00:00
    val Morning = DatabaseTime(minuteOfTheDay = 300) // 05:00
    val Noon = DatabaseTime(minuteOfTheDay = 720) // 12:00
}
