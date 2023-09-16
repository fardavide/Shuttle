package shuttle.design.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.CoroutineScope

/**
 * This is a container for single-use state.
 * Use this when you don't want an event to be repeated, for example while emitting an error to the ViewModel
 *
 * You usually wanna consume this into a `LaunchedEffect` block
 */
class Effect<T : Any> private constructor(private var event: T?) {

    /**
     * @return the [event] if not consumed, `null` otherwise
     */
    fun consume(): T? = event
        .also { event = null }

    companion object {

        fun <T : Any> of(event: T) = Effect(event)
        fun <T : Any> empty() = Effect<T>(null)
    }
}

fun Effect.Companion.ofUnit() = of(Unit)

/**
 * Executes a [LaunchedEffect] in the scope of [effect]
 * @param block will be called only when there is an [Effect.event] to consume
 */
@Composable
fun <T : Any> ConsumableLaunchedEffect(effect: Effect<T>, block: suspend CoroutineScope.(T) -> Unit) {
    effect.consume()?.let { event ->
        LaunchedEffect(event) {
            block(event)
        }
    }
}
