package com.flower.templates

import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes.*
import kotlin.random.Random

/**
 * 模板 D: 字符串反转混淆
 *
 * 还原自 ConfigFactory / BaseConfigModel getter/setter 中的花指令模式。
 *
 * 生成的等效 Java 代码：
 * ```java
 * StringBuilder sb = new StringBuilder(String.valueOf(new Random().nextInt(12)));
 * if (sb.length() > new Random().nextInt(10) + 100) {
 *     // sb 最长 2 字符，100~109 → 2 > 100? 永远 false
 *     StringBuilder reverse = sb.reverse();
 *     reverse.append("y38dinsx27chmrw1");  // 伪密钥
 *     reverse.delete(2, 4);
 *     String substring = sb.toString().substring(0, 5);
 *     substring.replace('x', 'a');
 *     StringBuilder sb2 = new StringBuilder();
 *     sb2.append("rw16bg");                // 另一段伪密钥
 *     sb2.append(substring);
 * }
 * ```
 *
 * 永假原理：
 * - Random().nextInt(12) → "0" 到 "11"，长度 1~2
 * - Random().nextInt(10) + 100 → 100~109
 * - sb.length() 最大 2，2 > 100 永远 false
 */
class StringReverseTemplate : FlowerTemplate() {

    override val localVarsUsed: Int = 3

    override fun inject(mv: MethodVisitor, startLabel: Label, endLabel: Label, localVarBase: Int) {
        val fakeKey1 = randomKeyString()
        val fakeKey2 = randomKeyString().take(6)

        val v_sb = localVarBase + 1
        val v_threshold = localVarBase + 2

        // --- StringBuilder sb = new StringBuilder(String.valueOf(new Random().nextInt(12))); ---
        mv.visitTypeInsn(NEW, "java/lang/StringBuilder")
        mv.visitInsn(DUP)

        // String.valueOf(new Random().nextInt(12))
        mv.visitTypeInsn(NEW, "java/util/Random")
        mv.visitInsn(DUP)
        mv.visitMethodInsn(INVOKESPECIAL, "java/util/Random", "<init>", "()V", false)
        mv.visitIntInsn(BIPUSH, 12)
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/Random", "nextInt", "(I)I", false)
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/String", "valueOf",
            "(I)Ljava/lang/String;", false)

        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>",
            "(Ljava/lang/String;)V", false)
        mv.visitVarInsn(ASTORE, v_sb)

        // --- if (sb.length() > new Random().nextInt(10) + 100) ---
        mv.visitVarInsn(ALOAD, v_sb)
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "length", "()I", false)

        // new Random().nextInt(10) + 100
        mv.visitTypeInsn(NEW, "java/util/Random")
        mv.visitInsn(DUP)
        mv.visitMethodInsn(INVOKESPECIAL, "java/util/Random", "<init>", "()V", false)
        mv.visitIntInsn(BIPUSH, 10)
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/Random", "nextInt", "(I)I", false)
        mv.visitIntInsn(BIPUSH, 100)
        mv.visitInsn(IADD)

        mv.visitJumpInsn(IF_ICMPLE, endLabel) // length <= threshold → 跳到 end（永远跳）

        // === 以下永远不执行 ===

        // sb.reverse()
        mv.visitVarInsn(ALOAD, v_sb)
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "reverse",
            "()Ljava/lang/StringBuilder;", false)

        // .append("y38dinsx27chmrw1")
        mv.visitLdcInsn(fakeKey1)
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
            "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false)

        // .delete(2, 4)
        mv.visitInsn(ICONST_2)
        mv.visitInsn(ICONST_4)
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "delete",
            "(II)Ljava/lang/StringBuilder;", false)
        mv.visitInsn(POP)

        // String substring = sb.toString().substring(0, 5)
        mv.visitVarInsn(ALOAD, v_sb)
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString",
            "()Ljava/lang/String;", false)
        mv.visitInsn(ICONST_0)
        mv.visitIntInsn(BIPUSH, 5)
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "substring",
            "(II)Ljava/lang/String;", false)

        // .replace('x', 'a')
        mv.visitIntInsn(BIPUSH, 'x'.code)
        mv.visitIntInsn(BIPUSH, 'a'.code)
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "replace",
            "(CC)Ljava/lang/String;", false)
        mv.visitInsn(POP)

        // StringBuilder sb2 = new StringBuilder(); sb2.append("rw16bg"); sb2.append(substring);
        mv.visitTypeInsn(NEW, "java/lang/StringBuilder")
        mv.visitInsn(DUP)
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false)
        mv.visitLdcInsn(fakeKey2)
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
            "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false)
        mv.visitLdcInsn("placeholder") // 占位
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
            "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false)
        mv.visitInsn(POP)

        mv.visitLabel(endLabel)
    }
}
