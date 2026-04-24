package com.flower.templates

import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes.*
import kotlin.random.Random

/**
 * 模板 A: sqrt 永假不等式
 *
 * 还原自 RootCheckUtils / ConfigFactory 中的花指令模式。
 *
 * 生成的等效 Java 代码：
 * ```java
 * int nextInt = new Random().nextInt(10) + 5;       // 5~14
 * int sqrt = (int) Math.sqrt(nextInt * 3);           // 3~6
 * if (sqrt > nextInt + 100) {                        // 永远 false (6 > 105?)
 *     int i = 61680 ^ sqrt;                          // 0xF0F0 ^ sqrt
 *     int i2 = ((i << 4) | (i >> 4)) % 1000;
 *     if (i2 < 0) { i2 = -i2; }
 *     StringBuilder sb = new StringBuilder();
 *     sb.append("ret#");
 *     sb.append(i2);
 * }
 * ```
 *
 * 永假原理：
 * - nextInt 范围 [5, 14]
 * - nextInt * 3 范围 [15, 42]
 * - sqrt(42) ≈ 6.48 → (int)6
 * - nextInt + 100 范围 [105, 114]
 * - 6 > 105 → 永远 false
 */
class SqrtTemplate : FlowerTemplate() {

    override val localVarsUsed: Int = 4 // nextInt, sqrt, i, i2

    override fun inject(mv: MethodVisitor, startLabel: Label, endLabel: Label, localVarBase: Int) {
        val xorConstant = Random.nextInt(0xF000, 0xFFFF) // 类似 0xF0F0 的大常量
        val appendStr = listOf("ret#", "val#", "chk#", "sig#").random()

        val v_nextInt = localVarBase + 1
        val v_sqrt = localVarBase + 2
        val v_i = localVarBase + 3
        val v_i2 = localVarBase + 4

        // --- int nextInt = new Random().nextInt(10) + 5; ---
        mv.visitTypeInsn(NEW, "java/util/Random")
        mv.visitInsn(DUP)
        mv.visitMethodInsn(INVOKESPECIAL, "java/util/Random", "<init>", "()V", false)
        mv.visitIntInsn(BIPUSH, 10)
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/Random", "nextInt", "(I)I", false)
        mv.visitIntInsn(BIPUSH, 5)
        mv.visitInsn(IADD)
        mv.visitVarInsn(ISTORE, v_nextInt)

        // --- int sqrt = (int) Math.sqrt(nextInt * 3); ---
        mv.visitVarInsn(ILOAD, v_nextInt)
        mv.visitIntInsn(BIPUSH, 3)
        mv.visitInsn(IMUL)
        mv.visitInsn(I2D)
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Math", "sqrt", "(D)D", false)
        mv.visitInsn(D2I)
        mv.visitVarInsn(ISTORE, v_sqrt)

        // --- if (sqrt > nextInt + 100) { ---
        mv.visitVarInsn(ILOAD, v_sqrt)
        mv.visitVarInsn(ILOAD, v_nextInt)
        mv.visitIntInsn(BIPUSH, 100)
        mv.visitInsn(IADD)
        mv.visitJumpInsn(IF_ICMPLE, endLabel)  // sqrt <= nextInt+100 → 跳到 end（永远跳）

        // === 以下代码永远不执行 ===

        // int i = xorConstant ^ sqrt;
        mv.visitLdcInsn(xorConstant)
        mv.visitVarInsn(ILOAD, v_sqrt)
        mv.visitInsn(IXOR)
        mv.visitVarInsn(ISTORE, v_i)

        // int i2 = ((i << 4) | (i >> 4)) % 1000;
        mv.visitVarInsn(ILOAD, v_i)
        mv.visitIntInsn(BIPUSH, 4)
        mv.visitInsn(ISHL)
        mv.visitVarInsn(ILOAD, v_i)
        mv.visitIntInsn(BIPUSH, 4)
        mv.visitInsn(ISHR)
        mv.visitInsn(IOR)
        mv.visitIntInsn(SIPUSH, 1000)
        mv.visitInsn(IREM)
        mv.visitVarInsn(ISTORE, v_i2)

        // if (i2 < 0) { i2 = -i2; }
        val labelNotNeg = Label()
        mv.visitVarInsn(ILOAD, v_i2)
        mv.visitJumpInsn(IFGE, labelNotNeg)
        mv.visitVarInsn(ILOAD, v_i2)
        mv.visitInsn(INEG)
        mv.visitVarInsn(ISTORE, v_i2)
        mv.visitLabel(labelNotNeg)

        // StringBuilder sb = new StringBuilder(); sb.append("ret#"); sb.append(i2);
        mv.visitTypeInsn(NEW, "java/lang/StringBuilder")
        mv.visitInsn(DUP)
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false)
        mv.visitLdcInsn(appendStr)
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
            "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false)
        mv.visitVarInsn(ILOAD, v_i2)
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
            "(I)Ljava/lang/StringBuilder;", false)
        mv.visitInsn(POP)

        // --- 花指令结束 ---
        mv.visitLabel(endLabel)
    }
}
