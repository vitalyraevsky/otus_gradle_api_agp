import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.workers.WorkerExecutor
import java.io.File
import javax.inject.Inject

@CacheableTask
open class CreateTextFilesTask @Inject constructor(
    private val workerExecutor: WorkerExecutor
): DefaultTask() {

    @OutputDirectory
    val outputDir: DirectoryProperty = project.objects
        .directoryProperty()
        .convention(
            project.layout.buildDirectory.dir("filesToPrint")
        )

    @TaskAction
    fun execute() {
        val queue = workerExecutor.noIsolation()
        val fileNamesToContent = listOf(
            "content1.txt" to "Love Gradle",
            "content2.txt" to "Love Gradle Tasks",
            "content3.txt" to "Love Gradle Worker API"
        )
        val outputDir = outputDir.get().asFile

        fileNamesToContent.forEach { (fileName, fileContent) ->
            queue.submit(GenerateWorkerAction::class.java) {
                it.outputFile = File(outputDir, fileName)
                it.content = fileContent
            }
        }
    }
}