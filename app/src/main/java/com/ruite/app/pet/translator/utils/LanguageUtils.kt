package com.ruite.app.pet.translator.utils

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.os.LocaleList
import com.blankj.utilcode.util.SPUtils
import java.util.Locale

object LanguageUtils {

    const val KEY = "Language_Index"

    fun getIndex() = SPUtils.getInstance().getInt(KEY, -1)

    fun setIndex(index: Int) {
        SPUtils.getInstance().put(KEY, index)
    }

    /**
     * Activity 更新语言资源
     */
    fun getAttachBaseContext(context: Context): Context {
        val index = getIndex()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return setAppLanguageApi24(context, index)
        } else {
            setAppLanguage(context, index)
        }
        return context
    }

    /**
     * 设置应用语言
     */
    @Suppress("DEPRECATION")
    fun setAppLanguage(context: Context, index: Int) {
        val resources = context.resources
        val displayMetrics = resources.displayMetrics
        val configuration = resources.configuration
        // 获取当前系统语言，默认设置跟随系统
        val locale = getAppLocale(index)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }
        resources.updateConfiguration(configuration, displayMetrics)
    }

    /**
     * 兼容 7.0 及以上
     */
    @TargetApi(Build.VERSION_CODES.N)
    private fun setAppLanguageApi24(context: Context, index: Int): Context {
        val locale = getAppLocale(index)
        val resource = context.resources
        val configuration = resource.configuration
        configuration.setLocale(locale)
        configuration.setLocales(LocaleList(locale))
        return context.createConfigurationContext(configuration)
    }

    /**
     * 获取 App 当前语言
     */
    private fun getAppLocale(index: Int) = when (index) {
        0 -> Locale("pt")
        1 -> Locale.CHINA
        2 -> Locale.ENGLISH
        3 -> Locale("hi")
        4 -> Locale("es")
        5 -> Locale("fr")
        else -> Locale.ENGLISH
    }


}