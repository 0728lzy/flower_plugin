package com.pet.translator.ext

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch


fun Context.dp2px(dpValue: Float): Int {
    val scale = this.resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

fun AppCompatActivity.countDown(
    time: Int = 30,
    start: (scop: CoroutineScope) -> Unit,
    next: (time: String) -> Unit,
    end: () -> Unit
): Job {
    return lifecycleScope.launch {
        // 在这个范围内启动的协程会在Lifecycle被销毁的时候自动取消
        flow {
            (time downTo 0).forEach {
                delay(1000)
                emit(it)
            }
        }.onStart {
            // 倒计时开始 ，在这里可以让Button 禁止点击状态
            start(this@launch)
        }.onCompletion {
            // 倒计时结束 ，在这里可以让Button 恢复点击状态
            end()
        }.catch {
            //错误
            Log.e("", it.message ?: "Unkown Error")
        }.collect {
            // 在这里 更新值来显示到UI
            next(it.toString())
        }
    }
}