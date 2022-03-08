package shuttle.coordinates.data.mapper

import shuttle.coordinates.domain.model.GeoHash
import kotlin.test.Test
import kotlin.test.assertEquals

class GeoHashMapperTest {

    private val mapper = GeoHashMapper()

    @Test
    fun `verify hash is formatted correctly`() {
        // given
        val expected = GeoHash("sjr4et3")

        // when
        val result = mapper.toGeoHash(30.0, 10.0)

        // then
        assertEquals(expected, result)
    }
}
