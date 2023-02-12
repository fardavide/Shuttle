package shuttle.plugins.modulescatalog

import org.gradle.api.Plugin
import org.gradle.api.Project

@Suppress("unused")
internal class ModulesCatalogPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        ModulesCatalogExtension.setup(target)
    }
}
