package shuttle.predictions.domain.usecase

import io.mockk.every
import io.mockk.mockk
import shuttle.coordinates.domain.model.Location
import shuttle.stats.domain.model.LocationRange
import kotlin.test.Test
import kotlin.test.assertEquals

class LocationToLocationRangeTest {

    private val metersToLatitude: MetersToLatitude = mockk {
        every { this@mockk(any()) } returns LatitudeOffset
    }
    private val metersToLongitude: MetersToLongitude = mockk {
        every { this@mockk(any(), any()) } returns LongitudeOffset
    }
    private val locationToLocationRange = LocationToLocationRange(
        metersToLatitude = metersToLatitude,
        metersToLongitude = metersToLongitude
    )

    @Test
    fun test() {
        // given
        val location = Location(11.0, -15.0)
        val expected = LocationRange(
            start = Location(10.6, -15.6),
            end = Location(11.4, -14.4)
        )

        // when
        val result = locationToLocationRange(location, 10)

        // then
        assertEquals(expected, result)
    }

    companion object TestData {

        private const val LatitudeOffset = 0.4
        private const val LongitudeOffset = 0.6
    }
}
