package com.flower

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationParameters
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.objectweb.asm.ClassVisitor

interface FlowerCodeAsmParams : InstrumentationParameters {
    @get:Input
    val enabled: Property<Boolean>
    @get:Input
    val enableInDebug: Property<Boolean>
    @get:Input
    val enableInRelease: Property<Boolean>
    @get:Input
    val variantName: Property<String>
    @get:Input
    val targetClasses: ListProperty<String>
    @get:Input
    val protectAllProjectClasses: Property<Boolean>
    @get:Input
    val minTemplatesPerMethod: Property<Int>
    @get:Input
    val maxTemplatesPerMethod: Property<Int>
    @get:Input
    val excludeMethods: ListProperty<String>
    @get:Input
    val excludeClassRegexes: ListProperty<String>
    @get:Input
    val injectAtMethodStart: Property<Boolean>
    @get:Input
    val injectAtMethodEnd: Property<Boolean>
    @get:Input
    val injectBeforeReturn: Property<Boolean>
}

abstract class FlowerCodeAsmClassVisitorFactory : AsmClassVisitorFactory<FlowerCodeAsmParams> {
    override fun isInstrumentable(classData: ClassData): Boolean {
        val params = parameters.get()
        if (!params.enabled.get()) return false
        if (!isVariantEnabled(params)) return false

        val className = classData.className.replace('.', '/')
        if (isGeneratedOrFrameworkClass(className)) return false
        if (params.excludeClassRegexes.get().any { regex -> Regex(regex).matches(className) }) return false

        if (params.protectAllProjectClasses.get()) return true

        val targets = params.targetClasses.get()
        return targets.any { target ->
            className == target || className.startsWith("$target$")
        }
    }

    override fun createClassVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor {
        val params = parameters.get()
        val extension = FlowerCodeExtension().apply {
            enabled = params.enabled.get()
            enableInDebug = params.enableInDebug.get()
            enableInRelease = params.enableInRelease.get()
            targetClasses = params.targetClasses.get().toMutableList()
            protectAllProjectClasses = params.protectAllProjectClasses.get()
            minTemplatesPerMethod = params.minTemplatesPerMethod.get()
            maxTemplatesPerMethod = params.maxTemplatesPerMethod.get()
            excludeMethods = params.excludeMethods.get().toMutableSet()
            excludeClassRegexes = params.excludeClassRegexes.get().toMutableList()
            injectAtMethodStart = params.injectAtMethodStart.get()
            injectAtMethodEnd = params.injectAtMethodEnd.get()
            injectBeforeReturn = params.injectBeforeReturn.get()
        }
        return FlowerCodeClassVisitor(nextClassVisitor, extension)
    }

    private fun isGeneratedOrFrameworkClass(className: String): Boolean {
        val simpleName = className.substringAfterLast('/')
        return simpleName == "R" ||
            simpleName.startsWith("R$") ||
            simpleName == "BuildConfig" ||
            simpleName == "Manifest" ||
            simpleName.startsWith("Manifest$")
    }

    private fun isVariantEnabled(params: FlowerCodeAsmParams): Boolean {
        val variant = params.variantName.get().lowercase()
        return when {
            "debug" in variant -> params.enableInDebug.get()
            "release" in variant -> params.enableInRelease.get()
            else -> params.enableInRelease.get()
        }
    }
}
