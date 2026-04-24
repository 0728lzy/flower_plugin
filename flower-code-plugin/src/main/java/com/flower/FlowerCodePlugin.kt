package com.flower

import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class FlowerCodePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create("flowerCode", FlowerCodeExtension::class.java)

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
                params.enabled.set(extension.enabled)
                params.targetClasses.set(extension.targetClasses)
                params.protectAllProjectClasses.set(extension.protectAllProjectClasses)
                params.minTemplatesPerMethod.set(extension.minTemplatesPerMethod)
                params.maxTemplatesPerMethod.set(extension.maxTemplatesPerMethod)
                params.excludeMethods.set(extension.excludeMethods.toList())
                params.excludeClassRegexes.set(extension.excludeClassRegexes)
                params.injectAtMethodStart.set(extension.injectAtMethodStart)
                params.injectAtMethodEnd.set(extension.injectAtMethodEnd)
                params.injectBeforeReturn.set(extension.injectBeforeReturn)
            }

            variant.instrumentation.setAsmFramesComputationMode(
                FramesComputationMode.COMPUTE_FRAMES_FOR_INSTRUMENTED_METHODS
            )
        }

        project.afterEvaluate {
            if (extension.enabled) {
                project.logger.lifecycle(
                    "Flower code enabled, protectAllProjectClasses: ${extension.protectAllProjectClasses}, " +
                        "target classes: ${extension.targetClasses.size}, " +
                        "templates per method: ${extension.minTemplatesPerMethod}~${extension.maxTemplatesPerMethod}"
                )
            }
        }
    }
}
