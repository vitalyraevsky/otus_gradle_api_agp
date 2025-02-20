import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

@CacheableTask
//@PathSensitive(PathSensitivity.RELATIVE)
abstract class SimpleTask : DefaultTask() {

    @get:Input
    //val message = project.objects.property<String>()
    abstract val message: Property<String>

    @TaskAction
    fun act() {
        println("Hello from task ${message.get()}")
    }
}