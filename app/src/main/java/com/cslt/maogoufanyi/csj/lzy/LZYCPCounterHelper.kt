package com.cslt.maogoufanyi.csj.lzy

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.cslt.maogoufanyi.widget.dialog.LoadingDiaLog

object LZYCPCounterHelper {

    private const val PREF_NAME = "cp_counter_pref" // SharedPreferences文件名
    private const val KEY_EVENT_COUNT = "cp_event_count" // 事件计数的key
    private const val MAX_COUNT = 5 // 达到多少次后触发操作

    private lateinit var prefs: SharedPreferences // SharedPreferences实例

    /**
     * 初始化方法，必须在使用前调用。
     * @param context 用于获取SharedPreferences实例的上下文
     */
    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    /**
     * 记录一次插屏展示时间，每调用一次该方法，计数加一。
     */
    fun recordEvent() {
        val currentCount = prefs.getInt(KEY_EVENT_COUNT, 0)
        val newCount = currentCount + 1
        prefs.edit().putInt(KEY_EVENT_COUNT, newCount).apply()
    }

    /**
     * 判断是否达到MAX_COUNT，如果达到则执行action
     * 并将当前计数减去MAX_COUNT，保留剩余次数
     *
     * @param action 达到阈值后要执行的操作
     */
    fun fullToExecute(action: () -> Unit) {
        val currentCount = prefs.getInt(KEY_EVENT_COUNT, 0)
        if (currentCount >= MAX_COUNT) {
            val remaining = currentCount - MAX_COUNT
            prefs.edit().putInt(KEY_EVENT_COUNT, remaining).apply()
            action()
        }
    }

    /**
     * 对于上一个方法的特殊封装，可以直接使用这个方法，或者自己通过fullToExecute()方法执行逻辑
     * 达到触发次数后，展示加载对话框并触发激励广告
     *
     * @param activity 当前页面Activity引用，用于弹出Dialog和广告展示
     */
    fun fullToExecuteShowJL(activity: Activity?) {
        fullToExecute {
            if (activity != null && !activity.isFinishing) {
                val diaLog = LoadingDiaLog(activity, "加载中...")
                diaLog.show()
                // 显示激励广告
                LZYInitJLAdsUtils.showAdJLTurn(diaLog, activity) {

                }
            }
        }
    }

    /**
     * 清除当前事件计数，保留方法，暂无使用场景哦~
     */
    fun clear() {
        prefs.edit().remove(KEY_EVENT_COUNT).apply()
    }
}