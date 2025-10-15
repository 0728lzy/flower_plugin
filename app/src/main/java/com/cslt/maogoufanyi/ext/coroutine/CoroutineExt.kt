package com.cslt.maogoufanyi.ext.coroutine

import androidx.lifecycle.lifecycleScope
import com.cslt.maogoufanyi.BuildConfig
import com.cslt.maogoufanyi.base.dj.RootActivity
import com.cslt.maogoufanyi.base.dj.RootFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext



fun RootActivity.launch(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
) {
    // 给context包裹一层异常处理
    val combined = combinedCoroutineContext(context)
    lifecycleScope.launch(combined, start, block)
}

fun RootFragment.launch(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
) {
    // 给context包裹一层异常处理
    val combined = combinedCoroutineContext(context)
    viewLifecycleOwner.lifecycleScope.launch(combined, start, block)
}

private fun combinedCoroutineContext(context: CoroutineContext): CoroutineContext {
    var combined = DefaultCoroutineExceptionHandler() + context
    if (BuildConfig.DEBUG) {
        //     添加一个协程id的元素，方便差错
        combined += CoroutineDebugId()
    }
    return combined
}