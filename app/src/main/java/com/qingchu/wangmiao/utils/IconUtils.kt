package com.qingchu.wangmiao.utils

import android.content.Context
import android.graphics.Bitmap
import com.drake.net.utils.withIO
import java.io.File
import java.io.FileOutputStream

object IconUtils {

    fun hasIcon(context: Context, host: String): Boolean {
        context.getExternalFilesDir("app")?.let { dirFile ->
            val rootDir = dirFile.absolutePath
            val iconFile = File(rootDir, "htmlUrl")
            val iconDir = iconFile.absolutePath
            val targetFile = File(iconDir, host + ".jpg")
            return targetFile.exists()
        }
        return false
    }

    fun getIcon(context: Context, host: String): String {
        context.getExternalFilesDir("app")?.let { dirFile ->
            val rootDir = dirFile.absolutePath
            val iconFile = File(rootDir, "htmlUrl")
            val iconDir = iconFile.absolutePath
            val targetFile = File(iconDir, host + ".jpg")
            return targetFile.absolutePath
        }
        return ""
    }

    suspend fun saveIcon(context: Context, host: String, icon: Bitmap): String {
        context.getExternalFilesDir("app")?.let { dirFile ->
            val rootDir = dirFile.absolutePath
            val iconFile = File(rootDir, "htmlUrl")
            val iconDir = iconFile.absolutePath
            val targetFile = File(iconDir, host + ".jpg")
            withIO {
                FileOutputStream(targetFile).use {
                    icon.compress(Bitmap.CompressFormat.JPEG, 100, it)
                    it.flush()
                }
            }
            return targetFile.absolutePath
        }
        return ""
    }


}