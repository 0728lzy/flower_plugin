package com.flower

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationParameters
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.objectweb.asm.ClassVisitor

interface FlowerCodeAsmParams : InstrumentationParameters {
    val enabled: Property<Boolean>
    val targetClasses: ListProperty<String>
    val protectAllProjectClasses: Property<Boolean>
    val minTemplatesPerMethod: Property<Int>
    val maxTemplatesPerMethod: Property<Int>
    val excludeMethods: ListProperty<String>
    val injectAtMethodStart: Property<Boolean>
    val injectAtMethodEnd: Property<Boolean>
    val injectBeforeReturn: Property<Boolean>
}

abstract class FlowerCodeAsmClassVisitorFactory : AsmClassVisitorFactory<FlowerCodeAsmParams> {
    override fun isInstrumentable(classData: ClassData): Boolean {
        val params = parameters.get()
        if (!params.enabled.get()) return false

        val className = classData.className.replace('.', '/')
        if (isGeneratedOrFrameworkClass(className)) return false

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
            targetClasses = params.targetClasses.get().toMutableList()
            protectAllProjectClasses = params.protectAllProjectClasses.get()
            minTemplatesPerMethod = params.minTemplatesPerMethod.get()
            maxTemplatesPerMethod = params.maxTemplatesPerMethod.get()
            excludeMethods = params.excludeMethods.get().toMutableSet()
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
}
