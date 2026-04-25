package com.flower

open class ProtectSuiteExtension {
    var enabled: Boolean = true

    var flowerEnabled: Boolean = true
    var flowerEnableInDebug: Boolean = false
    var flowerEnableInRelease: Boolean = true
    var protectAllProjectClasses: Boolean = true
    var targetClasses: MutableList<String> = mutableListOf()
    var minTemplatesPerMethod: Int = 2
    var maxTemplatesPerMethod: Int = 3
    var excludeMethods: MutableSet<String> = mutableSetOf(
        "<init>", "<clinit>", "toString", "hashCode", "equals"
    )
    var excludeClassRegexes: MutableList<String> = mutableListOf()
    var injectAtMethodStart: Boolean = true
    var injectAtMethodEnd: Boolean = true
    var injectBeforeReturn: Boolean = true
    var stringFogEnabled: Boolean = true
    var stringFogClassName: String = "com/flower/runtime/StringFog"

    var resChiperEnabled: Boolean = true
    var resChiperConfigFile: String = "tools/reschiper-config.xml"
    var resChiperOutputBundleName: String = "app-release-obfuscated.aab"
    var resChiperObfuscationMode: String = "default"
    var mergeDuplicateResources: Boolean = true
    var enableFileFiltering: Boolean = false
    var enableFilterStrings: Boolean = false

    var bundletoolVersion: String = "1.15.6"
    var outputDir: String = "release"
    var outputSuffix: String = "_o"
    var signingConfigName: String = "myConfig"

    var dptEnabled: Boolean = true
    var dptJar: String = "tools/dpt.jar"
    var dptExcludeAbi: String = "x86,x86_64"
    var dptDebug: Boolean = false
    var dptDisableAcf: Boolean = false
    var dptDumpCode: Boolean = false
    var dptNoisyLog: Boolean = false
    var dptKeepClasses: Boolean = false
    var dptSmaller: Boolean = false
    var dptVerifySign: Boolean = false
    var dptRulesFile: String? = null
    var dptProtectConfig: String? = null
}
