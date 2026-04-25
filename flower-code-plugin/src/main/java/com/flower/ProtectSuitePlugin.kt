package com.flower

import com.android.build.gradle.AppExtension
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File
import java.io.FileOutputStream
import java.util.Locale
import java.util.zip.ZipFile
import javax.xml.parsers.DocumentBuilderFactory

class ProtectSuitePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create(
            "protectSuite",
            ProtectSuiteExtension::class.java
        )

        registerFlowerInstrumentation(project, extension)
        applyResChiperIfAvailable(project)
        configureResChiper(project, extension)
        registerReleaseApkTask(project, extension)
    }

    private fun applyResChiperIfAvailable(project: Project) {
        runCatching {
            project.pluginManager.apply("io.github.goldfish07.reschiper")
        }.onFailure {
            project.logger.lifecycle(
                "ReSChiper plugin is not available on the build classpath; protectReleaseApk will use assembleRelease fallback."
            )
        }
    }

    private fun registerFlowerInstrumentation(project: Project, extension: ProtectSuiteExtension) {
        FlowerCodeRegistrar.register(project, ProtectSuiteFlowerCodeConfig(extension))
    }

    private fun configureResChiper(project: Project, extension: ProtectSuiteExtension) {
        project.afterEvaluate {
            if (!extension.enabled || !extension.resChiperEnabled) return@afterEvaluate

            val resChiper = project.extensions.findByName("resChiper") ?: return@afterEvaluate
            setExtensionProperty(resChiper, "enableObfuscation", true)
            setExtensionProperty(resChiper, "obfuscationMode", extension.resChiperObfuscationMode)
            setExtensionProperty(resChiper, "mergeDuplicateResources", extension.mergeDuplicateResources)
            setExtensionProperty(resChiper, "enableFileFiltering", extension.enableFileFiltering)
            setExtensionProperty(resChiper, "enableFilterStrings", extension.enableFilterStrings)
            setExtensionProperty(resChiper, "obfuscatedBundleName", extension.resChiperOutputBundleName)
            setExtensionProperty(
                resChiper,
                "whiteList",
                loadResChiperWhiteList(project.rootProject.file(extension.resChiperConfigFile))
            )
        }
    }

    private fun registerReleaseApkTask(project: Project, extension: ProtectSuiteExtension) {
        project.tasks.register("protectReleaseApk") { task ->
            task.group = "obfuscation"
            task.description = "Build release AAB, obfuscate resources, convert to APK, and optionally protect Dex."
            task.dependsOn(
                if (project.tasks.findByName("resChiperRelease") != null && extension.resChiperEnabled) {
                    "resChiperRelease"
                } else {
                    "assembleRelease"
                }
            )

            task.doLast {
                if (!extension.enabled) return@doLast

                val android = project.extensions.findByType(AppExtension::class.java)
                    ?: throw GradleException("Android application extension not found.")

                val buildDir = project.layout.buildDirectory.get().asFile
                val appId = android.defaultConfig.applicationId ?: project.name
                val versionName = android.defaultConfig.versionName ?: "unknown"
                val channel = System.getenv("APP_CHANNEL") ?: "release"
                val date = System.currentTimeMillis()
                val dynamicName = "${appId}_release_${date}_${channel}_${versionName}_obfuscated.apk"

                val inputAab = File(buildDir, "outputs/bundle/release/app-release.aab")
                val obfuscatedAab = File(buildDir, "outputs/bundle/release/${extension.resChiperOutputBundleName}")
                val outputApks = File(buildDir, "outputs/apk/release/app-release-obfuscated.apks")
                val finalApk = File(buildDir, "outputs/apk/release/$dynamicName")
                val releaseOutputDir = project.file(extension.outputDir)
                val releaseOutputApk = File(
                    releaseOutputDir,
                    "${dynamicName.removeSuffix(".apk")}${extension.outputSuffix}.apk"
                )

                val configFile = project.rootProject.file(extension.resChiperConfigFile)
                val useResChiper = extension.resChiperEnabled && project.tasks.findByName("resChiperRelease") != null
                if (useResChiper && !configFile.exists()) {
                    throw GradleException("Missing: ${configFile.absolutePath}")
                }

                releaseOutputDir.mkdirs()
                clearApks(releaseOutputDir)

                val signingConfig = android.signingConfigs.getByName(extension.signingConfigName)
                if (useResChiper) {
                    if (!inputAab.exists()) throw GradleException("Missing release bundle: ${inputAab.absolutePath}")
                    if (!obfuscatedAab.exists()) {
                        throw GradleException("Missing ReSChiper output bundle: ${obfuscatedAab.absolutePath}")
                    }

                    outputApks.parentFile.mkdirs()
                    val bundletoolClasspath = project.configurations.detachedConfiguration(
                        project.dependencies.create("com.android.tools.build:bundletool:${extension.bundletoolVersion}")
                    )
                    val bundletoolJava = resolveJavaBin(project, extension, extension.bundletoolJavaExecutable)

                    project.exec { spec ->
                        spec.commandLine(
                            bundletoolJava.absolutePath,
                            "-cp",
                            bundletoolClasspath.asPath,
                            "com.android.tools.build.bundletool.BundleToolMain",
                            "build-apks",
                            "--bundle=${obfuscatedAab.absolutePath}",
                            "--output=${outputApks.absolutePath}",
                            "--mode=universal",
                            "--overwrite",
                            "--ks=${signingConfig.storeFile?.absolutePath}",
                            "--ks-pass=pass:${signingConfig.storePassword}",
                            "--ks-key-alias=${signingConfig.keyAlias}",
                            "--key-pass=pass:${signingConfig.keyPassword}"
                        )
                    }

                    extractUniversalApk(outputApks, finalApk)
                    outputApks.delete()
                } else {
                    latestReleaseApk(File(buildDir, "outputs/apk/release"))
                        ?.copyTo(finalApk, overwrite = true)
                        ?: throw GradleException("No release APK found under ${File(buildDir, "outputs/apk/release").absolutePath}")
                }

                var releaseCandidate = finalApk

                if (extension.dptEnabled) {
                    releaseCandidate = runDpt(project, extension, finalApk, dynamicName, signingConfig)
                }

                releaseCandidate.copyTo(releaseOutputApk, overwrite = true)
                project.logger.lifecycle("Protected APK generated: ${releaseOutputApk.absolutePath}")
            }
        }

        project.tasks.register("resDJApkGenerate") { task ->
            task.group = "obfuscation"
            task.description = "Compatibility alias for protectReleaseApk."
            task.dependsOn("protectReleaseApk")
        }
    }

    private fun runDpt(
        project: Project,
        extension: ProtectSuiteExtension,
        finalApk: File,
        dynamicName: String,
        signingConfig: com.android.build.gradle.internal.dsl.SigningConfig
    ): File {
        val buildDir = project.layout.buildDirectory.get().asFile
        val dptJar = project.file(extension.dptJar)
        if (!dptJar.exists()) throw GradleException("Missing: ${dptJar.absolutePath}")

        val dptOutputDir = File(buildDir, "outputs/apk/release/dpt")
        val dptAlignedApk = File(buildDir, "outputs/apk/release/${dynamicName.removeSuffix(".apk")}_dpt_aligned.apk")
        val dptProtectedApk = File(buildDir, "outputs/apk/release/${dynamicName.removeSuffix(".apk")}_dpt.apk")

        dptOutputDir.mkdirs()
        clearApks(dptOutputDir)

        val dptJava = resolveJavaBin(project, extension, extension.dptJavaExecutable)
        val dptArgs = mutableListOf(
            dptJava.absolutePath,
            "-jar",
            dptJar.absolutePath,
            "-f",
            finalApk.absolutePath,
            "-o",
            dptOutputDir.absolutePath,
            "-x"
        )

        if (extension.dptDebug) dptArgs.add("--debug")
        if (extension.dptDisableAcf) dptArgs.add("--disable-acf")
        if (extension.dptDumpCode) dptArgs.add("--dump-code")
        if (extension.dptNoisyLog) dptArgs.add("--noisy-log")
        if (extension.dptKeepClasses) dptArgs.add("-K")
        if (extension.dptSmaller) dptArgs.add("-S")
        if (extension.dptVerifySign) dptArgs.add("-vs")
        if (extension.dptExcludeAbi.isNotBlank()) dptArgs.addAll(listOf("-e", extension.dptExcludeAbi))
        extension.dptRulesFile?.takeIf { it.isNotBlank() }?.let {
            dptArgs.addAll(listOf("-r", project.file(it).absolutePath))
        }
        extension.dptProtectConfig?.takeIf { it.isNotBlank() }?.let {
            dptArgs.addAll(listOf("-c", project.file(it).absolutePath))
        }

        project.exec { spec -> spec.commandLine(dptArgs) }

        val generatedDptApk = dptOutputDir
            .listFiles { file -> file.isFile && file.extension.equals("apk", ignoreCase = true) }
            ?.maxByOrNull { it.lastModified() }
            ?: throw GradleException("dpt-shell did not generate an APK in ${dptOutputDir.absolutePath}")

        project.exec { spec ->
            spec.commandLine(
                findAndroidBuildTool("zipalign.exe").absolutePath,
                "-p",
                "-f",
                "4",
                generatedDptApk.absolutePath,
                dptAlignedApk.absolutePath
            )
        }

        project.exec { spec ->
            spec.commandLine(
                findAndroidBuildTool("apksigner.bat").absolutePath,
                "sign",
                "--ks",
                signingConfig.storeFile?.absolutePath,
                "--ks-pass",
                "pass:${signingConfig.storePassword}",
                "--ks-key-alias",
                signingConfig.keyAlias,
                "--key-pass",
                "pass:${signingConfig.keyPassword}",
                "--out",
                dptProtectedApk.absolutePath,
                dptAlignedApk.absolutePath
            )
        }

        return dptProtectedApk
    }

    private fun loadResChiperWhiteList(configFile: File): Set<String> {
        if (!configFile.exists()) return emptySet()

        val document = DocumentBuilderFactory.newInstance()
            .newDocumentBuilder()
            .parse(configFile)
        val nodes = document.getElementsByTagName("path")

        return (0 until nodes.length)
            .mapNotNull { nodes.item(it).textContent?.trim() }
            .filter { it.isNotEmpty() }
            .toSet()
    }

    private fun extractUniversalApk(outputApks: File, finalApk: File) {
        ZipFile(outputApks).use { zip ->
            val entry = zip.getEntry("universal.apk")
                ?: throw GradleException("universal.apk not found in apks")
            zip.getInputStream(entry).use { input ->
                FileOutputStream(finalApk).use { output ->
                    input.copyTo(output)
                }
            }
        }
    }

    private fun clearApks(dir: File) {
        dir.listFiles()?.forEach { file ->
            if (file.isFile && file.extension.equals("apk", ignoreCase = true)) {
                file.delete()
            }
        }
    }

    private fun latestReleaseApk(dir: File): File? {
        return dir.listFiles { file -> file.isFile && file.extension.equals("apk", ignoreCase = true) }
            ?.maxByOrNull { it.lastModified() }
    }

    private fun resolveJavaBin(
        project: Project,
        extension: ProtectSuiteExtension,
        explicitExecutable: String?
    ): File {
        val executableCandidates = listOfNotNull(
            explicitExecutable?.trim()?.takeIf { it.isNotEmpty() },
            extension.javaExecutable?.trim()?.takeIf { it.isNotEmpty() }
        ).map(project::file)

        executableCandidates.firstOrNull { it.exists() }?.let { return it }

        val homeCandidates = listOfNotNull(
            extension.javaHome?.trim()?.takeIf { it.isNotEmpty() },
            System.getenv("JAVA_HOME")?.trim()?.takeIf { it.isNotEmpty() },
            System.getProperty("java.home")?.trim()?.takeIf { it.isNotEmpty() }
        ).map(project::file)

        homeCandidates
            .asSequence()
            .flatMap { javaHomeDir ->
                sequenceOf(
                    File(javaHomeDir, "bin${File.separator}${javaCommandName()}"),
                    File(javaHomeDir, javaCommandName())
                )
            }
            .firstOrNull { it.exists() }
            ?.let { return it }

        val attempted = buildList {
            addAll(executableCandidates.map { it.absolutePath })
            addAll(homeCandidates.map { it.absolutePath })
        }.joinToString()

        throw GradleException(
            "Java executable not found. Checked: $attempted. " +
                "Configure protectSuite.javaExecutable / javaHome / bundletoolJavaExecutable / dptJavaExecutable."
        )
    }

    private fun javaCommandName(): String {
        return if (System.getProperty("os.name").lowercase(Locale.ROOT).contains("windows")) {
            "java.exe"
        } else {
            "java"
        }
    }

    private fun findAndroidBuildTool(toolName: String): File {
        val sdkDir = listOfNotNull(
            System.getenv("ANDROID_HOME"),
            System.getenv("ANDROID_SDK_ROOT"),
            System.getenv("LOCALAPPDATA")?.let { "$it${File.separator}Android${File.separator}Sdk" }
        ).map(::File).firstOrNull { it.exists() }
            ?: throw GradleException("Android SDK not found. Set ANDROID_HOME or ANDROID_SDK_ROOT.")

        return File(sdkDir, "build-tools")
            .listFiles { file -> file.isDirectory }
            ?.sortedByDescending { it.name }
            ?.map { File(it, toolName) }
            ?.firstOrNull { it.exists() }
            ?: throw GradleException("Android build tool not found: $toolName")
    }

    private fun setExtensionProperty(target: Any, property: String, value: Any?) {
        val setterName = "set" + property.replaceFirstChar { it.uppercaseChar() }
        val setter = target.javaClass.methods.firstOrNull { method ->
            method.name == setterName && method.parameterCount == 1
        }

        if (setter != null) {
            setter.invoke(target, value)
            return
        }

        val groovySetter = target.javaClass.methods.firstOrNull { method ->
            method.name == "setProperty" && method.parameterCount == 2
        } ?: throw GradleException("Cannot set property '$property' on ${target.javaClass.name}")
        groovySetter.invoke(target, property, value)
    }
}
