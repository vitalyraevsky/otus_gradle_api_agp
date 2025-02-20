import org.gradle.workers.WorkAction

abstract class GenerateWorkerAction : WorkAction<GenerateWorkerParams> {

    override fun execute() {
        val params = parameters
        params.outputFile.bufferedWriter().use { writer ->
            Thread.sleep(3000)
            writer.write(params.content)
        }
    }
}