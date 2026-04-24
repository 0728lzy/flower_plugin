# Flower Protect Plugin 使用说明

`flower-code-plugin` 是一套 Android APK 防护 Gradle 插件，当前封装了：

- 花指令 / 垃圾代码注入：对 class 字节码插入不会影响业务逻辑的干扰代码。
- 资源 ID / 资源文件混淆：集成 ReSChiper，用于 release 资源混淆。
- AAB 转 APK：自动使用 Maven 依赖下载 `bundletool`，不需要手动放 `bundletool.jar`。
- 可选加壳：可调用本地 `dpt.jar` 对最终 APK 做 dex 保护。
- AGP 兼容：AGP 8+ 自动使用 ASM Instrumentation，AGP 4/7 自动降级使用 Transform API。
- DSL 兼容：支持 Kotlin DSL 和 Groovy DSL，也支持老项目 `buildscript/classpath` 接入。

> 当前发布版本：`com.flower:flower-code-plugin:1.0.1`

## 一、插件 ID

插件提供两个 ID：

```text
com.flower.code
com.flower.protect-suite
```

推荐使用：

```text
com.flower.protect-suite
```

它会统一处理花指令、资源混淆、AAB 转 APK、可选加壳。

如果只想注入花指令，则使用：

```text
com.flower.code
```

## 二、仓库凭据配置

插件发布在 GitHub Packages：

```text
https://maven.pkg.github.com/0728lzy/flower_plugin
```

建议把凭据放在用户级 Gradle 配置，不要提交到项目仓库。

Windows 路径：

```text
C:\Users\你的用户名\.gradle\gradle.properties
```

macOS / Linux 路径：

```text
~/.gradle/gradle.properties
```

示例：

```properties
gpr.user=你的GitHub用户名
gpr.key=你的GitHub Token
```

Token 至少需要具备读取 GitHub Packages 的权限。私有包通常需要 `read:packages`，如果要发布插件则还需要 `write:packages`。

## 三、AGP 4 / Groovy DSL 接入方式

老项目一般使用根目录 `build.gradle` 的 `buildscript` 方式。

### 1. 根目录 `build.gradle`

```groovy
buildscript {
    repositories {
        google()
        mavenCentral()
        maven {
            url "https://maven.pkg.github.com/0728lzy/flower_plugin"
            credentials {
                username = findProperty("gpr.user") ?: System.getenv("GPR_USER")
                password = findProperty("gpr.key") ?: System.getenv("GPR_KEY")
            }
        }
    }

    dependencies {
        classpath "com.android.tools.build:gradle:4.2.2"
        classpath "com.flower:flower-code-plugin:1.0.1"
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
```

### 2. App 模块 `build.gradle`

```groovy
apply plugin: "com.android.application"
apply plugin: "com.flower.protect-suite"

protectSuite {
    enabled = true

    // 花指令：debug 默认关闭，release 开启
    flowerEnabled = true
    flowerEnableInDebug = false
    flowerEnableInRelease = true

    // 保护所有当前 App 模块 class
    protectAllProjectClasses = true

    // 每个方法注入 2 到 3 段花指令
    minTemplatesPerMethod = 2
    maxTemplatesPerMethod = 3

    // 排除构造方法、常见基础方法
    excludeMethods = ["<init>", "<clinit>", "toString", "hashCode", "equals"] as Set

    // 建议排除高风险生成类
    excludeClassRegexes = [
        ".*\\/databinding\\/.*",
        ".*\\$.*inlined.*"
    ]

    // 资源混淆
    resChiperEnabled = true
    resChiperConfigFile = "tools/reschiper-config.xml"
    resChiperOutputBundleName = "app-release-obfuscated.aab"

    // bundletool 从 Maven 自动下载
    bundletoolVersion = "1.15.6"

    // 输出目录
    outputDir = "release"
    outputSuffix = "_o"

    // 使用 app 里的 signingConfigs 名称
    signingConfigName = "myConfig"

    // 可选加壳
    dptEnabled = true
    dptJar = "tools/dpt.jar"
    dptExcludeAbi = "x86,x86_64"
}
```

### 3. 执行打包

```bash
./gradlew protectReleaseApk
```

兼容旧任务名：

```bash
./gradlew resDJApkGenerate
```

最终 APK 默认输出到：

```text
release/
```

## 四、AGP 7/8 + Kotlin DSL 接入方式

### 1. `settings.gradle.kts`

```kotlin
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven {
            url = uri("https://maven.pkg.github.com/0728lzy/flower_plugin")
            credentials {
                username = providers.gradleProperty("gpr.user").orNull
                    ?: System.getenv("GPR_USER")
                password = providers.gradleProperty("gpr.key").orNull
                    ?: System.getenv("GPR_KEY")
            }
        }
    }
}
```

### 2. App 模块 `build.gradle.kts`

```kotlin
plugins {
    id("com.android.application")
    id("com.flower.protect-suite") version "1.0.1"
}

protectSuite {
    enabled = true

    flowerEnabled = true
    flowerEnableInDebug = false
    flowerEnableInRelease = true
    protectAllProjectClasses = true

    minTemplatesPerMethod = 2
    maxTemplatesPerMethod = 3

    excludeMethods = mutableSetOf("<init>", "<clinit>", "toString", "hashCode", "equals")
    excludeClassRegexes = mutableListOf(
        ".*\\/databinding\\/.*",
        ".*\\$.*inlined.*"
    )

    resChiperEnabled = true
    resChiperConfigFile = "tools/reschiper-config.xml"

    dptEnabled = true
    dptJar = "tools/dpt.jar"
}
```

## 五、只使用花指令

如果不需要资源混淆和加壳，只想做 class 花指令注入，可以使用 `com.flower.code`。

### Groovy DSL

```groovy
apply plugin: "com.android.application"
apply plugin: "com.flower.code"

flowerCode {
    enabled = true
    enableInDebug = false
    enableInRelease = true

    protectAllProjectClasses = true

    minTemplatesPerMethod = 2
    maxTemplatesPerMethod = 3

    excludeMethods = ["<init>", "<clinit>", "toString", "hashCode", "equals"] as Set
    excludeClassRegexes = [
        ".*\\/databinding\\/.*",
        ".*\\$.*inlined.*"
    ]
}
```

### Kotlin DSL

```kotlin
plugins {
    id("com.android.application")
    id("com.flower.code") version "1.0.1"
}

flowerCode {
    enabled = true
    enableInDebug = false
    enableInRelease = true

    protectAllProjectClasses = true

    minTemplatesPerMethod = 2
    maxTemplatesPerMethod = 3
}
```

## 六、配置项说明

### 花指令配置

| 配置项 | 默认值 | 说明 |
| --- | --- | --- |
| `enabled` | `true` | 总开关 |
| `flowerEnabled` | `true` | `protectSuite` 中的花指令开关 |
| `flowerEnableInDebug` / `enableInDebug` | `false` | debug 是否注入 |
| `flowerEnableInRelease` / `enableInRelease` | `true` | release 是否注入 |
| `protectAllProjectClasses` | `true` / `false` | 是否保护当前 App 模块全部 class |
| `targetClasses` | 空 | 指定保护类，格式如 `com/example/core/FeatureGate` |
| `minTemplatesPerMethod` | `2` | 每个方法最少注入段数 |
| `maxTemplatesPerMethod` | `3` 或 `4` | 每个方法最多注入段数 |
| `excludeMethods` | 常见基础方法 | 不注入的方法名 |
| `excludeClassRegexes` | 空 | 不注入的 class 正则 |
| `injectAtMethodStart` | `true` | 方法开始处注入 |
| `injectAtMethodEnd` | `true` | 方法结束处注入 |
| `injectBeforeReturn` | `true` | return 前注入 |

### 资源混淆配置

| 配置项 | 默认值 | 说明 |
| --- | --- | --- |
| `resChiperEnabled` | `true` | 是否启用资源混淆 |
| `resChiperConfigFile` | `tools/reschiper-config.xml` | ReSChiper 白名单配置 |
| `resChiperOutputBundleName` | `app-release-obfuscated.aab` | 混淆后的 AAB 名称 |
| `mergeDuplicateResources` | `true` | 合并重复资源 |
| `enableFileFiltering` | `false` | 文件过滤 |
| `enableFilterStrings` | `false` | 字符串过滤 |

### 加壳配置

| 配置项 | 默认值 | 说明 |
| --- | --- | --- |
| `dptEnabled` | `true` | 是否调用 DPT 加壳 |
| `dptJar` | `tools/dpt.jar` | 本地 DPT jar 路径 |
| `dptExcludeAbi` | `x86,x86_64` | 排除 ABI |
| `dptDebug` | `false` | DPT debug 模式 |
| `dptKeepClasses` | `false` | 保留 class |
| `dptSmaller` | `false` | 体积优化选项 |

## 七、推荐配置

### debug 开发友好版

```groovy
protectSuite {
    enabled = true
    flowerEnableInDebug = false
    resChiperEnabled = false
    dptEnabled = false
}
```

debug 建议保持关闭，避免影响编译速度、调试体验和增量构建。

### release 强防护版

```groovy
protectSuite {
    enabled = true

    flowerEnabled = true
    flowerEnableInDebug = false
    flowerEnableInRelease = true
    protectAllProjectClasses = true
    minTemplatesPerMethod = 2
    maxTemplatesPerMethod = 3

    resChiperEnabled = true
    dptEnabled = true
}
```

## 八、注意事项

- 不建议一开始就无差别保护所有复杂生成类。`databinding`、Kotlin inline/lambda、协程状态机类如果遇到 D8 报错，可以加入 `excludeClassRegexes`。
- 花指令会增加 class 体积和 D8/R8 压力，保护强度越高，构建越慢，APK 也可能更大。
- 资源混淆主要影响 release 产物，不建议 debug 开启。
- 加壳依赖本地 `tools/dpt.jar`，插件不会托管或自动下载 DPT。
- `bundletool` 不需要手动放 jar，插件会通过 Maven 坐标自动下载。
- AGP 4 会自动走 Transform；AGP 8 会自动走 ASM Instrumentation。

## 九、发布插件

在插件目录执行：

```bash
cd flower-code-plugin
../gradlew publish
```

如果只发布到本地 Maven：

```bash
cd flower-code-plugin
../gradlew publishToMavenLocal
```

发布前确认用户级 `gradle.properties` 中存在：

```properties
gpr.user=你的GitHub用户名
gpr.key=你的GitHub Token
```
