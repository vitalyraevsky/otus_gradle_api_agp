import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register

class SimplePlugin : Plugin<Project> {

    override fun apply(project: Project) {
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

    }
}