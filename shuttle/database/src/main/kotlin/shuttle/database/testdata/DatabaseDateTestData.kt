package shuttle.database.testdata

import shuttle.database.model.DatabaseDate

@Suppress("MagicNumber")
object DatabaseDateTestData {

    val Today = DatabaseDate(dayNumber = 100) // 2022-03-10
    val ThreeDaysAgo = DatabaseDate(dayNumber = Today.dayNumber - 3) // 2022-03-07
    val Yesterday = DatabaseDate(dayNumber = Today.dayNumber - 1) // 2022-03-09
}
