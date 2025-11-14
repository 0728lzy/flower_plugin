package com.cslianta.catdog.widget.dialog

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.TextView
import com.cslianta.catdog.R

class LoadingDiaLog(val mContext:Context,val text:String="翻译中...") : Dialog(mContext){
    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_loading)
        setCancelable(false)
        findViewById<TextView>(R.id.dialog_loading_text).text=text
    }
}