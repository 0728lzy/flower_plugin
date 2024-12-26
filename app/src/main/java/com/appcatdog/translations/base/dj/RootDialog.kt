package com.appcatdog.translations.base.dj

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.appcatdog.translations.R


/**
 * create by liaohailong
 * time 2023/8/21 16:39
 * desc: 通用基类，跟业务无关
 */
abstract class RootDialog : DialogFragment() {

    private var showing = false

    fun show(activity: FragmentActivity) {
        if (showing) return
        showing = true
        val fm = activity.supportFragmentManager
        val tag = javaClass.canonicalName
        try {
            show(fm, tag)
        } catch (ex: Exception) {
            // ignore
        }
    }

    private var onDismissListener: Runnable? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext(), getThemeResId())
        dialog.setContentView(getLayoutId())
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        val window = dialog.window
        if (window != null) {
            val attributes = window.attributes
            attributes.width = WindowManager.LayoutParams.MATCH_PARENT
            attributes.height = getWindowHeight()
            attributes.gravity = getGravity()
            window.attributes = attributes

        }
        onDialog(dialog)
        return dialog
    }


    override fun onStart() {
        super.onStart()
        val dialog = dialog ?: return
        val window = dialog.window ?: return
        val decorView = window.decorView
        val contentViewWrapper = decorView.findViewById<ViewGroup>(android.R.id.content)
        val contentView = contentViewWrapper.getChildAt(0)
        initView(contentView)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        showing = false
        if (onDismissListener != null) {
            onDismissListener!!.run()
        }
    }

    /**
     * @param onDismissListener 弹窗消失监听
     */
    fun setOnDismissListener(onDismissListener: Runnable?): RootDialog {
        this.onDismissListener = onDismissListener
        return this
    }

    /**
     * @param dialog 刚创建好，尚未显示，可修改配置
     */
    protected abstract fun onDialog(dialog: Dialog)

    /**
     * @return dialog主题资源id
     */
    protected open fun getThemeResId(): Int {
        return R.style.dialog_style
    }

    /**
     * @return dialog内容布局资源id
     */
    @LayoutRes
    protected abstract fun getLayoutId(): Int

    /**
     * @return dialog在window中摆放的位置
     */
    protected abstract fun getGravity(): Int

    /**
     * @return 获取window高度
     */
    protected open fun getWindowHeight(): Int {
        return WindowManager.LayoutParams.WRAP_CONTENT
    }

    /**
     * @param view 初始化view，在onStart函数中回调
     */
    protected abstract fun initView(view: View)

    /**
     * @return true 表示弹窗正在展示
     */
    fun isShowing(): Boolean {
        if (showing) return true
        val dialog = dialog
        return dialog != null && dialog.isShowing
    }

    override fun dismiss() {
        try {
            super.dismissAllowingStateLoss()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun dismiss(listener: Runnable) {
        onDismissListener = listener
        dismissAllowingStateLoss()
    }

}