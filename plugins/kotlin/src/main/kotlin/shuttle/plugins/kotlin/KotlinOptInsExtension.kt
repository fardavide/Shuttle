package shuttle.plugins.kotlin

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import shuttle.plugins.common.KotlinDefaults
import shuttle.plugins.util.configure
import shuttle.plugins.util.create
import javax.inject.Inject

open class KotlinOptInsExtension @Inject constructor(private val project: Project) {

    fun delicateCoroutinesApi() {
        optIn(KotlinDefaults.DelicateCoroutinesApi)
    }

    fun experimentalCoroutinesApi() {
        optIn(KotlinDefaults.ExperimentalCoroutinesApi)
    }

    fun experimentalPagingApi() {
        optIn(KotlinDefaults.ExperimentalPagingApi)
    }

    fun experimentalStdlibApi() {
        optIn(KotlinDefaults.ExperimentalStdlibApi)
    }

    fun flowPreview() {
        optIn(KotlinDefaults.FlowPreview)
    }

    private fun optIn(annotationName: String) {
        project.extensions.configure<KotlinJvmProjectExtension> { ext ->
            ext.sourceSets.all { sourceSet ->
                sourceSet.languageSettings.optIn(annotationName)
            }
        }
    }

    companion object {

        fun setup(project: Project): KotlinOptInsExtension = project.extensions.create("optIns")
    }
}
