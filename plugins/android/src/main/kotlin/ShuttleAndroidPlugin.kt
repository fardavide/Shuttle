import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

abstract class ShuttleAndroidPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        ShuttleAndroidExtension.setup(target)
        target.setupAndroidPlugin()
    }
}

private fun Project.setupAndroidPlugin() {
    apply(plugin = "org.jetbrains.kotlin.android")
    apply(plugin = "com.android.${ if (name == "app") "application" else "library" }")
    apply<ShuttleKotlinPlugin>()

    configureTestedExtension()
}
