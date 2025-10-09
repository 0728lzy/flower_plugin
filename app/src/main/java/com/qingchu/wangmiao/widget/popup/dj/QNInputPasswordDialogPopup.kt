package com.qingchu.wangmiao.widget.popup.dj

import android.content.Context
import android.widget.EditText
import android.widget.TextView
import com.qingchu.wangmiao.R
import com.lxj.xpopup.core.CenterPopupView

class QNInputPasswordDialogPopup(context: Context) : CenterPopupView(context){
    var listener: OnInputPasswordListener? = null



    override fun getImplLayoutId(): Int {
        return R.layout.popup_inputpassword_dialog
    }


    override fun onCreate() {
        super.onCreate()

        var exit_tv_cancel1 = findViewById<TextView>(R.id.exit_tv_cancel1)
        var exit_tv_check = findViewById<TextView>(R.id.exit_tv_check)
        var input_et_password = findViewById<EditText>(R.id.input_et_password)
        exit_tv_cancel1.setOnClickListener {
            dismiss()
            listener?.cancel()
        }

        exit_tv_check.setOnClickListener {

            var password = input_et_password.text.toString().trim()
            listener?.ok(password)
        }

    }


    override fun onDismiss() {

        super.onDismiss()

    }



    interface OnInputPasswordListener {
        fun cancel()
        fun ok(password:String)
    }
}