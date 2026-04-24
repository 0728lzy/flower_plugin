package com.flower

import com.flower.templates.BitmaskTemplate
import com.flower.templates.SqrtTemplate
import com.flower.templates.StringMatchTemplate
import com.flower.templates.StringReverseTemplate
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import kotlin.random.Random

/**
 * 花指令方法访问器
 *
 * 在方法的开头、return 前、结尾注入随机选择的花指令模板。
 * 所有花指令的条件永远为 false，JIT 编译器会直接跳过，零运行时开销。
 */
class FlowerCodeMethodVisitor(
    mv: MethodVisitor,
    private val extension: FlowerCodeExtension,
    private val className: String,
    private val methodName: String
) : MethodVisitor(Opcodes.ASM9, mv) {

    private val templates = listOf(
        SqrtTemplate(),
        BitmaskTemplate(),
        StringMatchTemplate(),
        StringReverseTemplate()
    )

    private var localVarOffset = 0
    private var injectedAtStart = false

    override fun visitCode() {
        super.visitCode()

        if (extension.injectAtMethodStart) {
            val count = Random.nextInt(
                extension.minTemplatesPerMethod,
                extension.maxTemplatesPerMethod + 1
            )
            repeat(count) {
                injectRandomTemplate()
            }
            injectedAtStart = true
        }
    }

    override fun visitInsn(opcode: Int) {
        // 在 return 指令前注入
        if (extension.injectBeforeReturn && isReturnOpcode(opcode)) {
            injectRandomTemplate()
        }
        super.visitInsn(opcode)
    }

    override fun visitMaxs(maxStack: Int, maxLocals: Int) {
        // 花指令需要额外的局部变量和操作数栈
        // COMPUTE_MAXS 会自动计算，但预留安全余量
        super.visitMaxs(maxStack + 8, maxLocals + 10)
    }

    private fun injectRandomTemplate() {
        val template = templates[Random.nextInt(templates.size)]
        val startLabel = Label()
        val endLabel = Label()

        // 注入花指令
        template.inject(mv, startLabel, endLabel, localVarOffset)
        localVarOffset += template.localVarsUsed
    }

    private fun isReturnOpcode(opcode: Int): Boolean {
        return opcode in listOf(
            Opcodes.RETURN, Opcodes.IRETURN, Opcodes.LRETURN,
            Opcodes.FRETURN, Opcodes.DRETURN, Opcodes.ARETURN
        )
    }
}
