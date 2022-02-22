import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.dependencies
import javax.inject.Inject

open class ShuttleModulesCatalogExtension @Inject constructor(project: Project): ModulesCatalog(project) {

    companion object {

        fun setup(project: Project): ShuttleModulesCatalogExtension =
            project.extensions.create("moduleDependencies")
    }
}
