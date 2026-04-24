package com.flower

open class FlowerCodeExtension {
    var enabled: Boolean = true
    var enableInDebug: Boolean = false
    var enableInRelease: Boolean = true

    // Internal class names using "/" separator, e.g. com/example/MyClass
    var targetClasses: MutableList<String> = mutableListOf()

    // When true, inject all classes from current app module.
    var protectAllProjectClasses: Boolean = false

    var minTemplatesPerMethod: Int = 2
    var maxTemplatesPerMethod: Int = 4

    var excludeMethods: MutableSet<String> = mutableSetOf(
        "<init>", "<clinit>", "toString", "hashCode", "equals"
    )

    // Internal class name regexes using "/" separator.
    var excludeClassRegexes: MutableList<String> = mutableListOf()

    var injectAtMethodStart: Boolean = true
    var injectAtMethodEnd: Boolean = true
    var injectBeforeReturn: Boolean = true
}
