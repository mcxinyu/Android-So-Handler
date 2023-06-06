import android.annotation.SuppressLint

val userPlugin: String by project

plugins {
    id("com.android.application")
    kotlin("android")
//    id("com.imf.plugin.so.SoLoadHookPlugin")
}

android {
    namespace = "com.imf.test"

    compileSdk = 33

    defaultConfig {
        applicationId = "com.imf.test"
        minSdk = 14
        targetSdk = 28

        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        ndk.abiFilters += listOf("armeabi-v7a", "arm64-v8a")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            isShrinkResources = false
            signingConfig = signingConfigs.findByName("debug")

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
        }
    }
//    flavorDimensions += listOf("default")
//    productFlavors {
//        create("ioTestV7a") {
//            dimension = "default"
//            ndk.abiFilters += listOf("armeabi-v7a")
//        }
//        create("ioTestV8a") {
//            dimension = "default"
//            ndk.abiFilters += listOf("arm64-v8a")
//        }
//    }
    sourceSets {
        getByName("main") {
            jniLibs.srcDirs("libs")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation(project(":testLibrary"))
    implementation(files("libs/aar-release.aar"))
    implementation("com.github.mainlxl:blur:1.0.1")
}

if (userPlugin.toBoolean()) {
    apply(from = "${rootDir}/so-file-config.gradle")
}
