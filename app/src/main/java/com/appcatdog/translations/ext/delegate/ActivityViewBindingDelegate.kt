package com.appcatdog.translations.ext.delegate

import android.view.LayoutInflater
import androidx.annotation.MainThread
import androidx.viewbinding.ViewBinding
import com.appcatdog.translations.base.dj.RootActivity
import kotlin.reflect.KClass



/**
 * 在 Activity 中创建 ViewBinding 绑定类，
 *
 * @param inflateMethodRef (LayoutInflater) -> T) VB 中 inflate 方法的函数引用
 * @param VB ViewBinding 的子类
 */
@MainThread
inline fun <reified VB : ViewBinding> RootActivity.viewBinding(noinline inflateMethodRef: ((LayoutInflater) -> VB)? = null): Lazy<VB> =
    ActivityViewBindingDelegate(this, VB::class, inflateMethodRef)


class ActivityViewBindingDelegate<VB : ViewBinding>(
    private val activity: RootActivity,
    private val kClass: KClass<*>,
    private val inflateMethodRef: ((LayoutInflater) -> VB)?
) : Lazy<VB> {
    private var cacheBinding: VB? = null

    override val value: VB
        get() {
            var viewBinding = cacheBinding
            if (viewBinding == null) {
                viewBinding = inflateMethodRef?.invoke(activity.layoutInflater)
                    ?: @Suppress("UNCHECKED_CAST")
                    kClass.java.getMethod(METHOD_INFLATE, LayoutInflater::class.java)
                        .invoke(null, activity.layoutInflater) as VB
                activity.setContentView(viewBinding.root)
                cacheBinding = viewBinding
            }
            return viewBinding!!
        }

    override fun isInitialized() = cacheBinding != null

}

const val METHOD_INFLATE = "inflate"