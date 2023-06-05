val GROUP_ID: String by project
val SO_PLUGIN_VERSION: String by project
val ANDROID_GRADLE_VERSION: String by project

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
}

gradlePlugin {
    val soFileTransformPlugin by plugins.creating {
        id = "com.imf.plugin.so.SoFileTransformPlugin"
        implementationClass = "com.imf.plugin.so.SoFileTransformPlugin"
    }
    val soFileAttachMergeTaskPlugin by plugins.creating {
        id = "com.imf.plugin.so.SoFileAttachMergeTaskPlugin"
        implementationClass = "com.imf.plugin.so.SoFileAttachMergeTaskPlugin"
    }
    val apkSoFileAdjustPlugin by plugins.creating {
        id = "com.imf.plugin.so.ApkSoFileAdjustPlugin"
        implementationClass = "com.imf.plugin.so.ApkSoFileAdjustPlugin"
    }
}

dependencies {
    implementation(gradleApi())
    implementation("com.android.tools:common:31.0.2")
    implementation("com.google.code.gson:gson:2.9.1")
    compileOnly("com.android.tools.build:gradle:$ANDROID_GRADLE_VERSION")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.google.gradle:osdetector-gradle-plugin:1.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.apktool:apktool-lib:2.7.0")
    implementation("org.apache.commons:commons-io:1.3.2")
}

java {
    withSourcesJar()
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
                from(components["java"])
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