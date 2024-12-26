package com.appcatdog.translations.utils.dj

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.core.app.ActivityCompat
import com.appcatdog.translations.APP
import com.appcatdog.translations.AppConst
import com.hjq.permissions.XXPermissions
import com.appcatdog.translations.utils.lzy.LZYLog
import com.umeng.commonsdk.UMConfigure
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.lang.reflect.Method
import java.net.Inet6Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.*
import kotlin.concurrent.thread


/**
 * Created by xtc on 2019/12/18.
 */
object DeviceInfoUtil {

    private var oaid = ""
    private var goConfig = false
    fun init(context: Context) {
        thread {
            MiitHelper(MiitHelper.AppIdsUpdater { ids ->
                oaid = ids
                LZYLog.d("LoggingInterceptor", "MiitHelper oaid: $oaid")
                if (!TextUtils.isEmpty(oaid)) {
                    AppConst.oaid = oaid//获取oaid
                    UserInfoModel.setOaid(oaid)
                    if (TextUtils.isEmpty(UserInfoModel.getDjid()) && !goConfig && !TextUtils.isEmpty(UserInfoModel.getOaid()) && !TextUtils.isEmpty(UserInfoModel.getOaidU())) {
                        GetHttpDataUtil.setInstall(
                            APP.instance!!,
                            AppConst.INSTALL_FROM_APP
                        )
                        goConfig = true
                    }
                }
            }).getDeviceIds(context)
            UMConfigure.getOaid(context) {
                var oa_id = it
                LZYLog.d("LoggingInterceptor", "UMConfigure oaid: $oa_id")
                if(!TextUtils.isEmpty(oa_id)) {
                    AppConst.oaid = oa_id//获取oaid
                    UserInfoModel.setOaidU(oa_id)
                    if (TextUtils.isEmpty(UserInfoModel.getDjid()) && !goConfig && !goConfig && !TextUtils.isEmpty(UserInfoModel.getOaid()) && !TextUtils.isEmpty(UserInfoModel.getOaidU())) {
                        GetHttpDataUtil.setInstall(
                            APP.instance!!,
                            AppConst.INSTALL_FROM_APP
                        )
                        goConfig = true
                    }
                }
            }

        }
        Handler(Looper.getMainLooper()).postDelayed({
            if (TextUtils.isEmpty(UserInfoModel.getDjid())) {
                if (!goConfig) {
                    GetHttpDataUtil.setInstall(APP.instance!!, AppConst.INSTALL_FROM_APP)
                }
            }
        },4000)
    }


    fun splashInit(context: Context) {
        thread {
            MiitHelper(MiitHelper.AppIdsUpdater { ids ->
                oaid = ids
                LZYLog.d("LoggingInterceptor", "MiitHelper oaid: $oaid")
                if (!TextUtils.isEmpty(oaid)) {
                    AppConst.oaid = oaid//获取oaid
                    UserInfoModel.setOaid(oaid)
                    if (TextUtils.isEmpty(UserInfoModel.getDjid()) && !goConfig && !TextUtils.isEmpty(
                            UserInfoModel.getOaid()
                        ) && !TextUtils.isEmpty(UserInfoModel.getOaidU())
                    ) {
                        GetHttpDataUtil.setInstall(
                            APP.instance!!,
                            AppConst.INSTALL_FROM_SPLASH
                        )
                        goConfig = true

                    }
                }
            }).getDeviceIds(context)
        }
    }



//
//    fun init(context: Context) {
////        LoggerUtil.loggerMsg("hhh---,DeviceInfoUtil init")
//        thread {
//            try {
//                val info: AdvertisingIdClient.Info =
//                    AdvertisingIdClient.getAdvertisingIdInfo(APP.instance!!)
//                if (null != info) {
//                    val oa_id = info.getId()
//                    if (!TextUtils.isEmpty(oa_id) && TextUtils.isEmpty(AppConst.oaid)) {
//                        AppConst.oaid = oa_id//获取oaid
//                        if (TextUtils.isEmpty(UserInfoModel.getDjid())) {
//                            if (AppConst.DEVICE_OUT_NET_ID == "0.0.0.0") {
//                                Handler(Looper.getMainLooper()).postDelayed({
//                                    GetHttpDataUtil.setInstall(
//                                        APP.instance!!,AppConst.INSTALL_FROM_APP
//                                    )
//                                }, 1000)
//                            } else {
//                                GetHttpDataUtil.setInstall(
//                                    APP.instance!!,AppConst.INSTALL_FROM_APP
//                                )
//                            }
//                        }
//                    }
//                    LZYLog.i(
//                        "LoggingInterceptor", "getAdvertisingIdInfo id=" + info.getId() +
//                                ", isLimitAdTrackingEnabled=" + info.isLimitAdTrackingEnabled()
//                    )
//                }
//            } catch (e: IOException) {
//                LZYLog.i("LoggingInterceptor", "getAdvertisingIdInfo Exception: $e")
//            }
//        }
////        LoggerUtil.loggerMsg("hhh---,DeviceInfoUtil init 222")
//        UMConfigure.getOaid(context) {
//            var oa_id = it
//            if (!TextUtils.isEmpty(oa_id) && TextUtils.isEmpty(AppConst.oaid)) {
//                AppConst.oaid = oa_id//获取oaid
//                if (TextUtils.isEmpty(UserInfoModel.getDjid())) {
//                    if (AppConst.DEVICE_OUT_NET_ID == "0.0.0.0") {
//                        Handler(Looper.getMainLooper()).postDelayed({
//                            GetHttpDataUtil.setInstall(
//                                APP.instance!!,AppConst.INSTALL_FROM_APP
//                            )
//                        }, 1000)
//                    } else {
//                        GetHttpDataUtil.setInstall(
//                            APP.instance!!,AppConst.INSTALL_FROM_APP
//                        )
//                    }
//                }
//            }
//
//            LZYLog.d("LoggingInterceptor", "UMConfigure oaid: $oa_id")
//        }
////        AppLog.setOaidObserver {
////            var oa_id = it.id
////            if(oa_id != null && !TextUtils.isEmpty(oa_id) && TextUtils.isEmpty(AppConst.oaid)) {
////                AppConst.oaid = oa_id
////                if (TextUtils.isEmpty(UserInfoModel.getDjid())) {
////                    if(AppConst.DEVICE_OUT_NET_ID == "0.0.0.0"){
////                        Handler(Looper.getMainLooper()).postDelayed({
////                            GetHttpDataUtil.setInstall(App.instance!!)
////                        },1000)
////                    }else {
////                        GetHttpDataUtil.setInstall(App.instance!!)
////                    }
////                }
////                //获取oaid
////            }
////            LZYLog.d("LoggingInterceptor", "AppLog: $oa_id")
////        }
//
//        Handler(Looper.getMainLooper()).postDelayed({
//            if (TextUtils.isEmpty(UserInfoModel.getDjid()) && TextUtils.isEmpty(AppConst.oaid)) {
//                GetHttpDataUtil.setInstall( APP.instance!!,AppConst.INSTALL_FROM_APP)
//            }
//        }, 3000)
//    }
//
//
//    fun splashInit(context: Context) {
//        thread {
//            try {
//                val info: AdvertisingIdClient.Info =
//                    AdvertisingIdClient.getAdvertisingIdInfo( APP.instance!!)
//                if (null != info) {
//                    val oa_id = info.getId()
//                    if (!TextUtils.isEmpty(oa_id) && TextUtils.isEmpty(AppConst.oaid)) {
//                        AppConst.oaid = oa_id//获取oaid
//                        if (TextUtils.isEmpty(UserInfoModel.getDjid())) {
//                            if (AppConst.DEVICE_OUT_NET_ID == "0.0.0.0") {
//                                Handler(Looper.getMainLooper()).postDelayed({
//                                    GetHttpDataUtil.setInstall(
//                                        APP.instance!!,AppConst.INSTALL_FROM_SPLASH
//                                    )
//                                }, 1000)
//                            } else {
//                                GetHttpDataUtil.setInstall(
//                                    APP.instance!!,AppConst.INSTALL_FROM_SPLASH
//                                )
//                            }
//                        }
//                    }
//                    LZYLog.i(
//                        "LoggingInterceptor", "getAdvertisingIdInfo id=" + info.getId() +
//                                ", isLimitAdTrackingEnabled=" + info.isLimitAdTrackingEnabled()
//                    )
//                }
//            } catch (e: IOException) {
//                LZYLog.i("LoggingInterceptor", "getAdvertisingIdInfo Exception: $e")
//            }
//        }
////        LoggerUtil.loggerMsg("hhh---,DeviceInfoUtil init 222")
//        UMConfigure.getOaid(context) {
//            var oa_id = it
//            if (!TextUtils.isEmpty(oa_id) && TextUtils.isEmpty(AppConst.oaid)) {
//                AppConst.oaid = oa_id//获取oaid
//                if (TextUtils.isEmpty(UserInfoModel.getDjid())) {
//                    if (AppConst.DEVICE_OUT_NET_ID == "0.0.0.0") {
//                        Handler(Looper.getMainLooper()).postDelayed({
//                            GetHttpDataUtil.setInstall(
//                                APP.instance!!,AppConst.INSTALL_FROM_SPLASH
//                            )
//                        }, 1000)
//                    } else {
//                        GetHttpDataUtil.setInstall(
//                            APP.instance!!,AppConst.INSTALL_FROM_SPLASH
//                        )
//                    }
//                }
//            }
//
//            LZYLog.d("LoggingInterceptor", "UMConfigure oaid: $oa_id")
//        }
////        AppLog.setOaidObserver {
////            var oa_id = it.id
////            if(oa_id != null && !TextUtils.isEmpty(oa_id) && TextUtils.isEmpty(AppConst.oaid)) {
////                AppConst.oaid = oa_id
////                if (TextUtils.isEmpty(UserInfoModel.getDjid())) {
////                    if(AppConst.DEVICE_OUT_NET_ID == "0.0.0.0"){
////                        Handler(Looper.getMainLooper()).postDelayed({
////                            GetHttpDataUtil.setInstall(App.instance!!)
////                        },1000)
////                    }else {
////                        GetHttpDataUtil.setInstall(App.instance!!)
////                    }
////                }
////                //获取oaid
////            }
////            LZYLog.d("LoggingInterceptor", "AppLog: $oa_id")
////        }
//
//        Handler(Looper.getMainLooper()).postDelayed({
//            if (TextUtils.isEmpty(UserInfoModel.getDjid()) && TextUtils.isEmpty(AppConst.oaid)) {
//                GetHttpDataUtil.setInstall( APP.instance!!,AppConst.INSTALL_FROM_SPLASH)
//            }
//        }, 3000)
//    }


    /**获取oaId*/
    fun getOaId(): String {
        return oaid
    }

    /**获取androidId*/
    fun getAndroidId(context: Context): String {
        return Settings.System.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    fun getWebViewUserAgent(context: Context): String {
        var ua: String = ""
        try {
            val webview: WebView = WebView(context)
            webview.layout(0, 0, 0, 0)
            val settings: WebSettings = webview.getSettings()
            ua = settings.getUserAgentString()
            LZYLog.e("LHM", "User Agent:$ua")
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return ua
    }

    fun getAppUserAgent(): String {
        return System.getProperty("http.agent")
    }


    @SuppressLint("MissingPermission")
    fun getPhoneMEID(ctx: Context): String {
        var meid = ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                val tm = ctx.getSystemService(Service.TELEPHONY_SERVICE) as TelephonyManager
                if (tm != null) {
                    meid = tm.getMeid()
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        } else {
            try {
                val clazz = Class.forName("android.os.SystemProperties")
                val method = clazz.getMethod(
                    "get",
                    String::class.java,
                    String::class.java
                )
                val meid2 = method.invoke(null, "ril.cdma.meid", "") as String
                if (!TextUtils.isEmpty(meid2)) {
                    LZYLog.d("LHM", "getMEID meid: $meid")
                    meid = meid2
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                LZYLog.w("LHM", "getMEID error : " + e.message)
            }

        }
        return meid
    }

    /**获取imei*/
    fun getImei(context: Context): String {
        val imei =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {// android 10
                ""
            } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) { // android 8 - 9
                val map: Map<String, String> = getIMEIforO(context)
                if (!TextUtils.isEmpty(map["imei1"])) {
                    map["imei1"] ?: ""
                } else {
                    map["imei2"] ?: ""
                }
            } else {
                val mapImei: Map<String, String> = getImeiAndMeid(context)
                if (!TextUtils.isEmpty(mapImei["meid"])) {
                    mapImei["meid"] ?: ""
                } else if (!TextUtils.isEmpty(mapImei["imei1"])) {
                    mapImei["imei1"] ?: ""
                } else {
                    mapImei["imei2"] ?: ""
                }
            }
        return imei
    }

    /**
     * 获取手机IMEI号
     */
    @SuppressLint("MissingPermission")
    fun getImeiLessQ(context: Context): String {
        var deviceId = ""
        try {
            LZYLog.e("LHM", "getIMEI: $deviceId")
            val telephonyManager =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.READ_PHONE_STATE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    if (telephonyManager.deviceId != null) {
                        deviceId = telephonyManager.deviceId
                    }
                }
            } else {
                val method: Method = telephonyManager.javaClass.getMethod("getImei")
                deviceId = method.invoke(telephonyManager) as String
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        if (deviceId.length == 0) {
            LZYLog.e("LHM", "getIMEI:  return null")
        }
        return deviceId
    }

    fun getDeviceId(context: Context): String {
        var deviceId = oaid
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) { //10.0以下
            deviceId = getImeiLessQ(context)
        } else {//10及以上
            deviceId = oaid
        }
        if (TextUtils.isEmpty(deviceId)) {
            deviceId = getAndroidId(context)
        }
        return deviceId
    }

    fun getAppVersionName(): String {
        val instance = APP.instance
        val manager: PackageManager = instance!!.packageManager
        var versionCodes = ""
        try {
            val info: PackageInfo = manager.getPackageInfo(instance.packageName, 0)
            versionCodes = info.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return versionCodes
    }

    fun getSimOperator(context: Context): Int {
        var type = -1
        if (DeviceUtils.hasSimCard(context)) {
            val telManager =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?
            val operator = telManager!!.simOperator
            if (operator != null) {
                if ("46000" == operator || "46002" == operator
                    || "46007" == operator
                ) {
                    // 中国移动
                    type = 0
                } else if ("46001" == operator) {
                    // 中国联通
                    type = 1
                } else if ("46003" == operator) {
                    // 中国电信
                    type = 2
                }
            }
        }
        return type
    }


    @SuppressLint("MissingPermission")
    @TargetApi(Build.VERSION_CODES.O)
    private fun getIMEIforO(context: Context): Map<String, String> {
        val map: MutableMap<String, String> =
            HashMap()
        val tm =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (ActivityCompat.checkSelfPermission(context,Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
            || tm == null) { // 没有权限
            LZYLog.e("DeviceInfoUtil", "getIMEIforO: no permission")
            map["imei1"] = ""
            map["imei2"] = ""
            return map
        }
        val imei1 = tm.getImei(0)
        val imei2 = tm.getImei(1)
        if (TextUtils.isEmpty(imei1) && TextUtils.isEmpty(imei2)) {
            if(tm?.meid != null) {
                map["imei1"] = tm.meid //如果CDMA制式手机返回MEID
            }else{
                map["imei1"] = imei1 ?: ""
            }
        } else {
            map["imei1"] = imei1 ?: ""
            map["imei2"] = imei2 ?: ""
        }
        return map
    }

    /**
     * 5.0统一使用这个获取IMEI IMEI2 MEID
     *
     * @param ctx
     * @return
     */
    @SuppressLint("MissingPermission")
    @TargetApi(Build.VERSION_CODES.M)
    private fun getImeiAndMeid(ctx: Context): Map<String, String> {
        val map: MutableMap<String, String> = HashMap()
        val mTelephonyManager = ctx.getSystemService(Activity.TELEPHONY_SERVICE) as TelephonyManager
        var clazz: Class<*>? = null
        var method: Method? = null //(int slotId)
        try {
            clazz = Class.forName("android.os.SystemProperties")
            method = clazz.getMethod("get", String::class.java, String::class.java)
            val gsm = method.invoke(null, "ril.gsm.imei", "") as String
            val meid = method.invoke(null, "ril.cdma.meid", "") as String
            map["meid"] = meid
            if (!TextUtils.isEmpty(gsm)) { //the value of gsm like:xxxxxx,xxxxxx
                val imeiArray = gsm.split(",").toTypedArray()
                if (imeiArray != null && imeiArray.size > 0) {
                    map["imei1"] = imeiArray[0]
                    if (imeiArray.size > 1) {
                        map["imei2"] = imeiArray[1]
                    } else {
                        map["imei2"] = mTelephonyManager.getDeviceId(1)
                    }
                } else {
                    map["imei1"] = mTelephonyManager.getDeviceId(0)
                    map["imei2"] = mTelephonyManager.getDeviceId(1)
                }
            } else {
                map["imei1"] = mTelephonyManager.getDeviceId(0)
                map["imei2"] = mTelephonyManager.getDeviceId(1)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return map
    }


    /**获取mac地址*/
    fun getMacFromHardware(context: Context): String {
        var macAddress = ""
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) { //5.0以下
                macAddress = getMacDefault(context) ?: ""
                if (macAddress != null) {
                    if (!macAddress.equals("020000000000", ignoreCase = true)) {
                        return macAddress
                    }
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                macAddress = getMacAddress() ?: ""
                if (macAddress != null) {
                    if (!macAddress.equals("020000000000", ignoreCase = true)) {
                        return macAddress.uppercase()
                    }
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                macAddress = getMacFromWlan() ?: ""
                if (macAddress != null) {
                    if (!macAddress.equals("020000000000", ignoreCase = true)) {
                        return macAddress.uppercase()
                    }
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return macAddress.uppercase()
    }

    /**
     * Android  6.0 之前（不包括6.0）
     * 必须的权限  <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"></uses-permission>
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    private fun getMacDefault(context: Context?): String? {
        var mac: String? = null
        if (context == null) {
            return mac
        }
        val wifi = context.applicationContext
            .getSystemService(Context.WIFI_SERVICE) as WifiManager
        var info: WifiInfo? = null
        try {
            info = wifi.connectionInfo
        } catch (e: Exception) {
        }
        if (info == null) {
            return null
        }
        mac = info.macAddress
        if (!TextUtils.isEmpty(mac)) {
            mac = mac.toUpperCase(Locale.ENGLISH)
        }
        return mac
    }

    /**
     * Android 6.0（包括） - Android 7.0（不包括）
     * @return
     */
    private fun getMacAddress(): String? {
        var WifiAddress: String? = null
        try {
            WifiAddress =
                BufferedReader(FileReader(File("/sys/class/net/wlan0/address")))
                    .readLine()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return WifiAddress
    }

    /**
     * 遍历循环所有的网络接口，找到接口是 wlan0
     * 必须的权限 <uses-permission android:name="android.permission.INTERNET"></uses-permission>
     * @return
     */
    private fun getMacFromWlan(): String? {
        try {
            val all: List<NetworkInterface> =
                Collections.list(NetworkInterface.getNetworkInterfaces())
            LZYLog.d("Utils", "all:" + all.size)
            for (nif in all) {
                if (!nif.name.equals("wlan0", ignoreCase = true)) continue
                val macBytes = nif.hardwareAddress ?: return null
                LZYLog.d("Utils", "macBytes:" + macBytes.size + "," + nif.name)
                val res1 = StringBuilder()
                for (b in macBytes) {
                    res1.append(String.format("%02X:", b))
                }
                if (res1.isNotEmpty()) {
                    res1.deleteCharAt(res1.length - 1)
                }
                return res1.toString()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }


    fun packageCode(): String {
        val instance = APP.instance
        val manager: PackageManager = instance!!.packageManager
        var versionCodes = ""
        try {
            val info: PackageInfo = manager.getPackageInfo(instance.packageName, 0)
            versionCodes = info.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return versionCodes
    }

    /**
     * 得到全局唯一UUID,有权限时
     * @param context NameActivity.this
     * @return 返回UUID字符串
     */
    @SuppressLint("MissingPermission")
    fun getUniqueID(context: Context): String? {
        try {
            var perms =
                arrayOf<String>(
                    AppConst.PERMISSONURL.WRITE_EXTERNAL.value,
                    AppConst.PERMISSONURL.READ_EXTERNAL.value,
                    AppConst.PERMISSONURL.READ_PHONE.value
                )
            if (XXPermissions.isGranted(context, perms)) {
                val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                val tmDevice: String
                val tmSerial: String
                val androidId: String
                tmDevice = "" + tm.deviceId
                tmSerial = "" + tm.simSerialNumber
                androidId =
                    "" + Settings.Secure.getString(
                        context.contentResolver,
                        Settings.Secure.ANDROID_ID
                    )
                val deviceUuid = UUID(
                    androidId.hashCode().toLong(),
                    tmDevice.hashCode().toLong() shl 32 or tmSerial.hashCode()
                        .toLong()
                )
                return deviceUuid.toString()
            }
            return ""
        } catch (ex: java.lang.Exception) {
            LZYLog.e("IP Address", ex.toString())
        }
        return ""
    }


    //ipv6
    fun getLocalIpV6(): String {
        try {
            val en = NetworkInterface.getNetworkInterfaces()
            while (en.hasMoreElements()) {
                val intf = en.nextElement()
                val enumIpAddr: Enumeration<InetAddress> = intf.inetAddresses
                while (enumIpAddr.hasMoreElements()) {
                    val inetAddress: InetAddress = enumIpAddr.nextElement()
                    LZYLog.e("ip1  ", inetAddress.getHostAddress())

                    if (!inetAddress.isLoopbackAddress() && inetAddress is Inet6Address) {
                        var hostIp6 = inetAddress.getHostAddress()

                        if (hostIp6 != null && hostIp6.contains("%")) {
                            val split: List<String> = hostIp6.split("%")
                            val s1 = split[0]
//                            LoggerUtil.loggerMsg("v6 remove % is $s1")
                            if (s1 != null && s1.contains(":")) {
                                val split1 = s1.split(":").toTypedArray()
                                if (split1.size == 6 || split1.size == 8) {
                                    if (split1[0].contains("fe") || split1[0].contains("fc")) {
                                        continue
                                    } else {
                                        return s1
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (ex: java.lang.Exception) {
            LZYLog.e("IP Address", ex.toString())
        }
        return ""
    }
    fun isHuaWeiPhone():Boolean {
        val brand = Build.BRAND //手机厂商
        if (TextUtils.equals(brand.lowercase(), "huawei") || TextUtils.equals(brand.lowercase(), "honor")
        ) {
            return true
        }
        return false
    }
    fun isXiaoMiPhone():Boolean {
        val brand = Build.BRAND //手机厂商
        if (TextUtils.equals(brand.lowercase(), "redmi") || TextUtils.equals(brand.lowercase(), "xiaomi")) {
            return true
        }
        return false
    }
    fun isOppoPhone():Boolean {
        val brand = Build.BRAND //手机厂商
        if (TextUtils.equals(brand.lowercase(), "oppo")) {
            return true
        }
        return false
    }
    fun isVivoPhone():Boolean {
        val brand = Build.BRAND //手机厂商
        if (TextUtils.equals(brand.lowercase(), "vivo")) {
            return true
        }
        return false
    }
}