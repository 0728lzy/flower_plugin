package com.flower

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import java.io.File
import java.io.FileOutputStream

class FlowerCodeTransform(
    private val config: FlowerCodeConfig
) : Transform() {
    override fun getName(): String = "flowerCode"

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        return mutableSetOf(QualifiedContent.DefaultContentType.CLASSES)
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        return mutableSetOf(QualifiedContent.Scope.PROJECT)
    }

    override fun isIncremental(): Boolean = false

    override fun transform(transformInvocation: TransformInvocation) {
        val outputProvider = transformInvocation.outputProvider ?: return
        val variantName = readVariantName(transformInvocation)
        val shouldInject = config.enabled && isVariantEnabled(variantName)

        outputProvider.deleteAll()
        transformInvocation.inputs.forEach { input ->
            input.directoryInputs.forEach { directoryInput ->
                val output = outputProvider.getContentLocation(
                    directoryInput.name,
                    directoryInput.contentTypes,
                    directoryInput.scopes,
                    Format.DIRECTORY
                )
                if (shouldInject) {
                    transformDirectory(directoryInput, output)
                } else {
                    copyDirectory(directoryInput.file, output)
                }
            }

            input.jarInputs.forEach { jarInput ->
                copyJar(jarInput, outputProvider.getContentLocation(
                    jarInput.name,
                    jarInput.contentTypes,
                    jarInput.scopes,
                    Format.JAR
                ))
            }
        }
    }

    private fun transformDirectory(input: DirectoryInput, output: File) {
        if (output.exists()) output.deleteRecursively()
        input.file.walkTopDown().forEach { source ->
            val relative = source.relativeTo(input.file)
            val target = File(output, relative.path)

            if (source.isDirectory) {
                target.mkdirs()
                return@forEach
            }

            target.parentFile.mkdirs()
            if (source.extension == "class" && shouldProcessClass(relative.invariantSeparatorsPath)) {
                target.writeBytes(transformClass(source.readBytes()))
            } else {
                source.copyTo(target, overwrite = true)
            }
        }
    }

    private fun transformClass(bytes: ByteArray): ByteArray {
        val reader = ClassReader(bytes)
        val writer = ClassWriter(reader, ClassWriter.COMPUTE_MAXS or ClassWriter.COMPUTE_FRAMES)
        val visitor = FlowerCodeClassVisitor(writer, config)
        reader.accept(visitor, ClassReader.EXPAND_FRAMES)
        return writer.toByteArray()
    }

    private fun shouldProcessClass(relativeClassPath: String): Boolean {
        val className = relativeClassPath.removeSuffix(".class")
        if (isGeneratedOrFrameworkClass(className)) return false
        if (config.excludeClassRegexes.any { regex -> Regex(regex).matches(className) }) return false
        if (config.protectAllProjectClasses) return true
        return config.targetClasses.any { target ->
            className == target || className.startsWith("$target$")
        }
    }

    private fun isGeneratedOrFrameworkClass(className: String): Boolean {
        val simpleName = className.substringAfterLast('/')
        return simpleName == "R" ||
            simpleName.startsWith("R$") ||
            simpleName == "BuildConfig" ||
            simpleName == "Manifest" ||
            simpleName.startsWith("Manifest$")
    }

    private fun isVariantEnabled(variantName: String): Boolean {
        val variant = variantName.lowercase()
        return when {
            "debug" in variant -> config.enableInDebug
            "release" in variant -> config.enableInRelease
            else -> config.enableInRelease
        }
    }

    private fun readVariantName(transformInvocation: TransformInvocation): String {
        return runCatching {
            val context = transformInvocation.context
            val method = context.javaClass.methods.firstOrNull { it.name == "getVariantName" && it.parameterCount == 0 }
            method?.invoke(context) as? String
        }.getOrNull().orEmpty()
    }

    private fun copyJar(input: JarInput, output: File) {
        output.parentFile.mkdirs()
        input.file.copyTo(output, overwrite = true)
    }

    private fun copyDirectory(source: File, target: File) {
        if (target.exists()) target.deleteRecursively()
        source.copyRecursively(target, overwrite = true)
    }

    private fun File.writeBytes(bytes: ByteArray) {
        parentFile.mkdirs()
        FileOutputStream(this).use { it.write(bytes) }
    }
}
