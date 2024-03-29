package shuttle.plugins.kotlin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinTopLevelExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import shuttle.plugins.common.GradlePlugin
import shuttle.plugins.common.JvmDefaults
import shuttle.plugins.common.KotlinDefaults
import shuttle.plugins.util.apply
import shuttle.plugins.util.configure
import shuttle.plugins.util.withType

@GradlePlugin
internal class KotlinPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.pluginManager.apply<KotlinPluginWrapper>()

        target.tasks.withType<KotlinCompile> { task ->
            task.compilerOptions.allWarningsAsErrors.set(JvmDefaults.WARNINGS_AS_ERRORS)
            task.kotlinOptions {
                freeCompilerArgs = freeCompilerArgs + KotlinDefaults.FreeCompilerArgs
            }
            if ("Test" in task.name) {
                task.kotlinOptions {
                    freeCompilerArgs = freeCompilerArgs + KotlinDefaults.TestFreeCompilerArgs
                }
            }
        }

        target.extensions.configure<KotlinTopLevelExtension> { ext ->
            ext.jvmToolchain(JvmDefaults.JAVA_VERSION)
        }

        KotlinOptInsExtension.setup(target)
    }
}
