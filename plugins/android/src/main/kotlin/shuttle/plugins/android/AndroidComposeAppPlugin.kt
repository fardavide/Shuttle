package shuttle.plugins.android

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import shuttle.plugins.util.apply
import shuttle.plugins.util.configure
import java.io.File

@Suppress("unused")
internal class AndroidComposeAppPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target.pluginManager) {
            apply("com.android.application")
            apply<AndroidPlugin>()
            apply<AndroidComposePlugin>()
        }

        target.extensions.configure<ApplicationExtension> { ext ->
            configureAndroidApplicationExtension(ext, target.rootDir)
        }
    }

    private fun configureAndroidApplicationExtension(ext: ApplicationExtension, rootDir: File) {
        ext.namespace = "studio.forface.shuttle"

        ext.defaultConfig {
            applicationId = AndroidDefaults.APPLICATION_ID
            versionCode = VersionCode
            versionName = VersionName
            targetSdk = AndroidDefaults.TARGET_SDK
        }

        ext.buildFeatures.buildConfig = true

        ext.signingConfigs {
            create("release") { config ->
                config.storeFile = File("${rootDir.path}/keystore/keystore.jks")
                config.storePassword = System.getenv("KEYSTORE_PASSWORD")
                config.keyAlias = System.getenv("KEYSTORE_KEY_ALIAS")
                config.keyPassword = System.getenv("KEYSTORE_KEY_PASSWORD")
            }
        }

        ext.buildTypes {
            named("release") { config ->
                config.isMinifyEnabled = false
                config.signingConfig = ext.signingConfigs.getByName("release")
            }
        }
    }

    companion object {

        val VersionCode = System.getenv()["APP_VERSION"]?.toInt() ?: 1
        val VersionName = "$VersionCode"
    }
}
