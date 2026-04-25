package com.flower

import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter

internal class SafeClassWriter(
    classReader: ClassReader,
    flags: Int
) : ClassWriter(classReader, flags) {
    override fun getCommonSuperClass(type1: String, type2: String): String {
        return runCatching {
            super.getCommonSuperClass(type1, type2)
        }.getOrDefault("java/lang/Object")
    }
}
