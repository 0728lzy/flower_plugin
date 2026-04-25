package com.flower

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class FlowerCodeClassVisitor(
    cv: ClassVisitor,
    private val extension: FlowerCodeConfig
) : ClassVisitor(AsmApi.value, cv) {
    private var className: String = ""

    override fun visit(
        version: Int, access: Int, name: String,
        signature: String?, superName: String?, interfaces: Array<String>?
    ) {
        className = name
        super.visit(version, access, name, signature, superName, interfaces)
    }

    override fun visitMethod(
        access: Int, name: String, descriptor: String,
        signature: String?, exceptions: Array<String>?
    ): MethodVisitor {
        val mv = super.visitMethod(access, name, descriptor, signature, exceptions)

        // 排除构造方法、toString、hashCode 等
        if (name in extension.excludeMethods) {
            return mv
        }

        // 排除 abstract/native 方法
        if ((access and Opcodes.ACC_ABSTRACT) != 0 || (access and Opcodes.ACC_NATIVE) != 0) {
            return mv
        }

        return FlowerCodeMethodVisitor(mv, extension, className, name)
    }
}
