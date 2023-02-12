package shuttle.plugins.android

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import shuttle.plugins.util.apply
import shuttle.plugins.util.configure

@Suppress("unused")
internal class AndroidLibraryPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.pluginManager.apply("com.android.library")
        target.pluginManager.apply<AndroidPlugin>()
        target.extensions.configure(::configureLibraryExtension)
    }

    @Suppress("UnstableApiUsage")
    private fun configureLibraryExtension(ext: LibraryExtension) {
        ext.sourceSets {
            configureEach { sourceSet ->
                sourceSet.kotlin.srcDir("build/generated/ksp/${sourceSet.name}/kotlin")
            }
        }
    }
}
