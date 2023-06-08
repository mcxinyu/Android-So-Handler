package com.imf.plugin.so

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.objectweb.asm.ClassVisitor

open class SoLoadHookPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.extensions.apply {
            val config = create("SoLoadHookConfig", SoLoadHookExtensions::class.java)
            val androidComponents = findByType(AndroidComponentsExtension::class.java)
            androidComponents?.onVariants { variant ->
                if (config.enable) {
                    variant.instrumentation.transformClassesWith(
                        SoLoadHookClassVisitorFactory::class.java,
                        InstrumentationScope.ALL
                    ) {
                        it.enable = config.enable
                        it.skipRAndBuildConfig = config.skipRAndBuildConfig
                        it.excludePackage = config.excludePackage
                    }
                    variant.instrumentation.setAsmFramesComputationMode(FramesComputationMode.COPY_FRAMES)
                }
            }
        }
    }

    abstract class SoLoadHookClassVisitorFactory : AsmClassVisitorFactory<SoLoadHookExtensions> {
        override fun createClassVisitor(
            classContext: ClassContext,
            nextClassVisitor: ClassVisitor
        ): ClassVisitor {
//            println("classContext ${classContext.currentClassData.className}")
            return LoadLibraryVisitor(nextClassVisitor)
        }

        override fun isInstrumentable(classData: ClassData): Boolean {
            val className = classData.className
            return when {
                className.startsWith("androidx.") -> false
                className.startsWith("com.google.android.") -> false
                className.startsWith("android.support.") -> false
                className.startsWith("com.imf.so.") -> false
                className.startsWith("com.imf.plugin.so.") -> false
                parameters.get().excludePackage.any { className.startsWith(it) } -> false
                parameters.get().skipRAndBuildConfig &&
                        className.contains(Regex(".BuildConfig|.R2$*|.R$*")) -> false

                else -> true
            }
        }
    }
}