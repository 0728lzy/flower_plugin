package com.cslt.maogoufanyi.utils.lzy

import android.content.Context
import android.widget.TextView
import android.widget.Toast
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.cslt.maogoufanyi.utils.xxpermission.PermissionInterceptor
import com.cslt.maogoufanyi.widget.dialog.lzy.PermissionDialog

object PermissionUtils {
    /**
     * 尝试执行某项操作，并在检查权限后执行或弹出权限请求对话框。
     * @param context 上下文对象，用于执行操作和显示对话框。
     * @param requestPermissions 需要检查的权限数组。
     * @param requestFailToast 权限请求失败时要显示的 Toast 提示文本。
     * @param dialogTile 对话框的标题文本。
     * @param dialogPermissionText 对话框中权限请求的文本。
     * @param doSomething 需要执行的操作，当权限被授予时。
     */
    fun tryToDoSomethingWithCheckPermission(
        context: Context,
        requestPermissions: Array<String>,
        requestFailToast: String,
        dialogTile: String,
        dialogPermissionText: String,
        doSomething: () -> Unit
    ) {
        // 检查权限是否被授予
        if (!XXPermissions.isGranted(context, requestPermissions)) {
            // 如果权限未被授予，显示权限请求对话框
            PermissionDialog(context).apply {
                // 设置对话框的标题文本和权限请求文本
                setRequestTipsText(dialogTile)
                setRequestPermissionText(dialogPermissionText)
                // 设置对话框的点击监听器
                setOnPermissionClickListener(object : PermissionDialog.OnPermissionClickListener {
                    override fun onSetClickListener(view: TextView) {
                        // 点击设置按钮，关闭对话框并请求权限
                        dismiss()
                        requestPermission(context, requestPermissions, requestFailToast)
                    }

                    override fun onCancelClickListener(view: TextView) {
                        // 点击取消按钮，关闭对话框并显示权限请求失败的提示
                        dismiss()
                        Toast.makeText(context, requestFailToast, Toast.LENGTH_SHORT).show()
                    }
                })
                // 显示权限请求对话框
                show()
            }
        } else {
            // 如果权限已被授予，执行传入的操作
            doSomething()
        }
    }
    /**
     * 请求指定权限，并根据情况是否保存权限请求状态到 SharedPreferences。
     * @param context 上下文对象，用于执行权限请求和获取 SharedPreferences。
     * @param permissions 需要请求的权限数组。
     * @param failToast 权限请求失败时要显示的 Toast 提示文本。
     * @param saveToPreferences 是否将权限请求状态保存到 SharedPreferences，默认为 false。
     */
    fun requestPermission(
        context: Context,
        permissions: Array<String>,
        failToast: String,
        saveToPreferences: Boolean = false
    ) {
        // 使用 XXPermissions 请求权限
        XXPermissions.with(context)
            .permission(permissions)
            .request(object : OnPermissionCallback {
                override fun onGranted(permissions: MutableList<String>, allGranted: Boolean) {
                    // 当所有请求的权限都被授予时调用
                    LZYLog.i("permission", "request notification permission SUCCESS")
                }

                override fun onDenied(permissions: MutableList<String>, doNotAskAgain: Boolean) {
                    // 当有权限被拒绝时调用
                    if (doNotAskAgain) {
                        // 如果用户选择了“不再询问”，则跳转到应用程序设置页面
                        XXPermissions.startPermissionActivity(context, arrayOf(
                            Permission.READ_CALENDAR,
                            Permission.WRITE_CALENDAR))
                        LZYLog.i("permission", "request notification permission NEVER")
                    } else {
                        // 如果权限被拒绝，但不是“不再询问”，则显示提示 Toast
                        LZYLog.i("permission", "request notification permission FAIL")
                        Toast.makeText(context, failToast, Toast.LENGTH_SHORT).show()
                    }
                    // 如果需要保存权限请求状态到 SharedPreferences
                    if (saveToPreferences) {
                        val sharedPref =
                            context.getSharedPreferences("permission", Context.MODE_PRIVATE)
                        with(sharedPref.edit()) {
                            putBoolean("request", true)
                            apply()
                        }
                    }
                }
            })
    }

    /**
     * 只在首次请求权限时执行请求，并将请求状态保存到 SharedPreferences。
     * @param context 上下文对象，用于执行权限请求和获取 SharedPreferences。
     * @param requestPermissions 需要请求的权限数组。
     * @param requestFailToast 权限请求失败时要显示的 Toast 提示文本。
     */
    fun requestPermissionOnlyOneTime(
        context: Context,
        requestPermissions: Array<String>,
        requestFailToast: String
    ) {
        // 获取 SharedPreferences 中的权限请求状态
        val sharedPref = context.getSharedPreferences("permission", Context.MODE_PRIVATE)
        val isRequest: Boolean = sharedPref.getBoolean("request", false)
        // 如果权限未被请求过，则执行权限请求，并保存请求状态到 SharedPreferences
        if (!isRequest) {
            requestPermission(context, requestPermissions, requestFailToast, true)
        }
    }

    /**
     * 尝试在检查权限和请求权限后执行操作的函数。
     *
     * @param context 上下文，用于处理权限请求和显示提示信息。
     * @param requestPermissions 请求的权限数组。
     * @param requestCode 请求权限时使用的请求码。
     * @param requestFailToast 权限请求失败时显示的 Toast 消息。
     * @param doSomething 当权限被授予时执行的操作。
     */
    fun tryToDoSomethingWithCheckPermissionAndCode(
        context: Context,
        requestPermissions: Array<String>,
        requestCode:Int,
        requestFailToast: String,
        doSomething: () -> Unit
    ){
        if (XXPermissions.isGranted(context, requestPermissions)) {
            // 如果权限已经被授予，则直接执行操作
            doSomething()
        } else {
            // 如果权限未被授予，则请求权限
            XXPermissions.with(context)
                .permission(requestPermissions)
                .interceptor(PermissionInterceptor(requestCode))
                .request(object : OnPermissionCallback {
                    override fun onGranted(permissions: List<String>, all: Boolean) {
                        // 当所有请求的权限被授予时执行操作
                        if (all) {
                            doSomething()
                        }
                    }

                    override fun onDenied(permissions: List<String>, never: Boolean) {
                        if (never) {
                            // 如果权限被永久拒绝，引导用户去设置中开启权限
                            XXPermissions.startPermissionActivity(context, permissions)
                        } else {
                            // 如果权限被拒绝，显示请求失败的 Toast 消息
                            Toast.makeText(context, requestFailToast, Toast.LENGTH_SHORT).show()
                        }
                    }
                })
        }
    }

}