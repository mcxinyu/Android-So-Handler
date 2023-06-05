pluginManagement {
    val GROUP_ID: String by settings
    val userPlugin: String by settings
    val SO_PLUGIN_VERSION: String by settings

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

//    plugins {
//        if (userPlugin.toBoolean()) {
//            id("com.imf.plugin.so.SoLoadHookPlugin") version "${SO_PLUGIN_VERSION}"
//            id("com.imf.plugin.so.ApkSoFileAdjustPlugin") version "$SO_PLUGIN_VERSION"
//        }
//    }
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

include(":load-hook-plugin")
include(":load-hook")
include(":file-plugin")
include(":android-un7z")
include(":load-assets-7z")
include(":p7z")
include(":app")
include(":testLibrary")
