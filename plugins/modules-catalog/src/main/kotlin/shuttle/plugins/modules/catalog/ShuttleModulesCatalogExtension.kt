package shuttle.plugins.modules.catalog

import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import javax.inject.Inject

open class ShuttleModulesCatalogExtension @Inject constructor(project: Project): ModulesCatalog(project) {

    companion object {

        fun setup(project: Project): ShuttleModulesCatalogExtension =
            project.extensions.create("moduleDependencies")
    }
}
