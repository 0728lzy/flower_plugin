package com.cslt.maogoufanyi.ext.coroutine

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlin.coroutines.CoroutineContext

/**
 * Author: liaohailong
 * Date: 2022/7/28
 * Time: 20:01
 * Description:
 **/
class DefaultCoroutineExceptionHandler : CoroutineExceptionHandler {
    // private val logcat = Logcat.obtain(this)
    override val key: CoroutineContext.Key<*> = CoroutineExceptionHandler

    override fun handleException(context: CoroutineContext, exception: Throwable) {

        // 逻辑判断exception异常类型


        // 上下文中出现了不需要显示客户端异常的元素
        if (context[NoClientError] != null) {
            // 不进行下面的log日志提示了
            return
        }


        val threadName = Thread.currentThread().name
        val coroutineId = "@coroutine#${context[CoroutineDebugId]?.id}"
        val tag = "[$threadName $coroutineId]"

        // 异常处理一下
        // logcat.exception(exception)
        // Logger.e(exception, tag)
    }
}