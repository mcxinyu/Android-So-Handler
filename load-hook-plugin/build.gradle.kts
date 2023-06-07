import org.jetbrains.kotlin.konan.properties.loadProperties
val properties = loadProperties("${rootDir.parent}/gradle.properties")
val GROUP_ID: String by properties
val SO_PLUGIN_VERSION: String by properties
val ANDROID_GRADLE_VERSION: String by properties

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
}

gradlePlugin {
    val soLoadHookPlugin by plugins.creating {
        id = "com.imf.plugin.so.SoLoadHookPlugin"
        implementationClass = "com.imf.plugin.so.SoLoadHookPlugin"
    }
}

dependencies {
    implementation("org.apache.commons:commons-io:1.3.2")
    implementation("commons-codec:commons-codec:1.15")
    implementation("com.android.tools:common:31.0.2")
    compileOnly("com.android.tools.build:gradle:$ANDROID_GRADLE_VERSION")
//    compileOnly("com.android.tools.build:gradle-api:$ANDROID_GRADLE_VERSION")
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
