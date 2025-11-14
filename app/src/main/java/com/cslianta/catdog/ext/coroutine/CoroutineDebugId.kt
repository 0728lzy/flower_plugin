package com.cslianta.catdog.ext.coroutine

import java.util.concurrent.atomic.AtomicLong
import kotlin.coroutines.CoroutineContext

/**
 * Author: liaohailong
 * Date: 2022/7/31
 * Time: 16:22
 * Description:
 **/
class CoroutineDebugId : CoroutineContext.Element {
    companion object Key : CoroutineContext.Key<CoroutineDebugId> {
        private val COROUTINE_ID = AtomicLong(0)
    }

    override val key: CoroutineContext.Key<*> = CoroutineDebugId
    val id = COROUTINE_ID.incrementAndGet()

    override fun toString(): String {
        return "CoroutineDebugId:$id"
    }

}