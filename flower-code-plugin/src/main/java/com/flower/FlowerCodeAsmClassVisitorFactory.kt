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
            minTemplatesPerMethod = params.minTemplatesPerMethod.get()
            maxTemplatesPerMethod = params.maxTemplatesPerMethod.get()
            excludeMethods = params.excludeMethods.get().toMutableSet()
            injectAtMethodStart = params.injectAtMethodStart.get()
            injectAtMethodEnd = params.injectAtMethodEnd.get()
            injectBeforeReturn = params.injectBeforeReturn.get()
        }
        return FlowerCodeClassVisitor(nextClassVisitor, extension)
    }
}
