import org.jetbrains.kotlin.gradle.dsl.kotlinExtension

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
        resourceConfigurations += listOf("en", "ru")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "API_KEY", "\"somekey\"")
    }

/*
    signingConfigs {
        named("debug") {
            storeFile = file("application1.jks")
            storePassword = "password1"
            keyAlias = "application1"
            keyPassword = "password1"
        }
        register("release") {
            storeFile = file("applicationRelease.jks")
            storePassword = "password1"
            keyAlias = "application1"
            keyPassword = "password1"
        }
    }
*/

    buildFeatures.buildConfig = true

    buildTypes {

        named("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        named("debug") {
            //signingConfig = signingConfigs.findByName("debug")
            applicationIdSuffix = ".debug"
            isMinifyEnabled = false
            isDebuggable = true
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        //create("feature") {
        register("feature") {
            initWith(getByName("debug"))
            matchingFallbacks.add("debug")
            applicationIdSuffix = ".preview"
        }
    }

    flavorDimensions += listOf("mode", "type")

    productFlavors {
        create("free") {
            dimension = "mode"
            applicationIdSuffix = ".free"
        }
        register("paid") {
            dimension = "mode"
        }
        register("huawei") {
            applicationIdSuffix = ".huawei"
            dimension = "type"
        }
        register("googlw") {
            dimension = "type"
        }
    }

    sourceSets {
        named("main") {
            res.srcDirs(
                "src/main/res",
                "src/ai/res"
            )
            java.srcDirs(
                "src/main/java",
                "src/ai/java"
            )
            assets.srcDirs(
                "src/main/assets",
                "src/ai/assets"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    kotlinExtension.apply {
        jvmToolchain(17)
    }

    publishing {
/*
        singleVariant("debug") {
            publishApk()
        }
*/
    }

    splits {
        abi {
            isEnable = true
            isUniversalApk = true
            reset()
            include("x86", "x86_64", "armeabi-v7a","arm64-v8a")
        }
        density {
            isEnable = true
            compatibleScreens("small", "normal", "large", "xlarge")
        }
    }
}

androidComponents {

    finalizeDsl { // Вызывается после генерации конфигурации проета
        it.buildFeatures.buildConfig = true
    }

    beforeVariants() { }  // Вызывается до генерации конфигурации проета
    beforeVariants { variant ->
        variant.minSdk = 26
    }
    beforeVariants { variant ->
        if (variant.productFlavors.containsAll(
            listOf("mode" to "free", "type" to "huawei")
        )) {
            variant.enable = false
        }
    }
    val debug = selector().withBuildType("debug")
    beforeVariants(debug) { variantBuilder ->
        println("Called with variant : ${variantBuilder.name}")
    }

    onVariants { }
    onVariants(debug) { }
    onVariants(selector().withBuildType("release")) {  variant -> // Вызывается после генерации конфигурации проета
        println("Called with ready variant : ${variant.name}")
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
//                from(components["debug"])
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
