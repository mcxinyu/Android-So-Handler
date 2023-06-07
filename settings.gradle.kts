pluginManagement {
    val GROUP_ID: String by settings
    val SO_PLUGIN_VERSION: String by settings
    val ANDROID_GRADLE_VERSION: String by settings

    includeBuild("load-hook-plugin")
    includeBuild("file-plugin")
    repositories {
        maven(uri("./build/maven"))
        gradlePluginPortal()
        google()
        mavenLocal()
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://maven.aliyun.com/repository/google")
        maven("https://maven.aliyun.com/repository/jcenter")
    }

    // 这里统一配置插件版本，因为在 build.gradle.kts 的 plugins{} 里面无法读取 gradle.properties
    plugins {
        id("com.android.application") version "$ANDROID_GRADLE_VERSION"
        id("com.android.library") version "$ANDROID_GRADLE_VERSION"
        id("com.imf.plugin.so.SoLoadHookPlugin") version "${SO_PLUGIN_VERSION}"
        id("com.imf.plugin.so.ApkSoFileAdjustPlugin") version "$SO_PLUGIN_VERSION"
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        maven(uri("./build/maven"))
        google()
        mavenLocal()
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://maven.aliyun.com/repository/google")
        maven("https://maven.aliyun.com/repository/jcenter")
    }
}

rootProject.name = "Android-So-Handler"

//include(":load-hook-plugin")
//include(":file-plugin")
include(":load-hook")
include(":android-un7z")
include(":load-assets-7z")
include(":p7z")
include(":app")
include(":testLibrary")
