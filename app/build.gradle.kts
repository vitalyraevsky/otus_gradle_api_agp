import org.jetbrains.kotlin.gradle.dsl.kotlinExtension

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("otus.plugin")
}

android {
    namespace = "ru.otus.gradleagp"
    compileSdk = 34

    defaultConfig {
        applicationId = "ru.otus.gradleagp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        resourceConfigurations += listOf("en", "ru")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "GOOGLE_KEY", "")
    }

    signingConfigs {
        named("debug") {
            storeFile = file("application1.jks")
            storePassword = "password1"
            keyAlias = "application1"
            keyPassword = "password1"
        }
        register("release") {
            storeFile = file("application1.jks")
            storePassword = "password1"
            keyAlias = "application1"
            keyPassword = "password1"
        }
    }


    buildTypes {
        //release {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        named("debug") {
            signingConfig = signingConfigs.findByName("debug")
        }

        //create("feature") {
        register("feature") {

        }
    }

    flavorDimensions += listOf("type", "mode")

    productFlavors {
        create("demo") {
            dimension = "mode"
        }
        register("full") {
            dimension = "mode"
            signingConfig = signingConfigs.findByName("release")
        }

        register("google") {
            dimension = "type"
        }

        register("huawei") {
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

    splits { // Разбиение APK
        density { // Разбиение APK по плотности
            isEnable = true
            compatibleScreens("small", "normal", "large", "xlarge")
        }

        abi { // Разбиение APK по архитектуре
            isEnable = true
            reset()
            include("armeabi-v7a", "x86", "x86_64")
            isUniversalApk = true
        }
    }
}

androidComponents {
    finalizeDsl { // Вызывается после генерации конфигурации проета
        it.buildFeatures.buildConfig = true
    }

    beforeVariants { variantBuilder -> // Вызывается до генерации конфигурации проета
        variantBuilder.minSdk = 24
    }

    beforeVariants { variantBuilder ->
        if (variantBuilder.productFlavors.containsAll(
                listOf("type" to "huawei", "mode" to "demo")
            )
        ) {
            variantBuilder.enable = false
        }
    }

    val debug = selector().withBuildType("debug")
    beforeVariants(debug) { variantBuilder ->
        println("Called with variant : ${variantBuilder.name}")
    }

    onVariants(selector().withBuildType("release")) { variant -> // Вызывается после генерации конфигурации проета
        println("Called with ready variant : ${variant.name}")
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    //debug
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}