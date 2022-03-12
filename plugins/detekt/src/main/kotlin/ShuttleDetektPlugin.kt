import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.registering
import org.gradle.kotlin.dsl.withType
import java.io.File

abstract class ShuttleDetektPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.setupDetekt()
    }
}

private fun Project.setupDetekt() {
    allprojects {
        apply<DetektPlugin>()
    }

    val reportMerge by tasks.registering(io.gitlab.arturbosch.detekt.report.ReportMergeTask::class) {
        output.set(File("${rootDir.path}/detekt/reports/merge.sarif"))
    }

    subprojects {
        tasks.withType<Detekt> {

            allRules = true
            basePath = rootDir.path
            config.setFrom("${rootDir.path}/detekt/config.yml")
            reports.sarif.required.set(true)
            // TODO ignoredBuildTypes = ["release"]
        }

        plugins.withType<DetektPlugin> {
            tasks.withType<Detekt> {
                finalizedBy(reportMerge)

                reportMerge.configure {
                    input.from(sarifReportFile)
                }
            }
        }
    }
}
