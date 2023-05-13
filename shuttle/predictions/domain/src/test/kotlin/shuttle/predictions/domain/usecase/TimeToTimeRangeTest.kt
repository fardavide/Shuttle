package shuttle.predictions.domain.usecase

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import korlibs.time.Time
import korlibs.time.hours

class TimeToTimeRangeTest : AnnotationSpec() {

    private val timeToTimeRange = TimeToTimeRange()

    @Test
    fun `create time range with one hour span`() {
        // given
        val input = Time(hour = 4, minute = 15)
        val expected = Time(hour = 3, minute = 45) .. Time(hour = 4, minute = 45)

        // when
        val result = timeToTimeRange(input, 1.hours)

        // then
        result shouldBe expected
    }

    @Test
    fun `create time range exceeding midnight`() {
        // given
        val input = Time(hour = 23, minute = 15)
        val expected = Time(hour = 21, minute = 15) .. Time(hour = 25, minute = 15)

        // when
        val result = timeToTimeRange(input, 4.hours)

        // then
        result shouldBe expected
        result.start.adjust() shouldBe expected.start.adjust()
        result.endInclusive.adjust() shouldBe expected.endInclusive.adjust()
    }
}
