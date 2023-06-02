val GROUP_ID: String by project
val SO_PLUGIN_VERSION: String by project
val ANDROID_GRADLE_VERSION: String by project

plugins {
    `maven-publish`
}

group = "com.mainli"
version = "1.0.1"

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                artifactId = project.name
                groupId = "com.mainli"
                // 需同步跟随 com.imf.plugin.so.SoFilePlugin#find7zPath 种版本
                version = "1.0.1"
                artifact("executable/p7z-linux-x86_32.exe") {
                    classifier = "linux-x86_32"
                    extension = "exe"
                }
                artifact("executable/p7z-linux-x86_64.exe") {
                    classifier = "linux-x86_64"
                    extension = "exe"
                }
                artifact("executable/p7z-linux-aarch64.exe") {
                    classifier = "linux-aarch64"
                    extension = "exe"
                }
                artifact("executable/p7z-windows-x86_32.exe") {
                    classifier = "windows-x86_32"
                    extension = "exe"
                }
                artifact("executable/p7z-windows-x86_64.exe") {
                    classifier = "windows-x86_64"
                    extension = "exe"
                }
                artifact("executable/p7z-osx-x86_64.exe") {
                    classifier = "osx-x86_64"
                    extension = "exe"
                }
                artifact("executable/p7z-osx-aarch_64.exe") {
                    classifier = "osx-aarch_64"
                    extension = "exe"
                }
                artifact("executable/p7z.aar") {
                    classifier = "all"
                    extension = "aar"
                }
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
