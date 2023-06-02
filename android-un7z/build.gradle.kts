val GROUP_ID: String by project
val SO_PLUGIN_VERSION: String by project
val ANDROID_GRADLE_VERSION: String by project

plugins {
    id("com.android.library")
    kotlin("android")
    `maven-publish`
}

android {
    namespace = "com.hzy.lib7z"

    compileSdk = 33

    defaultConfig {
        minSdk = 14
        targetSdk = 33
    }
    externalNativeBuild {
        cmake {
            path("src/main/cpp/CMakeLists.txt")
        }
    }

    buildTypes {
        debug {
            externalNativeBuild {
                cmake {
                    cFlags("-DNATIVE_LOG")
                }
            }
        }
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
