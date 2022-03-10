import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.withType

abstract class ShuttleDetektPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.allprojects {
            setupDetekt()
        }
    }
}

private fun Project.setupDetekt() {
    apply<DetektPlugin>()

    tasks.withType<Detekt> {

        allRules = true
        basePath = rootDir.path
        config.setFrom("${rootDir.path}/detekt/config.yml")
        // TODO ignoredBuildTypes = ["release"]
    }
}
