import com.android.build.gradle.TestedExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import javax.inject.Inject

@Suppress("unused")
open class ShuttleAndroidExtension @Inject constructor(private val project: Project) {

    fun androidApp(
        id: String,
        versionCode: Int,
        versionName: String
    ) {
        project.extensions.configure<TestedExtension> {
            defaultConfig {
                this.applicationId = id
                this.versionCode = versionCode
                this.versionName = versionName
            }
        }
    }

    @Suppress("UnstableApiUsage")
    fun useCompose() {
        val composeCompilerVersion = project.libsCatalog.findVersion("composeCompiler").get().toString()

        project.extensions.configure<TestedExtension> {
            buildFeatures.compose = true
            composeOptions.kotlinCompilerExtensionVersion = composeCompilerVersion
        }
    }

    companion object {

        fun setup(project: Project): ShuttleAndroidExtension =
            project.extensions.create("shuttleAndroid")
    }
}
