plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.otus.plugin)
    alias(libs.plugins.maven.publish)
}

android {
    namespace = "ru.otus.gradleapi"
    compileSdk = 35

    defaultConfig {
        applicationId = "ru.otus.gradleapi"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        named("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        named("debug") {

        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    publishing {
        singleVariant("debug") {
            publishApk()
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

afterEvaluate {
    publishing {
        publications {
            register<MavenPublication>("debugMaven") {
                from(components["debug"])
                groupId = "ru.otus.gradleapi"
                artifactId = "app"
            }
        }
        repositories {
            maven {
                name = "github"
                url =  uri("https://maven.pkg.github.com/otus-kotlin/otus-gradleapi")
                credentials {
                    username = ""
                    password = ""
                }
            }
        }
    }
}

tasks.create("otus3") {
    doFirst {
        println("Hello from action 3")
    }
}

task("otus2") {
    dependsOn("otus")
    dependsOn("otus3")
}

tasks.register("otus") {
    println("Hello from config")
    doFirst {
        println("Hello from action")
    }
}


simple {
    message = ""
}
