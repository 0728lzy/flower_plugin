package com.cslt.maogoufanyi.utils

import android.content.Context
import com.cslt.maogoufanyi.widget.dialog.LoadingDiaLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object DemoUtils {
    /**
     * 显示加载对话框，并在指定时间内检查条件是否满足。
     *
     * @param myContext 上下文，用于创建加载对话框
     * @param showMaxTime 最大等待时间（毫秒）
     * @param checkIntervalTime 条件检查间隔时间（毫秒）
     * @param conditionalPeriod 检查条件的函数，返回 Boolean
     * @param meetDoTask 条件满足时执行的任务
     * @param defaultDoTask 条件未满足时执行的默认任务
     */
    fun showLoadingDialogWitLimit(myContext:Context,
                                  showMaxTime:Long,
                                  checkIntervalTime:Long,
                                  conditionalPeriod:()->Boolean,
                                  meetDoTask:() -> Unit,
                                  defaultDoTask:()->Unit){
        val myDiaLog= LoadingDiaLog(myContext)
        myDiaLog.show()
        CoroutineScope(Dispatchers.Main).launch {
            val maxTime = showMaxTime  // 5秒
            val interval = checkIntervalTime  // 每0.1秒检测一次
            var elapsedTime = 0L
            var conditionMet = false
            while (elapsedTime < maxTime) {
                delay(interval)
                elapsedTime += interval
                // 检查变量是否满足条件
                if (conditionalPeriod()) {  // 假设条件是变量大于10
                    meetDoTask()
                    conditionMet = true
                    myDiaLog.dismiss()
                    break // 停止检测
                }
            }

            // 如果5秒内条件未满足，则执行fun1
            if (!conditionMet) {
                defaultDoTask()
                myDiaLog.dismiss()
            }
        }
    }
}