@file:Suppress("unused")

import org.gradle.api.Project

@Suppress("UnnecessaryAbstractClass")
abstract class ModulesCatalog(project: Project): ModuleCatalog(project, ":shuttle:shuttle-")

fun ModulesCatalog.accessibility() = create("accessibility")
val ModulesCatalog.apps get() = AppsModuleCatalog(project)
val ModulesCatalog.coordinates get() = CoordinatesModuleCatalog(project)
fun ModulesCatalog.database() = create("database")
fun ModulesCatalog.design() = create("design")
fun ModulesCatalog.di() = create("di")
val ModulesCatalog.icons get() = IconsModuleCatalog(project)
val ModulesCatalog.permissions get() = PermissionsModuleCatalog(project)
val ModulesCatalog.predictions get() = PredictionsModuleCatalog(project)
val ModulesCatalog.settings get() = SettingsModuleCatalog(project)
val ModulesCatalog.stats get() = StatsModuleCatalog(project)
val ModulesCatalog.test get() = TestModuleCatalog(project)
val ModulesCatalog.utils get() = UtilsModuleCatalog(project)

fun AppsModuleCatalog.data() = create("data")
fun AppsModuleCatalog.domain() = create("domain")
fun AppsModuleCatalog.presentation() = create("presentation")

fun CoordinatesModuleCatalog.data() = create("data")
fun CoordinatesModuleCatalog.domain() = create("domain")

fun IconsModuleCatalog.data() = create("data")
fun IconsModuleCatalog.domain() = create("domain")
fun IconsModuleCatalog.presentation() = create("presentation")

fun PermissionsModuleCatalog.domain() = create("domain")
fun PermissionsModuleCatalog.presentation() = create("presentation")

fun PredictionsModuleCatalog.domain() = create("domain")
fun PredictionsModuleCatalog.presentation() = create("presentation")

fun SettingsModuleCatalog.data() = create("data")
fun SettingsModuleCatalog.domain() = create("domain")
fun SettingsModuleCatalog.presentation() = create("presentation")

fun StatsModuleCatalog.data() = create("data")
fun StatsModuleCatalog.domain() = create("domain")

fun TestModuleCatalog.compose() = create("compose", configurationName = "androidTestImplementation")

fun UtilsModuleCatalog.android() = create("android")
fun UtilsModuleCatalog.kotlin() = create("kotlin")


class AppsModuleCatalog(project: Project): ModuleCatalog(project, ":shuttle:apps:shuttle-apps-")
class CoordinatesModuleCatalog(project: Project): ModuleCatalog(project, ":shuttle:coordinates:shuttle-coordinates-")
class IconsModuleCatalog(project: Project): ModuleCatalog(project, ":shuttle:icons:shuttle-icons-")
class PermissionsModuleCatalog(project: Project): ModuleCatalog(project, ":shuttle:permissions:shuttle-permissions-")
class PredictionsModuleCatalog(project: Project): ModuleCatalog(project, ":shuttle:predictions:shuttle-predictions-")
class SettingsModuleCatalog(project: Project): ModuleCatalog(project, ":shuttle:settings:shuttle-settings-")
class StatsModuleCatalog(project: Project): ModuleCatalog(project, ":shuttle:stats:shuttle-stats-")
class TestModuleCatalog(project: Project): ModuleCatalog(project, ":shuttle:test:shuttle-test-")
class UtilsModuleCatalog(project: Project): ModuleCatalog(project, ":shuttle:utils:shuttle-utils-")


@Suppress("UnnecessaryAbstractClass")
abstract class ModuleCatalog(val project: Project, private val path: String) {

    fun create(module: String, configurationName: String = "implementation") =
        project.dependencies.add(configurationName, project.project("$path$module"))
}

operator fun <T : ModuleCatalog> T.invoke(block: T.() -> Unit) = block()
