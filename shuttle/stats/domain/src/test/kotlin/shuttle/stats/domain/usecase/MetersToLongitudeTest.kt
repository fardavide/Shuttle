package shuttle.stats.domain.usecase

import kotlin.test.Test
import kotlin.test.assertEquals

class MetersToLongitudeTest {

    private val metersToLongitude = MetersToLongitude()

    @Test
    fun `111_111 meters are 1 degree at 0 latitude`() {
        assertEquals(1.00, metersToLongitude(111_111, 0.00))
    }
}
