import org.gradle.api.provider.Property

interface SimpleExtension {
    val message: Property<String>
}