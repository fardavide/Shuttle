package shuttle.stats.data.mapper

import com.soywiz.klock.DateTime
import shuttle.database.model.DatabaseDate
import shuttle.database.model.DatabaseTime
import shuttle.stats.data.model.DatabaseDateAndTime
import kotlin.test.*

internal class DatabaseDateAndTimeMapperTest {

    private val mapper = DatabaseDateAndTimeMapper()

    @Test
    fun `maps correctly`() {
        // Given
        val dateTime = DateTime(
            year = 2022,
            month = 2,
            day = 3,
            hour = 7,
            minute = 25
        )
        val expected = DatabaseDateAndTime(
            date = DatabaseDate(dayNumber = 34),
            time = DatabaseTime(minuteOfTheDay = 7 * 60 + 25)
        )

        // When
        val actual = mapper.toDatabaseDateAndTime(dateTime)

        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun `maps correctly after noon`() {
        // Given
        val dateTime = DateTime(
            year = 2022,
            month = 2,
            day = 3,
            hour = 15,
            minute = 25
        )
        val expected = DatabaseDateAndTime(
            date = DatabaseDate(dayNumber = 34),
            time = DatabaseTime(minuteOfTheDay = 15 * 60 + 25)
        )

        // When
        val actual = mapper.toDatabaseDateAndTime(dateTime)

        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun `maps correctly at midnight`() {
        // Given
        val dateTime = DateTime(
            year = 2022,
            month = 2,
            day = 3,
            hour = 0,
            minute = 15
        )
        val expected = DatabaseDateAndTime(
            date = DatabaseDate(dayNumber = 34),
            time = DatabaseTime(minuteOfTheDay = 15)
        )

        // When
        val actual = mapper.toDatabaseDateAndTime(dateTime)

        // Then
        assertEquals(expected, actual)
    }
}
