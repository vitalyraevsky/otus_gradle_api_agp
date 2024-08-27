import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project

internal val Project.applicationExtension: ApplicationExtension
    get() = extensions.findByName("android") as ApplicationExtension

internal val Project.libraryExtension: LibraryExtension
    get() = extensions.findByName("android") as LibraryExtension