package shuttle.plugins.util

import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.tasks.SourceSetContainer

val Project.sourceSets: SourceSetContainer get() = extensions.getByType()

/**
 * Invokes Groovy's [ExtensionContainer.configure] reifying the type [T].
 */
inline fun <reified T : Any> ExtensionContainer.configure(noinline action: (T) -> Unit) {
    configure(T::class.java, action)
}

/**
 * Invokes Groovy's [ExtensionContainer.create] reifying the type [T].
 */
inline fun <reified T : Any> ExtensionContainer.create(name: String): T = create(name, T::class.java)

/**
 * Invokes Groovy's [ExtensionContainer.getByType] reifying the type [T].
 */
inline fun <reified T : Any> ExtensionContainer.getByType(): T = getByType(T::class.java)

