package shuttle.plugins.android

import org.gradle.api.Plugin
import org.gradle.api.Project
import shuttle.plugins.util.apply

@Suppress("unused")
internal class AndroidComposeLibraryPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target.pluginManager) {
            apply<AndroidLibraryPlugin>()
            apply<AndroidComposePlugin>()
            apply("app.cash.paparazzi")
        }
    }
}
