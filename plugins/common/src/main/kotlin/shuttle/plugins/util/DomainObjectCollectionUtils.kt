package shuttle.plugins.util

import org.gradle.api.DomainObjectCollection

/**
 * Invokes Groovy's [DomainObjectCollection.withType] reifying the type [T].
 */
inline fun <reified T : Any> DomainObjectCollection<in T>.withType(
    noinline configuration: (T) -> Unit
): DomainObjectCollection<T> = withType(T::class.java, configuration)
