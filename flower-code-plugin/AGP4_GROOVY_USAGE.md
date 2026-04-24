# AGP 4 / Groovy DSL Usage

This plugin supports two Android Gradle Plugin paths:

- AGP 8+: uses Android Components ASM instrumentation.
- AGP 4.x/7.x: falls back to the legacy Transform API.

## Root `build.gradle`

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
```

Keep credentials outside the project when possible, for example in `~/.gradle/gradle.properties`:

```properties
gpr.user=your_github_user
gpr.key=your_github_package_token
```

## App `build.gradle`

```groovy
apply plugin: "com.android.application"
apply plugin: "com.flower.protect-suite"

protectSuite {
    enabled = true

    // Debug is developer-friendly by default.
    flowerEnableInDebug = false
    flowerEnableInRelease = true

    // Protect all app module classes. Framework/generated classes are skipped.
    protectAllProjectClasses = true
    minTemplatesPerMethod = 2
    maxTemplatesPerMethod = 3

    excludeMethods = ["<init>", "<clinit>", "toString", "hashCode", "equals"] as Set
    excludeClassRegexes = [
        ".*\\/databinding\\/.*",
        ".*\\$.*inlined.*"
    ]

    resChiperEnabled = true
    resChiperConfigFile = "tools/reschiper-config.xml"

    dptEnabled = true
    dptJar = "tools/dpt.jar"
}
```

## Flower-only Mode

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
}
```

## Notes

- AGP 4 cannot use the AGP 8 instrumentation API, so the plugin automatically uses Transform.
- The plugin declares AGP as `compileOnly`, so it will not force an AGP 8 dependency into AGP 4 projects.
- Protect all classes carefully. Generated binding classes, Kotlin inline/lambda classes, and hot UI classes can increase D8 pressure; add `excludeClassRegexes` when needed.
- Resource obfuscation and shelling are release-oriented. Keep them disabled for normal debug builds.
