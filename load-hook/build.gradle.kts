val GROUP_ID: String by project
val SO_PLUGIN_VERSION: String by project
val ANDROID_GRADLE_VERSION: String by project

plugins {
    `kotlin-dsl`
    `maven-publish`
}

dependencies {
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