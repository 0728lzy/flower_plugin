package com.flower

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
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
        var stringFogRuntimeWritten = false

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
                    if (!stringFogRuntimeWritten) {
                        writeStringFogRuntime(output)
                        stringFogRuntimeWritten = true
                    }
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
        val writer = SafeClassWriter(reader, ClassWriter.COMPUTE_MAXS)
        val visitor = FlowerCodeClassVisitor(writer, config)
        reader.accept(visitor, ClassReader.EXPAND_FRAMES)
        return writer.toByteArray()
    }

    private fun writeStringFogRuntime(output: File) {
        if (!config.stringFogEnabled) return

        val runtimeClass = File(output, "${config.stringFogClassName}.class")
        runtimeClass.parentFile.mkdirs()
        runtimeClass.writeBytes(generateStringFogRuntime(config.stringFogClassName))
    }

    private fun generateStringFogRuntime(className: String): ByteArray {
        val cw = ClassWriter(0)
        cw.visit(
            Opcodes.V1_7,
            Opcodes.ACC_PUBLIC or Opcodes.ACC_FINAL or Opcodes.ACC_SUPER,
            className,
            null,
            "java/lang/Object",
            null
        )

        cw.visitField(
            Opcodes.ACC_PRIVATE or Opcodes.ACC_STATIC or Opcodes.ACC_FINAL,
            "UTF_8",
            "Ljava/nio/charset/Charset;",
            null,
            null
        ).visitEnd()

        cw.visitMethod(Opcodes.ACC_PRIVATE, "<init>", "()V", null, null).apply {
            visitCode()
            visitVarInsn(Opcodes.ALOAD, 0)
            visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false)
            visitInsn(Opcodes.RETURN)
            visitMaxs(1, 1)
            visitEnd()
        }

        cw.visitMethod(
            Opcodes.ACC_PUBLIC or Opcodes.ACC_STATIC,
            "decode",
            "(Ljava/lang/String;I)Ljava/lang/String;",
            null,
            null
        ).apply {
            val loop = org.objectweb.asm.Label()
            val end = org.objectweb.asm.Label()
            visitCode()
            visitVarInsn(Opcodes.ALOAD, 0)
            visitInsn(Opcodes.ICONST_2)
            visitMethodInsn(
                Opcodes.INVOKESTATIC,
                "android/util/Base64",
                "decode",
                "(Ljava/lang/String;I)[B",
                false
            )
            visitVarInsn(Opcodes.ASTORE, 2)
            visitInsn(Opcodes.ICONST_0)
            visitVarInsn(Opcodes.ISTORE, 3)
            visitLabel(loop)
            visitVarInsn(Opcodes.ILOAD, 3)
            visitVarInsn(Opcodes.ALOAD, 2)
            visitInsn(Opcodes.ARRAYLENGTH)
            visitJumpInsn(Opcodes.IF_ICMPGE, end)
            visitVarInsn(Opcodes.ALOAD, 2)
            visitVarInsn(Opcodes.ILOAD, 3)
            visitVarInsn(Opcodes.ALOAD, 2)
            visitVarInsn(Opcodes.ILOAD, 3)
            visitInsn(Opcodes.BALOAD)
            visitVarInsn(Opcodes.ILOAD, 1)
            visitInsn(Opcodes.IXOR)
            visitInsn(Opcodes.I2B)
            visitInsn(Opcodes.BASTORE)
            visitIincInsn(3, 1)
            visitJumpInsn(Opcodes.GOTO, loop)
            visitLabel(end)
            visitTypeInsn(Opcodes.NEW, "java/lang/String")
            visitInsn(Opcodes.DUP)
            visitVarInsn(Opcodes.ALOAD, 2)
            visitFieldInsn(Opcodes.GETSTATIC, className, "UTF_8", "Ljava/nio/charset/Charset;")
            visitMethodInsn(
                Opcodes.INVOKESPECIAL,
                "java/lang/String",
                "<init>",
                "([BLjava/nio/charset/Charset;)V",
                false
            )
            visitInsn(Opcodes.ARETURN)
            visitMaxs(5, 4)
            visitEnd()
        }

        cw.visitMethod(Opcodes.ACC_STATIC, "<clinit>", "()V", null, null).apply {
            visitCode()
            visitLdcInsn("UTF-8")
            visitMethodInsn(
                Opcodes.INVOKESTATIC,
                "java/nio/charset/Charset",
                "forName",
                "(Ljava/lang/String;)Ljava/nio/charset/Charset;",
                false
            )
            visitFieldInsn(Opcodes.PUTSTATIC, className, "UTF_8", "Ljava/nio/charset/Charset;")
            visitInsn(Opcodes.RETURN)
            visitMaxs(1, 0)
            visitEnd()
        }

        cw.visitEnd()
        return cw.toByteArray()
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
