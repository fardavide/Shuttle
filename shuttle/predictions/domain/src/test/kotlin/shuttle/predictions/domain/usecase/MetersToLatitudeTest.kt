package shuttle.predictions.domain.usecase

import kotlin.test.Test
import kotlin.test.assertEquals

class MetersToLatitudeTest {

    private val metersToLatitude = MetersToLatitude()

    @Test
    fun `111_111 meters are 1 degree`() {
        assertEquals(1.00, metersToLatitude(111_111))
    }
}
