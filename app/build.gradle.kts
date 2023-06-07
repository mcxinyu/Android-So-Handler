import com.imf.plugin.so.SoFileExtensions
import com.imf.plugin.so.SoLoadHookExtensions
import org.jetbrains.kotlin.gradle.utils.IMPLEMENTATION

val userPlugin: String by project
val SO_PLUGIN_VERSION: String by project

plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.imf.test"

    compileSdk = 33

    defaultConfig {
        applicationId = "com.imf.test"
        minSdk = 14
        targetSdk = 28

        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        ndk.abiFilters += listOf("armeabi-v7a", "arm64-v8a")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            isShrinkResources = false
            signingConfig = signingConfigs.findByName("debug")

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
        }
    }
//    flavorDimensions += listOf("default")
//    productFlavors {
//        create("ioTestV7a") {
//            dimension = "default"
//            ndk.abiFilters += listOf("armeabi-v7a")
//        }
//        create("ioTestV8a") {
//            dimension = "default"
//            ndk.abiFilters += listOf("arm64-v8a")
//        }
//    }
    sourceSets {
        getByName("main") {
            jniLibs.srcDirs("libs")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation(project(":testLibrary"))
    implementation(files("libs/aar-release.aar"))
    implementation("com.github.mainlxl:blur:1.0.1")
}

if (userPlugin.toBoolean()) {
    // so 库压缩配置
    // 版本指根目录 build.gradle 下
    // com.android.tools.build:gradle:x.x.x 中 x.x.x 版本号
    // 3.4.0 版本及以下使用 SoFileTransformPlugin
    // 3.5.0 - 3.6.0 版本使用 SoFileAttachMergeTaskPlugin
    // 理论上通用 ApkSoFileAdjustPlugin  不过会对 apk 包进行二次打包组装
    // SoFileTransformPlugin 使用 Transform 进行 so 文件压缩删除
    // SoFileAttachMergeTaskPlugin 则在指定文件夹中处理
    // ApkSoFileAdjustPlugin 插件:4.1.0 中添加了 compressed_assets 机制导致无法把压缩后的 so 文件放入 asstes 中
    // 顾调整为针对已出包 apk 进行 so 文件操作并重新签名
    apply(plugin = "com.imf.plugin.so.ApkSoFileAdjustPlugin")
    configure<SoFileExtensions> {
        /**
         * 总开关配置 不配置时根据 compressSo2AssetsLibs 与 deleteSoLibs 是否为空自动开启关闭 配置 true 强制开启 false 强制关闭
         */
//        enable = false
        // 设置 debug 下不删除与压缩 so 库
//        excludeBuildTypes = setOf("debug")

        useApktool = true
        /**
         * 强制保留所有依赖 默认为 false 时 minSdkVersion <= 23 保留所有依赖 minSdkVersion > 23 只保留 deleteSoLibs 与
         * compressSo2AssetsLibs 中处理过的依赖
         */
        forceNeededRetainAllDependencies = true
        // 设置要删除的 so 库
        deleteSoLibs = emptySet()
        backupApk = true
        /**
         * 移除 so 时回调，这里可以做上传云端的逻辑
         */
        onDeleteSo = { file, md5 ->
            file.absolutePath.replace(".so", "_${md5}.so")
        }
        // 设置要压缩的库 注意 libun7zip.so 为 7z 解压库不可压缩
        compressSo2AssetsLibs = setOf(
            "libtestLibrary.so",
            "libnative-aar-lib.so",
            "libsource.so",
            "libblur-lib.so",
        )
        // 排除依赖
        excludeDependencies = setOf(
            "libGLESv2.so",
            "libGLESv3.so",
            "libgraphicsenv.so",
            "libjnigraphics.so",
            "liblog.so",
            "liblz4.so",
            "liblzma.so"
        )
        /**
         * 配置自定义依赖 用于解决 a.so 并未声明依赖 b.so 并且内部通过 dlopen 打开 b.so 或者反射 System.loadLibrary 等跳过 hook 加载
         * so 库等场景
         */
        customDependencies = mapOf("libsource.so" to listOf("liblog.so"))
    }

    // so 库加载 Hook 插件
    // 通过 com.imf.so.SoLoadHook#setSoLoadProxy(com.imf.so.SoLoadProxy)
    // 设置加载代理
    // 加载代理指编辑期间插件修改 java.lang.System#load(java.lang.String) 与
    // java.lang.System#loadLibrary(java.lang.String) 为
    // com.imf.so.SoLoadHook#load(java.lang.String) 与
    // com.imf.so.SoLoadHook#loadLibrary(java.lang.String)
    // 防止递归被 Hook 代理
    // 1. 继承 com.imf.so.SoLoadProxy 的类不会被修改 内部类会被修改
    // 2.excludePackage 跳过指定包名不会被修改
    // 3. 使用 @KeepSystemLoadLib 注解不会被修改
    // 具体可查看 load-hook 库
    // implementation "com.imf.so:load-hook:x.x.x"
    apply(plugin = "com.imf.plugin.so.SoLoadHookPlugin")
    configure<SoLoadHookExtensions> {
        // 是否跳过 R 文件与 BuildConfig
        skipRAndBuildConfig = true
        // 设置跳过的包名, 跳过的包不去 hook 修改后请先 clean
//        excludePackage = setOf("com.imf.test")
    }
    dependencies {
        IMPLEMENTATION("com.imf.so:load-hook:${SO_PLUGIN_VERSION}")
        IMPLEMENTATION("com.imf.so:load-assets-7z:${SO_PLUGIN_VERSION}")
    }
}
