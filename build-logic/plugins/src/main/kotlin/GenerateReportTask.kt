import org.gradle.api.DefaultTask
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.gradle.work.Incremental
import java.io.File

@CacheableTask
abstract class GenerateReportTask : DefaultTask() {

    @get:InputDirectory
    @get:Incremental
    @get:PathSensitive(PathSensitivity.NONE)
    abstract var sourceDirectory: File

    @get:OutputFile
    abstract var reportFile: File

    @TaskAction
    fun generateReport() {
        val fileCount = sourceDirectory.listFiles().count { it.isFile }
        val directoryCount = sourceDirectory.listFiles().count { it.isDirectory }

        val reportContent = """
            |Report for directory: ${sourceDirectory.absolutePath}
            |------------------------------
            |Number of files: $fileCount
            |Number of subdirectories: $directoryCount
        """.trimMargin()
        reportFile.writeText(reportContent)
        println("Report generated at: ${reportFile.absolutePath}")
    }
}