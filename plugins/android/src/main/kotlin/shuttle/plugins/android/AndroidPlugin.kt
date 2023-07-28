package shuttle.plugins.android

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinTopLevelExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinAndroidPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import shuttle.plugins.common.JvmDefaults
import shuttle.plugins.common.KotlinDefaults
import shuttle.plugins.util.apply
import shuttle.plugins.util.configure
import shuttle.plugins.util.withType

/**
 * Applies a shared configuration to Android targets (apps, libraries, etc...).
 */
internal class AndroidPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.pluginManager.apply<KotlinAndroidPluginWrapper>()

        target.extensions.configure<KotlinTopLevelExtension> { ext ->
            ext.jvmToolchain(JvmDefaults.JAVA_VERSION)
        }

        target.tasks.withType<KotlinCompile> { task ->
            task.compilerOptions {
                allWarningsAsErrors.set(JvmDefaults.WARNINGS_AS_ERRORS)
                jvmTarget.set(JvmDefaults.Target)
            }
            task.kotlinOptions {
                freeCompilerArgs = freeCompilerArgs + KotlinDefaults.FreeCompilerArgs
            }
        }

        target.extensions.configure<CommonExtension<*, *, *, *, *>> { ext ->
            val namespace = target.path.removePrefix(":").replace(":", ".")
            configureAndroidExtension(ext, namespace)
        }
        AndroidOptInsExtension.setup(target)
    }

    private fun configureAndroidExtension(ext: CommonExtension<*, *, *, *, *>, namespace: String) {
        if (ext !is ApplicationExtension) {
            ext.namespace = namespace
        }

        ext.buildToolsVersion = AndroidDefaults.BUILD_TOOLS
        ext.compileSdk = AndroidDefaults.COMPILE_SDK
        ext.defaultConfig.minSdk = AndroidDefaults.MIN_SDK

        val javaVersion = JavaVersion.toVersion(JvmDefaults.JAVA_VERSION)
        ext.compileOptions.apply {
            sourceCompatibility = javaVersion
            targetCompatibility = javaVersion
        }

        ext.defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ext.lint {
            checkAllWarnings = true
            warningsAsErrors = true
            disable += listOf("DuplicateStrings", "InvalidPackage", "SyntheticAccessor", "VectorPath")
        }

        ext.packaging.resources.excludes.addAll(
            listOf(
                "META-INF/licenses/ASM",
                "META-INF/LICENSE-notice.md",
                "META-INF/LICENSE.md",
                "META-INF/{AL2.0,LGPL2.1}",
                "win32-x86-64/attach_hotspot_windows.dll",
                "win32-x86/attach_hotspot_windows.dll"
            )
        )
    }
}
