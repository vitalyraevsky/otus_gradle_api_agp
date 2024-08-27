import org.gradle.workers.WorkParameters
import java.io.File

interface GenerateWorkerParams : WorkParameters {
    var content: String
    var outputFile: File
}