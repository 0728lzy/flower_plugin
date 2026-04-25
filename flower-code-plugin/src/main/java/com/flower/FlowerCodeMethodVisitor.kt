package com.flower

import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import java.nio.charset.StandardCharsets
import java.util.Base64
import kotlin.random.Random

class FlowerCodeMethodVisitor(
    mv: MethodVisitor,
    private val extension: FlowerCodeConfig,
    private val className: String,
    private val methodName: String
) : MethodVisitor(AsmApi.value, mv) {

    override fun visitLdcInsn(value: Any?) {
        if (extension.stringFogEnabled && value is String && value.isNotEmpty()) {
            val key = Random.nextInt(1, 127)
            mv.visitLdcInsn(encode(value, key))
            mv.visitIntInsn(Opcodes.BIPUSH, key)
            mv.visitMethodInsn(
                Opcodes.INVOKESTATIC,
                extension.stringFogClassName,
                "decode",
                "(Ljava/lang/String;I)Ljava/lang/String;",
                false
            )
            return
        }
        super.visitLdcInsn(value)
    }

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
        if ((extension.injectBeforeReturn || extension.injectAtMethodEnd) && isReturnOpcode(opcode)) {
            injectSafeDeadSnippet()
        }
        super.visitInsn(opcode)
    }

    private fun injectSafeDeadSnippet() {
        val end = Label()
        val marker = listOf("ret#", "chk#", "sig#", "mask#").random() + Random.nextInt(1000, 9999)

        // Runtime-opaque guard. R8 can fold a literal false branch, but it must
        // keep this path because System.nanoTime() is not known at compile time.
        mv.visitMethodInsn(
            Opcodes.INVOKESTATIC,
            "java/lang/System",
            "nanoTime",
            "()J",
            false
        )
        mv.visitLdcInsn(Long.MIN_VALUE)
        mv.visitInsn(Opcodes.LCMP)
        mv.visitJumpInsn(Opcodes.IFNE, end)

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

    private fun encode(value: String, key: Int): String {
        val bytes = value.toByteArray(StandardCharsets.UTF_8)
        for (index in bytes.indices) {
            bytes[index] = (bytes[index].toInt() xor key).toByte()
        }
        return Base64.getEncoder().encodeToString(bytes)
    }
}
