package com.flower

import org.gradle.api.Plugin
import org.gradle.api.Project

class FlowerCodePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create("flowerCode", FlowerCodeExtension::class.java)
        FlowerCodeRegistrar.register(project, extension)

        project.afterEvaluate {
            if (extension.enabled) {
                project.logger.lifecycle(
                    "Flower code enabled, debug: ${extension.enableInDebug}, release: ${extension.enableInRelease}, " +
                        "protectAllProjectClasses: ${extension.protectAllProjectClasses}, " +
                        "target classes: ${extension.targetClasses.size}, " +
                        "templates per method: ${extension.minTemplatesPerMethod}~${extension.maxTemplatesPerMethod}"
                )
            }
        }
    }
}
