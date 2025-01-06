package com.ruiteapp.pettranslator.base.dj

import android.content.Context
import android.content.Intent
import android.util.Pair
import android.view.View
import android.webkit.ValueCallback
import androidx.appcompat.app.AppCompatActivity
import com.ruiteapp.pettranslator.utils.LanguageUtils.getAttachBaseContext

abstract class RootActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase?.let { getAttachBaseContext(it) })
    }

    /** 维护一个callback集合，方便调用侧直接能拿到onActivityResult返回的数据 */
    private val resultCallbacks:
            MutableMap<Int, ArrayList<ValueCallback<Pair<Int, Intent?>>>> = HashMap()

    open fun startActivityForResult(
        intent: Intent,
        requestCode: Int,
        callback: ValueCallback<Pair<Int, Intent?>>
    ) {
        addActivityResultCallback(requestCode, callback)
        startActivityForResult(intent, requestCode)
    }

    @Synchronized
    open fun addActivityResultCallback(
        requestCode: Int,
        callback: ValueCallback<Pair<Int, Intent?>>
    ) {
        var list = resultCallbacks[requestCode]
        if (list == null) {
            list = ArrayList()
            resultCallbacks[requestCode] = list
        }
        if (!list.contains(callback)) {
            // logcat.i("addActivityResultCallback requestCode: %s ", requestCode)
            list.add(callback)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // 处理回调
        if (RESULT_OK == resultCode) {
            val pair = Pair(requestCode, data)
            val list: ArrayList<ValueCallback<Pair<Int, Intent?>>>? =
                resultCallbacks[requestCode]
            if (list != null) {
                // logcat.i(
                //     "addActivityResultCallback removed requestCode: %s, size: %s",
                //     requestCode,
                //     list.size
                // )
                for (callback in list) {
                    callback.onReceiveValue(pair)
                }
            }
        }
        // 无论是否成功，都把 activity result callback 给移除掉
        resultCallbacks.remove(requestCode)
    }


    // 借助 View.OnLongClickListener 有一个return返回值的优势，不单独写一个回调接口了，返回true表示拦截
    private val onBackPressedHandlers = ArrayList<View.OnLongClickListener>()
    fun addOnBackPressedHandler(handler: View.OnLongClickListener) {
        if (onBackPressedHandlers.contains(handler)) return
        onBackPressedHandlers.add(handler)
    }

    fun removeOnBackPressedHandler(handler: View.OnLongClickListener) {
        onBackPressedHandlers.remove(handler)
    }

    override fun onBackPressed() {
        for (handler in onBackPressedHandlers) {
            if (handler.onLongClick(window.decorView)) return
        }
        try {
            super.onBackPressed()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

}