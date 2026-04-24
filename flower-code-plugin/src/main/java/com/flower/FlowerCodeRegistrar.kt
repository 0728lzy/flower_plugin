package com.flower

import org.gradle.api.GradleException
import org.gradle.api.Project

object FlowerCodeRegistrar {
    fun register(project: Project, config: FlowerCodeConfig) {
        if (hasAgp8Instrumentation(project)) {
            invokeAgp8Registrar(project, config)
            return
        }

        FlowerCodeLegacyRegistrar.register(project, config)
    }

    private fun hasAgp8Instrumentation(project: Project): Boolean {
        val classLoader = javaClass.classLoader
        val hasApi = runCatching {
            Class.forName(
                "com.android.build.api.variant.ApplicationAndroidComponentsExtension",
                false,
                classLoader
            )
        }.isSuccess

        return hasApi && project.extensions.findByName("androidComponents") != null
    }

    private fun invokeAgp8Registrar(project: Project, config: FlowerCodeConfig) {
        try {
            val registrarClass = Class.forName("com.flower.FlowerCodeAgp8Registrar")
            val registrar = registrarClass.getField("INSTANCE").get(null)
            registrarClass
                .getMethod("register", Project::class.java, FlowerCodeConfig::class.java)
                .invoke(registrar, project, config)
        } catch (error: ReflectiveOperationException) {
            throw GradleException("Failed to register AGP 8 flower instrumentation.", error)
        }
    }
}
