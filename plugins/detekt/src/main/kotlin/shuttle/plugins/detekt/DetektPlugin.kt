package shuttle.plugins.detekt

import io.gitlab.arturbosch.detekt.Detekt
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.dsl.DependencyHandler
import shuttle.plugins.util.apply
import shuttle.plugins.util.libsCatalog
import shuttle.plugins.util.withType

@Suppress("unused")
internal class DetektPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.pluginManager.apply<io.gitlab.arturbosch.detekt.DetektPlugin>()

        target.tasks.withType<Detekt> { task ->
            task.autoCorrect = true
            task.config.setFrom("${target.rootDir.path}/detekt/config.yml")
        }

        val catalog = target.libsCatalog
        target.dependencies.apply {
            addDetektPlugin(catalog, plugin = "detekt-rules-twitter-compose")
            addDetektPlugin(catalog, plugin = "detekt-rules-formatting")
            addDetektPlugin(target.rootProject.project(":detekt:rules"))
        }
    }

    private fun DependencyHandler.addDetektPlugin(catalog: VersionCatalog, plugin: String) {
        add("detektPlugins", catalog.findLibrary(plugin).get())
    }

    private fun DependencyHandler.addDetektPlugin(plugin: Project) {
        add("detektPlugins", plugin)
    }
}
