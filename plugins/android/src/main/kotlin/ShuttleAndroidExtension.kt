import com.android.build.gradle.TestedExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getByType
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
        val libs = project.rootProject.extensions.getByType<VersionCatalogsExtension>().named("libs")
        val composeCompilerVersion = libs.findVersion("composeCompiler").get().toString()

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
