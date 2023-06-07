package com.imf.plugin.so

import com.android.build.api.instrumentation.InstrumentationParameters
import org.gradle.api.tasks.Input
import java.io.Serializable

open class SoLoadHookExtensions : InstrumentationParameters, Serializable {
    @get:Input
    var excludePackage: Set<String> = emptySet()

    @get:Input
    var skipRAndBuildConfig: Boolean = true
}

