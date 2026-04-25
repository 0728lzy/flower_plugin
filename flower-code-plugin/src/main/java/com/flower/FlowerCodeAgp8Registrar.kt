package com.flower

import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import org.gradle.api.Project

object FlowerCodeAgp8Registrar {
    fun register(project: Project, config: FlowerCodeConfig) {
        val androidComponents = project.extensions.findByType(
            ApplicationAndroidComponentsExtension::class.java
        ) ?: throw IllegalStateException(
            "Flower code plugin can only be applied to Android Application modules"
        )

        androidComponents.onVariants { variant ->
            variant.instrumentation.transformClassesWith(
                FlowerCodeAsmClassVisitorFactory::class.java,
                InstrumentationScope.PROJECT
            ) { params ->
                params.enabled.set(config.enabled)
                params.enableInDebug.set(config.enableInDebug)
                params.enableInRelease.set(config.enableInRelease)
                params.variantName.set(variant.name)
                params.targetClasses.set(config.targetClasses)
                params.protectAllProjectClasses.set(config.protectAllProjectClasses)
                params.minTemplatesPerMethod.set(config.minTemplatesPerMethod)
                params.maxTemplatesPerMethod.set(config.maxTemplatesPerMethod)
                params.excludeMethods.set(config.excludeMethods.toList())
                params.excludeClassRegexes.set(config.excludeClassRegexes)
                params.injectAtMethodStart.set(config.injectAtMethodStart)
                params.injectAtMethodEnd.set(config.injectAtMethodEnd)
                params.injectBeforeReturn.set(config.injectBeforeReturn)
                params.stringFogEnabled.set(false)
                params.stringFogClassName.set(config.stringFogClassName)
            }

            variant.instrumentation.setAsmFramesComputationMode(
                FramesComputationMode.COMPUTE_FRAMES_FOR_INSTRUMENTED_METHODS
            )
        }
    }
}
