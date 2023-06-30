package shuttle.plugins.android

import com.android.build.gradle.TestedExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import shuttle.plugins.util.configure
import shuttle.plugins.util.libsCatalog

internal class AndroidComposePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        val libsCatalog = target.libsCatalog
        target.extensions.configure<TestedExtension> { ext -> configureComposeOptions(libsCatalog, ext) }
        target.pluginManager.apply("app.cash.molecule")
    }

    @Suppress("UnstableApiUsage")
    private fun configureComposeOptions(libsCatalog: VersionCatalog, ext: TestedExtension) {
        ext.buildFeatures.compose = true
        val composeCompilerVersion = libsCatalog.findVersion("composeCompiler").get().toString()
        ext.composeOptions.kotlinCompilerExtensionVersion = composeCompilerVersion
    }
}
