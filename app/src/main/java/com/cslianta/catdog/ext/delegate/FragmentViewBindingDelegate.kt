package com.cslianta.catdog.ext.delegate

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewbinding.ViewBinding
import kotlin.reflect.KClass


/**
 * 在 Fragment 中创建 ViewBinding 绑定类
 *
 * @param VB ViewBinding 的子类
 * @param inflateMethodRef (LayoutInflater) -> T) VB 中 inflate 方法的函数引用
 */
@MainThread
inline fun <reified VB : ViewBinding> Fragment.viewInflate(noinline inflateMethodRef: ((LayoutInflater) -> VB)?) =
    FragmentViewBindingDelegate(this, VB::class, inflateMethodRef)

/**
 * 在 Fragment 中创建 ViewBinding 绑定类
 *
 * @param VB ViewBinding 的子类
 * @param bindMethdRef (View) -> T) VB 中 bind 方法的函数引用
 * 需调用 [Fragment(@LayoutRes int contentLayoutId)][androidx.fragment.app.Fragment]
 */
@MainThread
inline fun <reified VB : ViewBinding> Fragment.viewBinding(noinline bindMethdRef: ((View) -> VB)?) =
    FragmentViewBindingDelegate(this, VB::class, null, bindMethdRef)


class FragmentViewBindingDelegate<VB : ViewBinding>(
    private val fragment: Fragment,
    private val kClass: KClass<*>,
    private val inflateMethodRef: ((LayoutInflater) -> VB)? = null,
    private val bindMethodRef: ((View) -> VB)? = null
) : Lazy<VB> {

    private var cacheBinding: VB? = null
    private val clearBindingHandler by lazy(LazyThreadSafetyMode.NONE) { Handler(Looper.getMainLooper()) }

    init {
        observeFragmentDestroy(fragment) {
            clearBindingHandler.post { cacheBinding = null }
        }
    }

    override val value: VB
        get() {
            var viewBinding = cacheBinding
            if (viewBinding == null) {
                checkBindingFirstInvoke(fragment)
                viewBinding = if (fragment.view != null) {
                    bindMethodRef?.invoke(fragment.requireView())
                        ?: @Suppress("CHECKED_CAST")
                        kClass.java.getMethod("bind", View::class.java)
                            .invoke(null, fragment.view) as VB
                } else {
                    inflateMethodRef?.invoke(fragment.layoutInflater)
                        ?: @Suppress("UNCHECKED_CAST")
                        kClass.java.getMethod(METHOD_INFLATE, LayoutInflater::class.java)
                            .invoke(null, fragment.layoutInflater) as VB
                }
            }
            return viewBinding!!
        }

    override fun isInitialized() = cacheBinding != null
}


fun observeFragmentDestroy(
    fragment: Fragment,
    callback: () -> Unit
) {
    fragment.viewLifecycleOwnerLiveData.observe(fragment) { viewLifecycleOwner ->

        viewLifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                Log.i("fragmentViewBinding", "${fragment::class.java.simpleName} call onDestroy")
                Handler(Looper.getMainLooper()).post {
                    callback.invoke()
                }
                // fragment.requireView().post {
                //     callback.invoke()
                // }
                viewLifecycleOwner.lifecycle.removeObserver(this)
            }
        })
    }
}

private fun checkBindingFirstInvoke(fragment: Fragment) {
    if (!fragment.viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
        error("Cannot access view bindings. View lifecycle is ${fragment.viewLifecycleOwner.lifecycle.currentState}!")
    }
}


