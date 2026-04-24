package com.flower

import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import kotlin.random.Random

class FlowerCodeMethodVisitor(
    mv: MethodVisitor,
    private val extension: FlowerCodeExtension,
    private val className: String,
    private val methodName: String
) : MethodVisitor(Opcodes.ASM9, mv) {

    override fun visitCode() {
        super.visitCode()
        if (extension.injectAtMethodStart) {
            val count = Random.nextInt(
                extension.minTemplatesPerMethod,
                extension.maxTemplatesPerMethod + 1
            )
            repeat(count) { injectSafeDeadSnippet() }
        }
    }

    override fun visitInsn(opcode: Int) {
        if (extension.injectBeforeReturn && isReturnOpcode(opcode)) {
            injectSafeDeadSnippet()
        }
        super.visitInsn(opcode)
    }

    private fun injectSafeDeadSnippet() {
        val end = Label()
        val marker = listOf("ret#", "chk#", "sig#", "mask#").random() + Random.nextInt(1000, 9999)

        // Always jump; no local variable writes, so verifier frames stay stable.
        mv.visitInsn(Opcodes.ICONST_0)
        mv.visitJumpInsn(Opcodes.IFEQ, end)

        mv.visitTypeInsn(Opcodes.NEW, "java/lang/StringBuilder")
        mv.visitInsn(Opcodes.DUP)
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false)
        mv.visitLdcInsn(marker)
        mv.visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            "java/lang/StringBuilder",
            "append",
            "(Ljava/lang/String;)Ljava/lang/StringBuilder;",
            false
        )
        mv.visitMethodInsn(
            Opcodes.INVOKESTATIC,
            "java/lang/System",
            "nanoTime",
            "()J",
            false
        )
        mv.visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            "java/lang/StringBuilder",
            "append",
            "(J)Ljava/lang/StringBuilder;",
            false
        )
        mv.visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            "java/lang/StringBuilder",
            "toString",
            "()Ljava/lang/String;",
            false
        )
        mv.visitInsn(Opcodes.POP)
        mv.visitLabel(end)
    }

    private fun isReturnOpcode(opcode: Int): Boolean {
        return opcode == Opcodes.RETURN ||
            opcode == Opcodes.IRETURN ||
            opcode == Opcodes.LRETURN ||
            opcode == Opcodes.FRETURN ||
            opcode == Opcodes.DRETURN ||
            opcode == Opcodes.ARETURN
    }
}
