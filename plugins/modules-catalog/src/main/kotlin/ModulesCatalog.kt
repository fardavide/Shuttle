@file:Suppress("unused")

import org.gradle.api.Project

abstract class ModulesCatalog(project: Project): ModuleCatalog(project, ":shuttle:shuttle-")

val ModulesCatalog.apps get() = AppsModuleCatalog(project)
fun ModulesCatalog.database() = create("database")
fun ModulesCatalog.design() = create("design")
fun ModulesCatalog.di() = create("di")
val ModulesCatalog.predictions get() = PredictionsModuleCatalog(project)
val ModulesCatalog.stats get() = StatsModuleCatalog(project)

fun AppsModuleCatalog.data() = create("data")
fun AppsModuleCatalog.domain() = create("domain")
fun AppsModuleCatalog.presentation() = create("presentation")

fun PredictionsModuleCatalog.domain() = create("domain")

fun StatsModuleCatalog.data() = create("data")
fun StatsModuleCatalog.domain() = create("domain")


class AppsModuleCatalog(project: Project): ModuleCatalog(project, ":shuttle:apps:shuttle-apps-")
class PredictionsModuleCatalog(project: Project): ModuleCatalog(project, ":shuttle:predictions:shuttle-predictions-")
class StatsModuleCatalog(project: Project): ModuleCatalog(project, ":shuttle:stats:shuttle-stats-")


abstract class ModuleCatalog(val project: Project, val path: String) {

    fun create(module: String) = project.dependencies.add(
        "implementation",
        project.project("$path$module")
    )
}

operator fun <T : ModuleCatalog> T.invoke(block: T.() -> Unit) = block()
