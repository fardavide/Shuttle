package shuttle.plugins.common

/**
 * The only purpose of this annotation is to add an inspection rule to do not mark Gradle Plugins as unused.
 */
@Target(AnnotationTarget.CLASS)
@Retention(value = AnnotationRetention.SOURCE)
annotation class GradlePlugin
