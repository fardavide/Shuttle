package shuttle.coordinates.data.mapper

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import shuttle.coordinates.domain.model.GeoHash

class GeoHashMapperTest : AnnotationSpec() {

    private val mapper = GeoHashMapper()

    @Test
    fun `verify hash is formatted correctly`() {
        // given
        val expected = GeoHash("sjr4et3")

        // when
        val result = mapper.toGeoHash(30.0, 10.0)

        // then
        result shouldBe expected
    }
}
