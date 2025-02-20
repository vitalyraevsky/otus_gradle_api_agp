import org.gradle.api.DefaultTask
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.TaskAction
import org.gradle.workers.WorkerExecutor
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.OutputDirectory
import javax.inject.Inject
import java.io.File


@CacheableTask
open class CreateTextFilesTask @Inject constructor(
    private val workerExecutor: WorkerExecutor,
) : DefaultTask() {

    @OutputDirectory
    val outputDirection: DirectoryProperty = project.objects
        .directoryProperty()
        .convention(
            project.layout.buildDirectory.dir("files")
        )

    @TaskAction
    fun execute() {
        val queue = workerExecutor.noIsolation()
        val filesNamesToContent = listOf(
            "file1.txt" to "Otus",
            "file2.txt" to "Otus Gradle",
            "file3.txt" to "Otus Gradle AGP",
        )

        val outputDir = outputDirection.get().asFile
        filesNamesToContent.forEach { (fileName, fileContent) ->
            queue.submit(GenerateWorkerAction::class.java) {
                it.content = fileContent
                it.outputFile = File(outputDir, fileName)
            }
        }
    }
}