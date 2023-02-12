package shuttle.plugins.util

import org.gradle.api.Plugin
import org.gradle.api.plugins.PluginManager

/**
 * Applies the plugin of the given class [P].
 */
inline fun <reified P : Plugin<*>> PluginManager.apply() {
    apply(P::class.java)
}
