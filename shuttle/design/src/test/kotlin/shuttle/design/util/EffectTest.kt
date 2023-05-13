package shuttle.design.util

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe

internal class EffectTest : AnnotationSpec() {

    @Test
    fun `event is returned correctly on consume`() {
        // given
        val event = "hello"
        val effect = Effect.of(event)

        // when - then
        effect.consume() shouldBe event
    }

    @Test
    fun `event is cleared on consume`() {
        // given
        val effect = Effect.of("hello")

        // when
        effect.consume()

        // then
        effect.consume() shouldBe null
    }
}
