package com.weini.maogou.utils.dj

import android.util.Log

object AntiRepeatClickUtils {
    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private const val MIN_CLICK_DELAY_TIME = 1000
    private const val MIN_INSTALL_DELAY_TIME = 10 * 60 * 1000
    private var lastClickTime: Long = 0
    private const val MIN_CLICK_HTTP_DELAY_TIME = 5*1000

    private const val MIN_CLICK_JL_DELAY_TIME = 4*1000
    fun isFastClick(): Boolean {
        var flag = false
        val curClickTime = System.currentTimeMillis()
        if (curClickTime - lastClickTime >= MIN_CLICK_DELAY_TIME) {
            flag = true
        }
        lastClickTime = curClickTime
        Log.e("AntiRepeatClickUtils", " flag=${flag}")
        return flag
    }

    fun isFastClickInstall(): Boolean {
        var flag = false
        val curTime = System.currentTimeMillis()
        var lastShowInstallTime = UserInfoModel.getShowAppInstallTime()
        if (curTime - lastShowInstallTime >= MIN_INSTALL_DELAY_TIME) {
            flag = true
        }
        UserInfoModel.setShowAppInstallTime(curTime)
        Log.e("isFastClickInstall", " flag=${flag}")
        return flag
    }

    fun isFastHttpClick(): Boolean {
        var flag = false
        val curClickTime = System.currentTimeMillis()
        if (curClickTime - lastClickTime >= MIN_CLICK_HTTP_DELAY_TIME) {
            flag = true
        }
        lastClickTime = curClickTime
        Log.e("AntiRepeatClickUtils", " flag=${flag}")
        return flag
    }


    fun isFastClickJL(): Boolean {
        var flag = false
        val curTime = System.currentTimeMillis()
        var lastShowInstallTime = UserInfoModel.getClickJlTime()
        Log.e("tttt","curTime - lastShowInstallTime:${curTime - lastShowInstallTime},MIN_CLICK_JL_DELAY_TIME:$MIN_CLICK_JL_DELAY_TIME")
        if (curTime - lastShowInstallTime >= MIN_CLICK_JL_DELAY_TIME) {
            flag = true
        }
        UserInfoModel.setClickJlTime(curTime)
        Log.e("isFastClickInstall", " flag=${flag}")
        return flag
    }

}