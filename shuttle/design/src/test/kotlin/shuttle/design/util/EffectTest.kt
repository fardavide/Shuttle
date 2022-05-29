package shuttle.design.util

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class EffectTest {

    @Test
    fun `event is returned correctly on consume`() {
        // given
        val event = "hello"
        val effect = Effect.of(event)

        // when - then
        assertEquals(event, effect.consume())
    }

    @Test
    fun `event is cleared on consume`() {
        // given
        val effect = Effect.of("hello")

        // when
        effect.consume()

        // then
        assertNull(effect.consume())
    }
}
