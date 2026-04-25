package com.flower

import org.objectweb.asm.Opcodes

internal object AsmApi {
    val value: Int = listOf("ASM9", "ASM8", "ASM7", "ASM6", "ASM5", "ASM4")
        .firstNotNullOfOrNull(::readAsmApiField)
        ?: Opcodes.ASM4

    private fun readAsmApiField(fieldName: String): Int? {
        return runCatching {
            Opcodes::class.java.getField(fieldName).getInt(null)
        }.getOrNull()
    }
}
