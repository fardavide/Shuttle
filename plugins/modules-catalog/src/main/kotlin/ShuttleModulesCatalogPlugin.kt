import org.gradle.api.Plugin
import org.gradle.api.Project

abstract class ShuttleModulesCatalogPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.subprojects.forEach(ShuttleModulesCatalogExtension::setup)
    }
}
