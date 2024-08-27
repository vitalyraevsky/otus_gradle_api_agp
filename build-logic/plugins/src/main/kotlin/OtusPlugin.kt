import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.gradle.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.register

class OtusPlugin : Plugin<Project> {

    override fun apply(project: Project) {

        project.plugins.run {
            apply("maven-publish")
        }
        project.configure<PublishingExtension> {

        }


        //
        //if (project == project.rootProject) {
        //}
        val generateReportTask = project.tasks.register<GenerateReportTask>("generateReport") {

            sourceDirectory = project.file(project.layout.projectDirectory.file("src/main"))
            reportFile =
                project.layout.buildDirectory.file("reports/directoryReport.txt").get().asFile
        }

        project.tasks.register<CreateTextFilesTask>("createTextFilesTask")

        // AGP
        project.plugins.withType(AppPlugin::class.java) {

            val androidComponents =
                project.extensions.getByType(ApplicationAndroidComponentsExtension::class.java)

            androidComponents.finalizeDsl { extension ->
                project.tasks.getByName("assemble") {
                    it.dependsOn(generateReportTask)
                }

                extension.buildTypes.register("extra2").apply {
                    get().isDebuggable = true
                }


            }
        }

        project.applicationExtension.apply {
            buildTypes.create("extra").apply {
                isDebuggable = true
            }
        }
    }
}