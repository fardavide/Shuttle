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
    if (isApp()) {
        apply(plugin = "com.android.application")
        apply(plugin = "com.google.gms.google-services")
        apply(plugin = "com.google.firebase.crashlytics")
    } else {
        apply(plugin = "com.android.library")
    }
    apply<ShuttleKotlinPlugin>()

    configureTestedExtension()
}

private fun Project.isApp() = name == "app"
