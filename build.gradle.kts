// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    val userPlugin: String by extra
    val SO_PLUGIN_VERSION: String by extra

    dependencies {
        if (userPlugin.toBoolean()) {
            classpath("com.imf.so:load-hook-plugin:${SO_PLUGIN_VERSION}")
            classpath("com.imf.so:file-plugin:${SO_PLUGIN_VERSION}")
        }
    }
}

plugins {
    id("com.android.application") version "8.0.2" apply false
    id("com.android.library") version "8.0.2" apply false
    kotlin("android") version "1.8.21" apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
