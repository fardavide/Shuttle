package shuttle.utils.kotlin

/**
 * @return [List] of nullable [T] with [List.size] equals to [count]
 *  @see take, if the receiver [List.size] < than [count], fills with `null`s
 */
fun <T> List<T>.takeOrFillWithNulls(count: Int): List<T?> =
    with(take(count)) {
        val diff = count - size
        if (diff == 0) this
        else this + (0..diff).map { null }
    }
