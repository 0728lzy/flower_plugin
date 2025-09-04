package com.weini.maogou.utils

import android.util.Log

class LogUtil {
    companion object {
        private const val TAG = "YYBrowser"

        fun d(debug: String){
            Log.d(TAG, debug)
        }
    }
}