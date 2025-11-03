package com.weini.catdog.widget.dialog.lzy

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.TextView
import com.weini.catdog.R

class PermissionDialog(myContext:Context) :Dialog(myContext) {
    private var cancel:TextView
    private var set:TextView
    private var permission:TextView
    private var tips:TextView
    private lateinit var onPermissionClickListener: OnPermissionClickListener
    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_request_permission)
        setCancelable(false)
        set=findViewById<TextView>(R.id.pop_permission_set)
//        set.setOnClickListener{
//            dismiss()
//            XXPermissions.startPermissionActivity(myContext, Permission.ACCESS_FINE_LOCATION)
//        }
        set.setOnClickListener {
            onPermissionClickListener.onSetClickListener(set)
        }
        cancel=findViewById<TextView>(R.id.pop_permission_cancel)
//        cancel.setOnClickListener{
//            dismiss()
//            Toast.makeText(myContext,"未开启权限，地图和定位相关功能暂时无法使用",Toast.LENGTH_SHORT).show()
//        }
        cancel.setOnClickListener {
            onPermissionClickListener.onCancelClickListener(cancel)
        }
        permission=findViewById(R.id.pop_permission_permission)
        tips=findViewById(R.id.pop_permission_tips)
    }

    interface OnPermissionClickListener{
        fun onSetClickListener(view: TextView)
        fun onCancelClickListener(view: TextView)
    }
    fun setRequestPermissionText(per:String){
        permission.text="$per"
    }
    fun setRequestTipsText(tip:String){
        tips.text=tip
    }
    fun setOnPermissionClickListener(onPermissionClickListener: OnPermissionClickListener){
        this.onPermissionClickListener=onPermissionClickListener
    }
}