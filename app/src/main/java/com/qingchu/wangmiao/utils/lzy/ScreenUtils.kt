package com.qingchu.wangmiao.utils.lzy

import android.content.Context
import android.view.Display
import android.view.WindowManager

/**
 * 像素尺寸工具类
 */
object ScreenUtils {
    private var defaultDisplay: Display? = null
    private var mDensity = 0f
    private var mScaledDensity = 0f
    private var mDensityDpi = 0
    fun getDensity(context: Context): Float {
        if (mDensity == 0f) {
            mDensity = context.resources.displayMetrics.density
        }
        return mDensity
    }

    fun getDensityDpi(context: Context): Int {
        if (mDensityDpi == 0) {
            mDensityDpi = context.resources.displayMetrics.densityDpi
        }
        return mDensityDpi
    }

    fun getScaledDensity(context: Context): Float {
        if (mScaledDensity == 0f) {
            mScaledDensity = context.resources.displayMetrics.scaledDensity
        }
        return mScaledDensity
    }

    fun dip2px(dip: Int, context: Context): Int {
        return (0.5f + getDensity(context) * dip).toInt()
    }

    fun px2dip(px: Int, context: Context): Int {
        return (0.5f + px / getDensity(context)).toInt()
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @param fontScale（DisplayMetrics类中属性scaledDensity）
     * @return
     */
    fun px2sp(pxValue: Float, context: Context): Int {
        return (pxValue / getScaledDensity(context) + 0.5f).toInt()
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @param fontScale（DisplayMetrics类中属性scaledDensity）
     * @return
     */
    fun sp2px(spValue: Float, context: Context): Int {
        return (spValue * getScaledDensity(context) + 0.5f).toInt()
    }

    // get screen width value int pixels
    fun getScreenWidth(context: Context): Int {
        return context.resources.displayMetrics.widthPixels
    }

    // get screen height value int pixels
    fun getScreenHeight(context: Context): Int {
        return context.resources.displayMetrics.heightPixels
    }

    fun getDefaultDisplay(context: Context): Display? {
        if (defaultDisplay == null) {
            defaultDisplay =
                (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        }
        return defaultDisplay
    }

    fun getHeight(context: Context): Int {
        return getDefaultDisplay(context)!!.height
    }

    fun getWidth(context: Context): Int {
        return getDefaultDisplay(context)!!.width
    }

    fun percentHeight(percent: Float, context: Context): Int {
        return (percent * getHeight(context)).toInt()
    }

    fun percentWidth(percent: Float, context: Context): Int {
        return (percent * getWidth(context)).toInt()
    }
}
