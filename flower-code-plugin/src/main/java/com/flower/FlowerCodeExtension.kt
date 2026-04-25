package com.flower

interface FlowerCodeConfig {
    val enabled: Boolean
    val enableInDebug: Boolean
    val enableInRelease: Boolean
    val targetClasses: MutableList<String>
    val protectAllProjectClasses: Boolean
    val minTemplatesPerMethod: Int
    val maxTemplatesPerMethod: Int
    val excludeMethods: MutableSet<String>
    val excludeClassRegexes: MutableList<String>
    val injectAtMethodStart: Boolean
    val injectAtMethodEnd: Boolean
    val injectBeforeReturn: Boolean
    val stringFogEnabled: Boolean
    val stringFogClassName: String
}

open class FlowerCodeExtension : FlowerCodeConfig {
    override var enabled: Boolean = true
    override var enableInDebug: Boolean = false
    override var enableInRelease: Boolean = true

    // Internal class names using "/" separator, e.g. com/example/MyClass
    override var targetClasses: MutableList<String> = mutableListOf()

    // When true, inject all classes from current app module.
    override var protectAllProjectClasses: Boolean = false

    override var minTemplatesPerMethod: Int = 2
    override var maxTemplatesPerMethod: Int = 4

    override var excludeMethods: MutableSet<String> = mutableSetOf(
        "<init>", "<clinit>", "toString", "hashCode", "equals"
    )

    // Internal class name regexes using "/" separator.
    override var excludeClassRegexes: MutableList<String> = mutableListOf()

    override var injectAtMethodStart: Boolean = true
    override var injectAtMethodEnd: Boolean = true
    override var injectBeforeReturn: Boolean = true

    override var stringFogEnabled: Boolean = false
    override var stringFogClassName: String = "com/flower/runtime/StringFog"
}
