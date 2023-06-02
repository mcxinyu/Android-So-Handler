package com.imf.plugin.so

import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

open class SoLoadHookPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.extensions.apply {
            val soLoadHookConfig = create("SoLoadHookConfig", SoLoadHookExtensions::class.java)
            val androidComponents = findByType(AndroidComponentsExtension::class.java)
            androidComponents?.onVariants {
                println("SoLoadHookPlugin ${it.name}")
            }
        }
    }
}