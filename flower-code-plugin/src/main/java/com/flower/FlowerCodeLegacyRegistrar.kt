package com.flower

import com.android.build.gradle.AppExtension
import org.gradle.api.GradleException
import org.gradle.api.Project

object FlowerCodeLegacyRegistrar {
    fun register(project: Project, config: FlowerCodeConfig) {
        val android = project.extensions.findByType(AppExtension::class.java)
            ?: throw GradleException("Flower code plugin can only be applied to Android Application modules.")

        android.registerTransform(FlowerCodeTransform(config))
        project.logger.lifecycle("Flower code registered with legacy Transform API for AGP 4/7 compatibility.")
    }
}
