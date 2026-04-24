package com.flower

open class FlowerCodeExtension {
    /** 是否启用花指令注入 */
    var enabled: Boolean = true

    /** 目标类列表（使用 / 分隔的内部类名格式） */
    var targetClasses: MutableList<String> = mutableListOf()

    /** 每个方法最少注入几段花指令 */
    var minTemplatesPerMethod: Int = 2

    /** 每个方法最多注入几段花指令 */
    var maxTemplatesPerMethod: Int = 4

    /** 排除的方法名（不注入花指令） */
    var excludeMethods: MutableSet<String> = mutableSetOf(
        "<init>", "<clinit>", "toString", "hashCode", "equals"
    )

    /** 是否在方法开头注入 */
    var injectAtMethodStart: Boolean = true

    /** 是否在方法结尾注入 */
    var injectAtMethodEnd: Boolean = true

    /** 是否在 return 语句前注入 */
    var injectBeforeReturn: Boolean = true
}
