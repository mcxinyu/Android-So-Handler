val GROUP_ID: String by project
val SO_PLUGIN_VERSION: String by project
val ANDROID_GRADLE_VERSION: String by project

plugins {
    id("com.android.library")
    kotlin("android")
    `maven-publish`
}

android {
    namespace = "com.imf.so.load.assetes"
    compileSdk = 33

    defaultConfig {
        minSdk = 14
        targetSdk = 33
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_17
//        targetCompatibility = JavaVersion.VERSION_17
//    }
    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

dependencies {
    implementation(project(":load-hook"))
    implementation(project(":android-un7z"))
//    implementation("com.android.tools:annotations:30.4.2")
    implementation("androidx.annotation:annotation:1.3.0")
}

group = GROUP_ID
version = SO_PLUGIN_VERSION

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                groupId = GROUP_ID
                artifactId = project.name
                version = SO_PLUGIN_VERSION
                from(components["release"])
//                artifact(sourcesJar)
            }
        }
        repositories {
            maven {
                name = "buildLocal"
                url = uri("${rootProject.buildDir}/maven")
            }
        }
    }
}

//val sourcesJar by tasks.register<Jar>("sourcesJar"){
//    archiveClassifier.set("sources")
//    from(android.sourceSets.map { it.java.getSourceFiles() })
//}