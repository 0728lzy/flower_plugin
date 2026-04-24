package com.flower

import com.android.build.api.transform.*
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import java.io.File
import java.io.FileOutputStream
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream

class FlowerCodeTransform(
    private val extension: FlowerCodeExtension
) : Transform() {

    override fun getName(): String = "flowerCodeTransform"

    override fun getInputTypes(): Set<QualifiedContent.ContentType> =
        mutableSetOf(QualifiedContent.DefaultContentType.CLASSES)

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> =
        mutableSetOf(
            QualifiedContent.Scope.PROJECT,
            QualifiedContent.Scope.SUB_PROJECTS,
            QualifiedContent.Scope.EXTERNAL_LIBRARIES
        )

    override fun isIncremental(): Boolean = false

    override fun transform(invocation: TransformInvocation) {
        val outputProvider = invocation.outputProvider
        outputProvider.deleteAll()

        invocation.inputs.forEach { input ->
            // 处理目录中的 class
            input.directoryInputs.forEach { dirInput ->
                val outputDir = outputProvider.getContentLocation(
                    dirInput.name, dirInput.contentTypes,
                    dirInput.scopes, Format.DIRECTORY
                )
                processDirectory(dirInput.file, outputDir)
            }

            // 处理 jar 中的 class
            input.jarInputs.forEach { jarInput ->
                val outputJar = outputProvider.getContentLocation(
                    jarInput.name, jarInput.contentTypes,
                    jarInput.scopes, Format.JAR
                )
                processJar(jarInput.file, outputJar)
            }
        }
    }

    private fun processDirectory(inputDir: File, outputDir: File) {
        inputDir.walkTopDown().forEach { file ->
            val relativePath = file.relativeTo(inputDir).path
            val outputFile = File(outputDir, relativePath)

            if (file.isDirectory) {
                outputFile.mkdirs()
                return@forEach
            }

            outputFile.parentFile.mkdirs()

            if (file.extension == "class" && shouldProcess(relativePath)) {
                val bytes = processClass(file.readBytes())
                outputFile.writeBytes(bytes)
            } else {
                file.copyTo(outputFile, overwrite = true)
            }
        }
    }

    private fun processJar(inputJar: File, outputJar: File) {
        outputJar.parentFile.mkdirs()
        val jarFile = JarFile(inputJar)
        val jos = JarOutputStream(FileOutputStream(outputJar))

        jarFile.entries().asSequence().forEach { entry ->
            val newEntry = JarEntry(entry.name)
            jos.putNextEntry(newEntry)

            val inputStream = jarFile.getInputStream(entry)
            if (entry.name.endsWith(".class") && shouldProcess(entry.name)) {
                jos.write(processClass(inputStream.readBytes()))
            } else {
                jos.write(inputStream.readBytes())
            }
            jos.closeEntry()
        }

        jos.close()
        jarFile.close()
    }

    private fun shouldProcess(path: String): Boolean {
        val className = path.removeSuffix(".class")
        return extension.targetClasses.any { target ->
            className == target || className.startsWith("$target\$")
        }
    }

    private fun processClass(bytes: ByteArray): ByteArray {
        val reader = ClassReader(bytes)
        val writer = ClassWriter(reader, ClassWriter.COMPUTE_MAXS or ClassWriter.COMPUTE_FRAMES)
        val visitor = FlowerCodeClassVisitor(writer, extension)
        reader.accept(visitor, ClassReader.EXPAND_FRAMES)
        return writer.toByteArray()
    }
}
