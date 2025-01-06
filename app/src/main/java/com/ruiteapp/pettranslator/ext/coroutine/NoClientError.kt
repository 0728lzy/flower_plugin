package com.ruiteapp.pettranslator.ext.coroutine

import kotlin.coroutines.CoroutineContext

/**
 * Author: liaohailong
 * Date: 2022/7/31
 * Time: 16:02
 * Description: 定义一个不需要显示客户端异常的协程上下文元素
 **/
object NoClientError : CoroutineContext.Element,
    // CoroutineContext.Key是一个标记接口
    CoroutineContext.Key<NoClientError> {

    override val key: CoroutineContext.Key<*> = NoClientError
}