package shuttle.plugins.modulescatalog

import org.gradle.api.Project
import shuttle.plugins.util.create
import javax.inject.Inject

open class ModulesCatalogExtension @Inject constructor(private val project: Project) {

    fun add(module: String) {
        project.dependencies.add("implementation", project.project(":shuttle:$module"))
    }

    companion object {

        fun setup(project: Project): ModulesCatalogExtension = project.extensions.create("moduleDependencies")
    }
}
