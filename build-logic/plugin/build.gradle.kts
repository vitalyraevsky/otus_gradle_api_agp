plugins {
    alias(libs.plugins.kotlin.jvm)
    `java-gradle-plugin`
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    compileOnly(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    compileOnly(libs.android.gradlePlugin.api)
    implementation(gradleKotlinDsl())
}

gradlePlugin {
    plugins {
        register("otus-plugin") {
            id = "otus-plugin"
            implementationClass = "SimplePlugin"
        }
    }
}