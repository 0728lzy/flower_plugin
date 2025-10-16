package com.cslt.maogoufanyi.widget.popup.dj

import android.content.Context
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.cslt.maogoufanyi.R
import com.cslt.maogoufanyi.csj.ZYMAllAdsUtils
import com.cslt.maogoufanyi.ui.activity.MainActivity


import com.lxj.xpopup.core.CenterPopupView

class ExitDialogPopup(context: Context) : CenterPopupView(context){
    var listener: OnExitClickListener? = null

    private lateinit var  feed_container_exit_dialog: FrameLayout

    override fun getImplLayoutId(): Int {
        return R.layout.popup_exit_dialog
    }


    override fun onCreate() {
        super.onCreate()

        var exit_tv_cancel = findViewById<ImageView>(R.id.exit_tv_cancel)
        var exit_tv_cancel1 = findViewById<TextView>(R.id.exit_tv_cancel1)
        var exit_tv_check = findViewById<TextView>(R.id.exit_tv_check)
        feed_container_exit_dialog = findViewById(R.id.feed_container_dialog_exit)


        exit_tv_cancel.setOnClickListener {
            dismiss()
            listener?.closeDialog()
        }
        exit_tv_cancel1.setOnClickListener {
            dismiss()
            listener?.cancel()
        }

        exit_tv_check.setOnClickListener {

            listener?.ok()
        }

    }

    override fun onShow() {
        super.onShow()
        ZYMAllAdsUtils.loadSimpleAll(context as MainActivity,"信息",feed_container_exit_dialog)
    }

    override fun onDismiss() {

        super.onDismiss()

    }



    interface OnExitClickListener {
        fun cancel()
        fun closeDialog()
        fun ok()
    }
}