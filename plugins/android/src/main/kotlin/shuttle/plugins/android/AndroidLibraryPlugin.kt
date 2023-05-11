package shuttle.plugins.android

import org.gradle.api.Plugin
import org.gradle.api.Project
import shuttle.plugins.util.apply

@Suppress("unused")
internal class AndroidLibraryPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.pluginManager.apply("com.android.library")
        target.pluginManager.apply<AndroidPlugin>()
    }
}
