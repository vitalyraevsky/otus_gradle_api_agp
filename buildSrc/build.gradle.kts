import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.jvm)
    `kotlin-dsl`
}

val javaVersion = JavaVersion.VERSION_17

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = javaVersion.toString()
}

dependencies {
    implementation(gradleApi())
    implementation(localGroovy())
}

gradlePlugin {
    plugins {
        register("otus-plugin") {
            id = "otus-plugin"
            description = "Simple plugin"
            implementationClass = "ru.otus.plugin.SimplePlugin"
        }
    }
}