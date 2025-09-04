package com.weini.maogou.base.dj

import android.app.Dialog
import android.view.Gravity


/**
 * create by liaohailong
 * time 2023/8/24 14:17
 * desc: dialog业务基类
 */
abstract class BaseDialog : RootDialog() {

    override fun onDialog(dialog: Dialog) {

    }

    override fun getGravity(): Int {
        return Gravity.CENTER
    }

}