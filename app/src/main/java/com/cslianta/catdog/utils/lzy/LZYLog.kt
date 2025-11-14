package com.cslianta.catdog.utils.lzy

import android.content.Context

object LZYLog {
    // 控制日志输出的开关
    private var isLogEnabled: Boolean = true

    // 设置是否启用日志
    fun setLogEnabled(enabled: Boolean) {
        isLogEnabled = enabled
    }

    // 获取日志开关状态
    fun isLogEnabled(): Boolean {
        return isLogEnabled
    }

    // 打印调试日志
    fun d(tag: String, message: String) {
        if (isLogEnabled) {
            android.util.Log.d(tag, message)
        }
    }

    // 打印信息日志
    fun i(tag: String, message: String) {
        if (isLogEnabled) {
            android.util.Log.i(tag, message)
        }
    }

    // 打印警告日志
    fun w(tag: String, message: String) {
        if (isLogEnabled) {
            android.util.Log.w(tag, message)
        }
    }

    // 打印错误日志
    fun e(tag: String, message: String) {
        if (isLogEnabled) {
            android.util.Log.e(tag, message)
        }
    }

    // 打印调试日志
    fun d(context: Context, message: String) {
        if (isLogEnabled) {
            android.util.Log.d(context::class.java.name, message)
        }
    }

    // 打印信息日志
    fun i(context: Context, message: String) {
        if (isLogEnabled) {
            android.util.Log.i(context::class.java.name, message)
        }
    }

    // 打印警告日志
    fun w(context: Context, message: String) {
        if (isLogEnabled) {
            android.util.Log.w(context::class.java.name, message)
        }
    }

    // 打印错误日志
    fun e(context: Context, message: String) {
        if (isLogEnabled) {
            android.util.Log.e(context::class.java.name, message)
        }
    }
}
