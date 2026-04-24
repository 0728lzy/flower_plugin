package com.flower.templates

import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes.*
import kotlin.random.Random

/**
 * 模板 B: 位运算永假链
 *
 * 还原自 ConfigFactory / BaseConfigModel 中的花指令模式。
 *
 * 生成的等效 Java 代码：
 * ```java
 * int nextInt = new Random().nextInt(10) & 170;    // 0xAA
 * int i = (nextInt >> 7) | (nextInt << 1);
 * boolean z = (i & 240) == 160;                     // 0xF0 == 0xA0
 * if (z) {
 *     boolean z2 = (i & 15) != 5;
 *     if (z2) {
 *         boolean z3 = (z & z2) | ((!z) & (!z2));   // XNOR
 *         System.out.println("getInfo$=" + z3);
 *     }
 * }
 * ```
 *
 * 永假原理：
 * - nextInt(10) 范围 [0, 9]
 * - & 170(0xAA=10101010) 后可能值 = {0, 2, 8}
 * - (x>>7)|(x<<1) 对 {0,2,8} → {0, 4, 16}
 * - {0,4,16} & 240(0xF0) → {0, 0, 16}
 * - 都不等于 160(0xA0) → z 永远 false
 */
class BitmaskTemplate : FlowerTemplate() {

    override val localVarsUsed: Int = 4

    override fun inject(mv: MethodVisitor, startLabel: Label, endLabel: Label, localVarBase: Int) {
        val printKey = listOf("getInfo\$=", "checkVal\$=", "verify\$=", "state\$=").random()

        val v_nextInt = localVarBase + 1
        val v_i = localVarBase + 2
        val v_z = localVarBase + 3
        val v_z2 = localVarBase + 4

        // --- int nextInt = new Random().nextInt(10) & 170; ---
        mv.visitTypeInsn(NEW, "java/util/Random")
        mv.visitInsn(DUP)
        mv.visitMethodInsn(INVOKESPECIAL, "java/util/Random", "<init>", "()V", false)
        mv.visitIntInsn(BIPUSH, 10)
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/Random", "nextInt", "(I)I", false)
        mv.visitIntInsn(SIPUSH, 170) // 0xAA
        mv.visitInsn(IAND)
        mv.visitVarInsn(ISTORE, v_nextInt)

        // --- int i = (nextInt >> 7) | (nextInt << 1); ---
        mv.visitVarInsn(ILOAD, v_nextInt)
        mv.visitIntInsn(BIPUSH, 7)
        mv.visitInsn(ISHR)
        mv.visitVarInsn(ILOAD, v_nextInt)
        mv.visitInsn(ICONST_1)
        mv.visitInsn(ISHL)
        mv.visitInsn(IOR)
        mv.visitVarInsn(ISTORE, v_i)

        // --- boolean z = (i & 240) == 160; ---
        // if ((i & 240) != 160) goto end
        mv.visitVarInsn(ILOAD, v_i)
        mv.visitIntInsn(SIPUSH, 240) // 0xF0
        mv.visitInsn(IAND)
        mv.visitIntInsn(SIPUSH, 160) // 0xA0
        mv.visitJumpInsn(IF_ICMPNE, endLabel)  // 永远跳到 end

        // === 以下永远不执行 ===

        // boolean z2 = (i & 15) != 5;
        mv.visitVarInsn(ILOAD, v_i)
        mv.visitIntInsn(BIPUSH, 15) // 0x0F
        mv.visitInsn(IAND)
        mv.visitIntInsn(BIPUSH, 5)
        val labelZ2False = Label()
        val labelZ2Done = Label()
        mv.visitJumpInsn(IF_ICMPEQ, labelZ2False)
        mv.visitInsn(ICONST_1)
        mv.visitJumpInsn(GOTO, labelZ2Done)
        mv.visitLabel(labelZ2False)
        mv.visitInsn(ICONST_0)
        mv.visitLabel(labelZ2Done)
        mv.visitVarInsn(ISTORE, v_z2)

        // if (z2) { System.out.println("getInfo$=" + z3); }
        mv.visitVarInsn(ILOAD, v_z2)
        mv.visitJumpInsn(IFEQ, endLabel)

        // System.out.println("getInfo$=" + z3)
        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;")
        mv.visitTypeInsn(NEW, "java/lang/StringBuilder")
        mv.visitInsn(DUP)
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false)
        mv.visitLdcInsn(printKey)
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
            "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false)
        mv.visitVarInsn(ILOAD, v_z2) // 用 z2 代替 z3（反正不执行）
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
            "(Z)Ljava/lang/StringBuilder;", false)
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString",
            "()Ljava/lang/String;", false)
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println",
            "(Ljava/lang/String;)V", false)

        mv.visitLabel(endLabel)
    }
}
