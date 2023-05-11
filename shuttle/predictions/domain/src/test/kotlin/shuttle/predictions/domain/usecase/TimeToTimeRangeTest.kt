package shuttle.predictions.domain.usecase

import korlibs.time.Time
import korlibs.time.hours
import kotlin.test.Test
import kotlin.test.assertEquals

class TimeToTimeRangeTest {

    private val timeToTimeRange = TimeToTimeRange()

    @Test
    fun `create time range with one hour span`() {
        // given
        val input = Time(hour = 4, minute = 15)
        val expected = Time(hour = 3, minute = 45) .. Time(hour = 4, minute = 45)

        // when
        val result = timeToTimeRange(input, 1.hours)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `create time range exceeding midnight`() {
        // given
        val input = Time(hour = 23, minute = 15)
        val expected = Time(hour = 21, minute = 15) .. Time(hour = 25, minute = 15)

        // when
        val result = timeToTimeRange(input, 4.hours)

        // then
        assertEquals(expected, result)
        assertEquals(expected.start.adjust(), result.start.adjust())
        assertEquals(expected.endInclusive.adjust(), result.endInclusive.adjust())
    }
}
