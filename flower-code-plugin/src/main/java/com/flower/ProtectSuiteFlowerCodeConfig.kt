package com.flower

class ProtectSuiteFlowerCodeConfig(
    private val extension: ProtectSuiteExtension
) : FlowerCodeConfig {
    override val enabled: Boolean
        get() = extension.enabled && extension.flowerEnabled
    override val enableInDebug: Boolean
        get() = extension.flowerEnableInDebug
    override val enableInRelease: Boolean
        get() = extension.flowerEnableInRelease
    override val targetClasses: MutableList<String>
        get() = extension.targetClasses
    override val protectAllProjectClasses: Boolean
        get() = extension.protectAllProjectClasses
    override val minTemplatesPerMethod: Int
        get() = extension.minTemplatesPerMethod
    override val maxTemplatesPerMethod: Int
        get() = extension.maxTemplatesPerMethod
    override val excludeMethods: MutableSet<String>
        get() = extension.excludeMethods
    override val excludeClassRegexes: MutableList<String>
        get() = extension.excludeClassRegexes
    override val injectAtMethodStart: Boolean
        get() = extension.injectAtMethodStart
    override val injectAtMethodEnd: Boolean
        get() = extension.injectAtMethodEnd
    override val injectBeforeReturn: Boolean
        get() = extension.injectBeforeReturn
}
