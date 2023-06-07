// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    //region 以下配置代码展示了如何在外部使用本库插件：
    // 插件库还没有发布到 Gradle Plugin Portal，外部使用的时候需要通过 classpath 声明插件库
//    dependencies {
//        val userPlugin: String by extra
//        val SO_PLUGIN_VERSION: String by extra
//        if (userPlugin.toBoolean()) {
//            classpath("com.imf.so:load-hook-plugin:${SO_PLUGIN_VERSION}")
//            classpath("com.imf.so:file-plugin:${SO_PLUGIN_VERSION}")
//        }
//    }
    //endregion
}

plugins {
    id("com.android.application") apply false
    id("com.android.library") apply false
    kotlin("android") version "1.8.10" apply false
    id("com.imf.plugin.so.SoLoadHookPlugin") apply false
    id("com.imf.plugin.so.ApkSoFileAdjustPlugin") apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
