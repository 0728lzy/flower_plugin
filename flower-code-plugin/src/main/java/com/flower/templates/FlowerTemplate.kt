package com.flower.templates

import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor

/**
 * 花指令模板基类
 *
 * 设计原则：
 * 1. 条件永远为 false，但静态分析器难以自动证明
 * 2. 内部代码看似有意义（加密/校验/文件操作）
 * 3. 每次注入使用不同的随机常量，避免特征重复
 * 4. 零运行时开销（JIT 会消除死代码）
 */
abstract class FlowerTemplate {

    /** 该模板使用的局部变量数量 */
    abstract val localVarsUsed: Int

    /**
     * 向方法中注入花指令字节码
     *
     * @param mv 方法访问器
     * @param startLabel 花指令开始标签
     * @param endLabel 花指令结束标签（跳过花指令体的目标）
     * @param localVarBase 可用的局部变量起始索引
     */
    abstract fun inject(
        mv: MethodVisitor,
        startLabel: Label,
        endLabel: Label,
        localVarBase: Int
    )

    /** 生成随机的 16 字符伪密钥字符串 */
    protected fun randomKeyString(): String {
        val chars = "abcdefghijklmnopqrstuvwxyz0123456789"
        return (1..16).map { chars.random() }.joinToString("")
    }

    /** 生成随机的伪邮箱 */
    protected fun randomEmail(): String {
        val user = (1..4).map { ('a'..'z').random() }.joinToString("")
        val domains = listOf("163.com", "qq.com", "gmail.com", "126.com")
        return "$user@${domains.random()}"
    }
}
