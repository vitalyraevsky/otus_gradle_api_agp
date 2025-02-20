import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.the

internal val Project.applicationExtension: ApplicationExtension
    get() = extensions.findByName("android") as ApplicationExtension

internal val Project.libraryExtension: LibraryExtension
    get() = extensions.findByName("android") as LibraryExtension

val Project.libs: LibrariesForLibs
    get() = the<LibrariesForLibs>()
