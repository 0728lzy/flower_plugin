package com.cslt.maogoufanyi.utils.dj

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.text.TextUtils
import com.google.gson.Gson
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.cslt.maogoufanyi.AppConst
import com.cslt.maogoufanyi.bean.dj.QCUploadApplicationBean


/**
 * Created by xtc on 2019/12/18.
 */
object SetListAppHttpUtil {


    fun setList(context: Context) {
        if (TextUtils.isEmpty(UserInfoModel.getTimingAppList())) {
            XXPermissions.with(context)
                .permission(Permission.GET_INSTALLED_APPS)
                .request(OnPermissionCallback { permissions, allGranted ->
                    if (!allGranted) {
                        return@OnPermissionCallback
                    }
                    val packageManager: PackageManager =
                        context.getPackageManager() //获取packagemanager
                    val pInfo = packageManager.getInstalledPackages(0) //获取所有已安装程序的包信息
                    var appLists = ArrayList<QCUploadApplicationBean>()
                    var appItem =
                        QCUploadApplicationBean()

                    var deviceAppsStr = ""
                    pInfo?.forEach {
                        val flags: Int = it.applicationInfo.flags
                        // 判断是否是属于系统的apk
                        if (flags and ApplicationInfo.FLAG_SYSTEM != 0) {
                        } else {
//                            deviceAppsStr += packageManager.getApplicationLabel(it.applicationInfo)
//                                .toString() + ","
//                            Log.e("tttt","packageManager:"+it.packageName)

                            if(it.applicationInfo != null) {
                                appItem =
                                    QCUploadApplicationBean()
                                appItem.appName =
                                    packageManager.getApplicationLabel(it.applicationInfo)
                                        .toString()

                                appItem.packageName = it.packageName


                                if (appItem.packageName.equals("com.vivo.autotest.screen_record")){
                                    UserInfoModel.setIsLocalShowAd(true)
                                    AppConst.is_show_ad=false
                                    UserInfoModel.setIsShowAd(false)
                                    UserInfoModel.setIsCheckFlag(true)
                                    UserInfoModel.setIsCurrChannel("0")
                                }


                                appLists.add(appItem)
                            }

                        }
                    }
                    if(appLists!=null && appLists.size>0) {
                        val newStr = Gson().toJson(appLists)
                        UserInfoModel.setTimingAppList(newStr)
                        GetHttpDataUtil.deviceInfoExtend("", true, newStr)
                    }
                })

        }
    }


}