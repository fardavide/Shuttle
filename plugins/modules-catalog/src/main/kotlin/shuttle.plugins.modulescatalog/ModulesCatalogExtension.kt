package shuttle.plugins.modulescatalog

import org.gradle.api.Project
import shuttle.plugins.util.create
import javax.inject.Inject

open class ModulesCatalogExtension @Inject constructor(private val project: Project) {

    fun api(module: String) {
        project.dependencies.add("api", project.project(":shuttle:$module"))
    }

    fun implementation(module: String) {
        project.dependencies.add("implementation", project.project(":shuttle:$module"))
    }

    fun testImplementation(module: String) {
        project.dependencies.add("testImplementation", project.project(":shuttle:$module"))
    }

    fun androidTestImplementation(module: String) {
        project.dependencies.add("androidTestImplementation", project.project(":shuttle:$module"))
    }

    companion object {

        fun setup(project: Project): ModulesCatalogExtension = project.extensions.create("moduleDependencies")
    }
}
