package com.cslt.maogoufanyi.ext.dj

import android.view.View
import androidx.viewbinding.ViewBinding
import com.cslt.maogoufanyi.R
import com.cslt.maogoufanyi.utils.dj.AntiShakeUtils

/**
 * @param onClickListener 防止快速点击
 */
fun View.thrillClickListener(block: (View) -> Unit) {
    this.setOnClickListener {
        if (AntiShakeUtils.isInvalidClick(it)) return@setOnClickListener
        block.invoke(it)
    }
}

inline fun <reified VB : ViewBinding> View.getBinding() = getBinding(VB::class.java)

@Suppress("UNCHECKED_CAST")
fun <VB : ViewBinding> View.getBinding(clazz: Class<VB>) =
    getTag(R.id.tag_view_binding) as? VB ?: (clazz.getMethod("bind", View::class.java)
        .invoke(null, this) as VB)
        .also { setTag(R.id.tag_view_binding, it) }


