# Android-So-Handler

[![](https://jitpack.io/v/mcxinyu/Android-So-Handler.svg)](https://jitpack.io/#mcxinyu/Android-So-Handler)

最新 2.0 版本支持 Android Gradle Plugin 8.x，其他版本不做支持保证，理论上 agp 7.2 以上是可以兼容的[了解更多](https://developer.android.com/build/releases/gradle-plugin-api-updates#replacement_apis)。

新版变化，参考 [releases](https://github.com/mcxinyu/Android-So-Handler/releases)。

AGP7.x 及以下请切换到 [main-agp7.x](https://github.com/mcxinyu/Android-So-Handler/tree/main-agp7.x) 分支，原 README 请阅读 [main-agp7.x/README.md](https://github.com/mcxinyu/Android-So-Handler/blob/main-agp7.x/README.md)。

## 特点如下:

1. 支持 APK 中所有通过 `System.Load/LoadLibrary` 加载的 So 库文件（包含 Maven、aar 等方式引入三方库与源码中的
   so 文件）进行处理。
2. 支持 7z 压缩与云端下发
3. 对项目代码侵入少，如果只是压缩 so
   库，只需一行初始化 `AssetsSoLoadBy7zFileManager.init(v.getContext());` 即可。
4. 云端下发 so 库需要在 `init` 中传入 `NeedDownloadSoistener`
   自行下载，并在下载后调用 `SoFileInfo#insertOrUpdateCache(saveLibsDir,File)` 插入缓存即可，**
   需要在加载前插入缓存 **

## 开始使用:

工件包发布在 [Jitpack](https://jitpack.io/#mcxinyu/Android-So-Handler)

目前插件未发布到 Gradle 官方 Gradle Plugin Portal，所以，需要通过以下方式引入，暂时不能使用 `plugins` 块的方式。

### 引入依赖

此处 `maven("https://jitpack.io")` 也可添加到 `settings.gradle` 的 `dependencyResolutionManagement` 模块中。[参考 settings.gradle.kts](settings.gradle.kts)

```kotlin "build.gradle.kts"
buildscript {
    val userSoPlugin by extra
    val SO_PLUGIN_VERSION by extra
    repositories {
        maven("https://jitpack.io")
    }
    dependencies {
        if (userSoPlugin) {
            classpath("com.github.mcxinyu.Android-So-Handler:load-hook-plugin:${SO_PLUGIN_VERSION}")
            classpath("com.github.mcxinyu.Android-So-Handler:file-plugin:${SO_PLUGIN_VERSION}")
        }
    }
}
```

```kotlin "app/build.gradle.kts"
plugins {
    // ...
    id("com.imf.plugin.so.ApkSoFileAdjustPlugin")
    id("com.imf.plugin.so.SoLoadHookPlugin")
}
dependencies {
    val SO_PLUGIN_VERSION by project
    listOf(
        "com.github.mcxinyu.Android-So-Handler:load-hook:$SO_PLUGIN_VERSION",
        "com.github.mcxinyu.Android-So-Handler:load-assets-7z:$SO_PLUGIN_VERSION"
    ).forEach(::implementation)
}
```

### 配置

参考 [so-file-config.gradle](so-file-config.gradle) groovy 写法。

或者 [build.gradle.kts](app/build.gradle.kts) kts 中使用有条件的动态语法。

下面是 kts 的常规写法，与 groovy 类似：
```kotlin
SoFileConfig {
    /**
     * 总开关配置 不配置时根据 compressSo2AssetsLibs 与 deleteSoLibs 是否为空自动开启关闭 配置 true 强制开启 false 强制关闭
     */
    // enable = false
    // 设置 debug 下不删除与压缩 so 库
    excludeBuildTypes = setOf("debug")
    forceNeededRetainAllDependencies = true
    backupApk = true
    useApktool = true
    // 设置要删除的 so 库
    deleteSoLibs = emptySet()
    // 移除 so 时回调，这里可以做上传云端的逻辑
    onDeleteSo = { file, md5 ->
        // 可以返回一个 so 文件的下载链接，可通过 md5 判断缓存读取链接，如果没有则上传云端并返回链接
        // 作者在实际项目中是在此处将 so 压缩后上传云端，在 NeedDownloadSoListener 中下载，调用 insertOrUpdateCache 前解压。
    }
    // 设置要压缩的库 注意 libun7zip.so 为 7z 解压库不可压缩
    compressSo2AssetsLibs = setOf()
    // 排除依赖
    excludeDependencies = setOf(
        "libun7zip.so",
        "libmmkv.so",
    )
    /**
     * 配置自定义依赖 用于解决 a.so 并未声明依赖 b.so 并且内部通过 dlopen 打开 b.so 或者反射 System.loadLibrary 等跳过 hook 加载 so
     * 库等场景
     */
    customDependencies = mapOf(
        // "libflutter.so" to listOf("libapp.so")
    )
}

SoLoadHookConfig {
    // 默认启用的
    enable = true
    // 是否跳过 R 文件与 BuildConfig
    skipRAndBuildConfig = true
    // 设置跳过的包名, 跳过的包不去 hook 修改后请先 clean
    excludePackage = setOf(
        "com.imf.so"
    )
}
```

### 其他

其他注意事项请阅读[原说明](https://github.com/mcxinyu/Android-So-Handler/blob/main-agp7.x/README.md)
