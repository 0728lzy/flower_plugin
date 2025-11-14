package com.cslianta.catdog.utils

/**
 * Toast工具类
 * 提供简单的Toast显示功能
 */
object ToastUtils {
    
    /**
     * 显示短时间Toast
     */
    fun show(message: String) {
        com.blankj.utilcode.util.ToastUtils.showShort(message)
    }
    
    /**
     * 显示长时间Toast
     */
    fun showLong(message: String) {
        com.blankj.utilcode.util.ToastUtils.showLong(message)
    }
}