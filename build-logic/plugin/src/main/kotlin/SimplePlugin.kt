import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register
import com.android.build.gradle.AppPlugin
import org.gradle.api.publish.PublishingExtension
import org.gradle.kotlin.dsl.configure

class SimplePlugin : Plugin<Project> {

    override fun apply(project: Project) {

        project.pluginManager.apply("maven-publish")
        project.pluginManager.apply(project.libs.plugins.maven.publish.get().pluginId)
        project.plugins.run {
            apply("maven-publish")
        }
        project.configure<PublishingExtension> {

        }

        //project.extensions.create<SimpleExtension>("simple")
        val extension = project.extensions.create("simple", SimpleExtension::class.java)
        val task = project.tasks.register<SimpleTask>("simpleTask") {
            description = "Simple task"
            group = "otus"
            message.convention(extension.message)
            //message.set(extension.message)
            //message.value(extension.message)
        }

        val generateReport = project.tasks.register<GenerateReportTask>("generateReport") {
            group = "otus"
            sourceDirectory = project.file(project.layout.projectDirectory.file("src/main"))
            reportFile = project.layout.buildDirectory.file("reports/directoryReport.txt").get().asFile
        }

        project.tasks.register<CreateTextFilesTask>("createTextFilesTask") {
            group = "otus"
        }

        project.plugins.withType(AppPlugin::class.java) {
            val androidComponents = project.extensions.getByType(
                ApplicationAndroidComponentsExtension::class.java)

            androidComponents.finalizeDsl { extension ->
                project.tasks.getByName("assemble") {
                    it.dependsOn(generateReport)
                }

                extension.buildTypes.register("extra2").apply {
                    get().isDebuggable = true
                }
            }
        }

        project.applicationExtension.apply {
            buildTypes.register("extra").apply {
                get().isDebuggable = true
            }
        }
    }
}