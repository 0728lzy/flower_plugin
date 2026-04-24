package com.flower.templates

import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes.*
import kotlin.random.Random

/**
 * 模板 C: 字符串永假匹配
 *
 * 还原自 InitConfigs / RootCheckUtils 中的花指令模式。
 *
 * 生成的等效 Java 代码：
 * ```java
 * if (String.valueOf(new Random().nextInt(110)).contains("systemMillis")) {
 *     // nextInt(110) 产生 "0"~"109"，最长 3 字符
 *     // "systemMillis" 有 12 字符 → contains 永远 false
 *     Matcher matcher = Pattern.compile(
 *         "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$"
 *     ).matcher("fsje@163.com");
 *     if (matcher.matches()) {
 *         boolean z = matcher.group(1).length() > matcher.group(2).length();
 *         System.out.println("getPatternResult=" + z);
 *     }
 * }
 * ```
 *
 * 永假原理：
 * - Random().nextInt(110) → "0" 到 "109"，最长 3 个字符
 * - "systemMillis" 有 12 个字符
 * - 3 字符的字符串不可能 contains 12 字符的子串
 */
class StringMatchTemplate : FlowerTemplate() {

    override val localVarsUsed: Int = 2

    // 这些字符串永远不可能出现在 nextInt(N) 的结果中
    private val impossibleStrings = listOf(
        "systemMillis", "localThread", "mainHandler",
        "contextWrapper", "serviceManager", "activityStack",
        "networkCallback", "packageResolver", "contentProvider"
    )

    override fun inject(mv: MethodVisitor, startLabel: Label, endLabel: Label, localVarBase: Int) {
        val randBound = Random.nextInt(100, 150) // 产生 1~3 位数
        val searchStr = impossibleStrings.random()
        val email = randomEmail()
        val printKey = listOf("getPatternResult=", "matchResult=", "validateResult=").random()

        val v_matcher = localVarBase + 1

        // --- String.valueOf(new Random().nextInt(N)) ---
        mv.visitTypeInsn(NEW, "java/util/Random")
        mv.visitInsn(DUP)
        mv.visitMethodInsn(INVOKESPECIAL, "java/util/Random", "<init>", "()V", false)
        mv.visitIntInsn(SIPUSH, randBound)
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/Random", "nextInt", "(I)I", false)
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/String", "valueOf",
            "(I)Ljava/lang/String;", false)

        // --- .contains("systemMillis") ---
        mv.visitLdcInsn(searchStr)
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "contains",
            "(Ljava/lang/CharSequence;)Z", false)
        mv.visitJumpInsn(IFEQ, endLabel) // contains == false → 跳到 end（永远跳）

        // === 以下永远不执行 ===

        // Pattern.compile(emailRegex).matcher(email)
        mv.visitLdcInsn("^([a-zA-Z0-9_\\\\-\\\\.]+)@([a-zA-Z0-9_\\\\-\\\\.]+)\\\\.([a-zA-Z]{2,5})\$")
        mv.visitMethodInsn(INVOKESTATIC, "java/util/regex/Pattern", "compile",
            "(Ljava/lang/String;)Ljava/util/regex/Pattern;", false)
        mv.visitLdcInsn(email)
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/regex/Pattern", "matcher",
            "(Ljava/lang/CharSequence;)Ljava/util/regex/Matcher;", false)
        mv.visitVarInsn(ASTORE, v_matcher)

        // if (matcher.matches()) { ... }
        mv.visitVarInsn(ALOAD, v_matcher)
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/regex/Matcher", "matches", "()Z", false)
        mv.visitJumpInsn(IFEQ, endLabel)

        // boolean z = matcher.group(1).length() > matcher.group(2).length();
        mv.visitVarInsn(ALOAD, v_matcher)
        mv.visitInsn(ICONST_1)
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/regex/Matcher", "group",
            "(I)Ljava/lang/String;", false)
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "length", "()I", false)
        mv.visitVarInsn(ALOAD, v_matcher)
        mv.visitInsn(ICONST_2)
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/regex/Matcher", "group",
            "(I)Ljava/lang/String;", false)
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "length", "()I", false)

        val labelFalse = Label()
        val labelDone = Label()
        mv.visitJumpInsn(IF_ICMPLE, labelFalse)
        mv.visitInsn(ICONST_1)
        mv.visitJumpInsn(GOTO, labelDone)
        mv.visitLabel(labelFalse)
        mv.visitInsn(ICONST_0)
        mv.visitLabel(labelDone)

        // System.out.println("getPatternResult=" + z)
        val v_z = localVarBase + 2
        mv.visitVarInsn(ISTORE, v_z)
        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;")
        mv.visitTypeInsn(NEW, "java/lang/StringBuilder")
        mv.visitInsn(DUP)
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false)
        mv.visitLdcInsn(printKey)
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
            "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false)
        mv.visitVarInsn(ILOAD, v_z)
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
            "(Z)Ljava/lang/StringBuilder;", false)
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString",
            "()Ljava/lang/String;", false)
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println",
            "(Ljava/lang/String;)V", false)

        mv.visitLabel(endLabel)
    }
}
