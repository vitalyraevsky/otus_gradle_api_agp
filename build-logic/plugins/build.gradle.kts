plugins {
    `java-gradle-plugin`
    alias(libs.plugins.kotlin.jvm)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin.api)
    implementation(gradleKotlinDsl())
}

gradlePlugin {
    plugins {
        register("otusPlugin") {
            id = "otus.plugin"
            implementationClass = "OtusPlugin"
        }
    }
}