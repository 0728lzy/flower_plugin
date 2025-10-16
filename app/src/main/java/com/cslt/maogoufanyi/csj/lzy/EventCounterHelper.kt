package com.cslt.maogoufanyi.csj.lzy

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray

object EventCounterHelper {

    private const val PREF_NAME = "event_counter_pref"
    private const val KEY_EVENT_TIMESTAMPS = "event_timestamps"
    private const val TIME_WINDOW_MILLIS = 12 * 60 * 60 * 1000L // 12 小时

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        // 初始化时清理过期记录
        val currentTime = System.currentTimeMillis()
        val validTimestamps = getValidTimestamps(currentTime)
        saveTimestamps(validTimestamps)
    }

    fun recordEvent() {
        val currentTime = System.currentTimeMillis()
        val timestamps = getValidTimestamps(currentTime).apply {
            put(currentTime)
        }
        saveTimestamps(timestamps)
    }

    fun getEventCount(): Int {
        val currentTime = System.currentTimeMillis()
        return getValidTimestamps(currentTime).length()
    }

    fun clear() {
        prefs.edit().remove(KEY_EVENT_TIMESTAMPS).apply()
    }

    private fun getValidTimestamps(currentTime: Long): JSONArray {
        val raw = prefs.getString(KEY_EVENT_TIMESTAMPS, "[]") ?: "[]"
        val all = JSONArray(raw)
        val valid = JSONArray()

        for (i in 0 until all.length()) {
            val ts = all.optLong(i, 0L)
            if (currentTime - ts <= TIME_WINDOW_MILLIS) {
                valid.put(ts)
            }
        }
        return valid
    }

    private fun saveTimestamps(array: JSONArray) {
        prefs.edit().putString(KEY_EVENT_TIMESTAMPS, array.toString()).apply()
    }
}
