# JDK Compatibility Notes

## What changed

- The sample `app` module no longer hardcodes Java 17 bytecode.
- `flower-code-plugin` can now use a custom Java runtime for external tooling.
- External Java selection supports both a shared setting and per-tool overrides.

## Important distinction

- `flower-code-plugin` itself is compiled to Java 11 bytecode.
- If the consuming Android project uses AGP 8.x, Gradle still has to run on JDK 17. That requirement comes from AGP, not from this plugin.
- Even on AGP 8.x, your app code can still target Java 11 bytecode.

## App module Java target

The sample app defaults to Java 11 and can be overridden with:

```bash
./gradlew assembleRelease -Pflower.java.version=11
./gradlew assembleRelease -Pflower.java.version=17
```

You can also use environment variables:

```bash
FLOWER_JAVA_VERSION=11
FLOWER_JAVA_VERSION=17
```

## Plugin Java selection

`protectSuite` now supports these fields:

```kotlin
protectSuite {
    javaHome = providers.gradleProperty("flower.java.home").orNull
        ?: System.getenv("FLOWER_JAVA_HOME")
    javaExecutable = providers.gradleProperty("flower.java.bin").orNull
        ?: System.getenv("FLOWER_JAVA_BIN")
    bundletoolJavaExecutable = providers.gradleProperty("flower.bundletool.java.bin").orNull
        ?: System.getenv("FLOWER_BUNDLETOOL_JAVA_BIN")
    dptJavaExecutable = providers.gradleProperty("flower.dpt.java.bin").orNull
        ?: System.getenv("FLOWER_DPT_JAVA_BIN")
}
```

Resolution order:

1. Tool-specific executable, such as `bundletoolJavaExecutable` or `dptJavaExecutable`
2. Shared `javaExecutable`
3. `javaHome`
4. `JAVA_HOME`
5. Current Gradle JVM `java.home`

## Example

Run Gradle on JDK 17 for AGP 8, but force the plugin's helper tools to use JDK 11:

```bash
./gradlew protectReleaseApk ^
  -Pflower.java.version=11 ^
  -Pflower.java.home="C:\Program Files\Java\jdk-11"
```
