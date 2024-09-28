package com.pet.translator.utils.dj

import android.content.Context
import android.text.TextUtils
import com.pet.translator.bean.dj.CommonConfigBean
import com.google.gson.Gson
import com.pet.translator.APP
import com.pet.translator.AppConst
import com.pet.translator.R
import com.pet.translator.bean.dj.StartRet
import com.pet.translator.bean.dj.WhiteListBean
import com.pet.translator.bean.dj.HelpQuestionBean
import com.pet.translator.bean.dj.InstallBean
import com.pet.translator.bean.dj.OpenMemberBean
import com.pet.translator.bean.dj.ResponseBase
import com.pet.translator.event.dj.ActiveEvent
import com.pet.translator.network.RetrofitFactory
import com.pet.translator.network.XtmHttp
import com.pet.translator.network.XtmObserver
import com.pet.translator.utils.lzy.LZYLog
import io.reactivex.disposables.Disposable
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.greenrobot.eventbus.EventBus
import java.io.IOException


/**
 * Created by xtc on 2019/12/18.
 */
object GetHttpDataUtil {
    var TAG = "GetHttpDataUtil"


    var defaultJson = getDefJsonStr()

    fun getDefJsonStr():String{
        var data = StartRet()
        data.id = "1"
        data.upgrade = "1.0.0"
        data.appId = AppConst.DJ_APP_ID
        data.pkgName = APP.instance.packageName
        data.appName = APP.instance.getString(R.string.app_name)
        data.adSwitch = "0"
        data.adDelay = "0"
        data.adChain = "[{\"adType\":2,\"interval\":10,\"number\":999},{\"adType\":1,\"interval\":30,\"number\":999},{\"adType\":1,\"interval\":100,\"number\":999},{\"adType\":2,\"interval\":120,\"number\":999},{\"adType\":2,\"interval\":180,\"number\":999},{\"adType\":1,\"interval\":180,\"number\":999}]"
        data.clickTrigger = "0"
        data.closeRate = "100"
        data.checkFlag = "0"

        var str = ""
        try {
            val gson = Gson()
            str = gson.toJson(data)
        }catch (e:Exception){
            e.printStackTrace()
        }
        return str
    }

    fun start() {

        var map = HashMap<String, String>()
        map["pkgName"] = APP.instance!!.packageName
        map["version"] = DeviceInfoUtil.packageCode()
        map["appVersion"] = DeviceUtils.getVersionName(APP.instance)
        map["channel"] = AppConst.CHANNEL
        if (!TextUtils.isEmpty(UserInfoModel.getDjid())) {
            map["djId"] = UserInfoModel.getDjid()
        }
        XtmHttp.toSubscribe(
            RetrofitFactory.instance.httpApi?.getAppConfig(
                map
            )!!,
            object : XtmObserver<StartRet>() {
                override fun onNext(t: ResponseBase<StartRet>) {
                    super.onNext(t)
                    if (t.code == 200) {
                        val responseData = t.data
//                        LZYLog.i("Alex", "okhttp成功  -----responseData=${responseData}")
                        if (null != responseData) {
                            val gson = Gson()
                            AppConst.is_show_ad = responseData.adSwitch.equals("1") // 0.OFF：ON、1：ON
                            UserInfoModel.setIsShowAd(responseData.adSwitch.equals("1"))
                            AppConst.is_lockSwitch = responseData.lockSwitch.equals("1") //锁屏信息流开关
                            AppConst.is_adDelay = responseData.adDelay.equals("1") //device mgr
                            val isCurrChannel =
                                if (TextUtils.isEmpty(responseData.currChannel)) "0" else responseData.currChannel
                            AppConst.is_curr_channel=isCurrChannel//当前安装渠道，0：自然量、1：巨量引擎、2：磁力引擎
                            UserInfoModel.setIsCurrChannel(isCurrChannel)
                            if(responseData.riseId!=null) {
                                UserInfoModel.setRiseId(responseData.riseId.toString())
                            }
                            if (responseData.baiduId != null) {
                                AppConst.BAIDU_APP_ID = responseData.baiduId
                                if (!TextUtils.isEmpty(AppConst.BAIDU_APP_ID)) {
                                    SPUtils.getInstance()
                                        .setString(SPUtils.SP_BAIDU_ID, AppConst.BAIDU_APP_ID)
                                }
                            }
                            var attribition = 1
                            if (AppConst.is_curr_channel != "0") {
                                attribition = 0
                            }

                            UserInfoModel.setIsCheckFlag(responseData.checkFlag.equals("1"))
                            UserInfoModel.setBuryingEnable(responseData.buryingEnable.equals("1"))
                            //csj xxl 业务需求
//                            LZYLog.e("tttt","responseData.csjCheckFlag:"+responseData.csjCheckFlag)
                            if (AppConst.CHANNEL == "CSJ" || AppConst.CHANNEL.contains("XXL")) { //判断渠道
                                UserInfoModel.setIsCheckFlag(true)
                                if (responseData.csjCheckFlag =="0") {
                                    AppConst.is_show_ad = true
                                    UserInfoModel.setIsShowAd(true)
                                }
                            }
//                            AppPrefs.putSharedInt(
//                                APP.Companion.instance,
//                                "AP_KEY_ATTRIBUTION",
//                                attribition
//                            )
//                            if (attribition==0){
//                                APP.initDJLib()
//                                TimerCheckRunning.startCountdown(App.application)
//                            }


                            UserInfoModel.setBanStatus(responseData.banStatus)
                            AppConst.is_ban_status = responseData.banStatus == "1"
                            AppConst.is_motivationVideo=responseData.motivationVideo.equals("1") //激励视频

                            val toJson = gson.toJson(responseData)
                            UserInfoModel.setStartHttpRespon(toJson.toString());
//                            LZYLog.i("Alex", "okhttp成功  -----toJson=${toJson}")
                            if (UserInfoModel.getApkInstallationTime() == "") {
                                UserInfoModel.setApkInstallationTime("${System.currentTimeMillis()}")
                            }
                            UserInfoModel.setStorageTime(
                                System.currentTimeMillis().toString()
                            )//存储数据储存时间


                            if(!TextUtils.isEmpty(responseData.isWhiteList)) {
                                UserInfoModel.setIsWhiteListState(responseData.isWhiteList)
                            }

                            if (UserInfoModel.getIsLocalShowAd()){
                                AppConst.is_show_ad=false
                                responseData.adSwitch ="0"
                                responseData.checkFlag="0"
                                responseData.currChannel = "0"
                                UserInfoModel.setIsShowAd(false)
                                UserInfoModel.setIsCheckFlag(true)
                                UserInfoModel.setIsCurrChannel("0")
                            }

                        }
                    }
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    LZYLog.i("Alex", "okhttp成功  -----onError=${e.message}")
                }

                override fun onSubscribe(d: Disposable) {
                    LZYLog.i("Alex", "okhttp成功  -----Disposable=${d.isDisposed}")
                }

                override fun onComplete() {
                }
            })

    }

    fun getStartRet(): StartRet {
        val gson = Gson()
        val httpRespon = UserInfoModel.getStartHttpRespon();
        if (!TextUtils.isEmpty(httpRespon)) {
            return gson.fromJson(httpRespon, StartRet::class.java)
        } else {
            return gson.fromJson(defaultJson, StartRet::class.java)
        }
        return StartRet()
    }

    fun setInstall(activity: Context,source:Int) {
        val startRet = getStartRet()
        val instance = APP.instance
//        LZYLog.i("Alex", "okhttp成功  -----startRet.appId=${startRet.appId}")
//        if (null == startRet.appId) {
//            return
//        }

        var installDatetime = (System.currentTimeMillis() / 1000)
        var appId = startRet.appId.toString()

//        val appList = RunningSystemUtils.getAllPackageInfo(activity)//获取所有应用信息
        if (UserInfoModel.getApkInstallationTime() != "") {
            installDatetime = (UserInfoModel.getApkInstallationTime().toLong()) / 1000
        }
        val simOperator = DeviceInfoUtil.getSimOperator(activity)//运营商判断
//        val openUdid = if (TextUtils.isEmpty(DeviceInfoUtil.getUniqueID(activity))) "" else DeviceInfoUtil.getUniqueID(activity)!!//运营商判断
        var networkAccess = 0;//	入网类型：0：WIFI、1：1G、2：2G、3：3G、4：4G、5：5G
        val networkType = NetworkUtils.getNetworkType(activity);
//        val umengZID = if (TextUtils.isEmpty(UMConfigure.getUmengZID(activity))) "" else UMConfigure.getUmengZID(activity)
        val isBatteryCharging = BatteryUtils.isBatteryCharging(activity)
        when (networkType) {
            "WIFI" -> {
                networkAccess = 0;
            }
            "2G" -> {
                networkAccess = 2;
            }
            "3G" -> {
                networkAccess = 3;
            }
            "4G" -> {
                networkAccess = 4;
            }
            "5G" -> {
                networkAccess = 5;
            }
        }
        var map = HashMap<String, Any>()
        map["androidId"] = AppConst.AndroidId
        map["appId"] = appId
        map["appName"] = activity.getString(R.string.app_name)
        map["appVersion"] = DeviceUtils.getVersionName(instance)
        map["pkgName"] = instance!!.packageName
        map["carrier"] = simOperator//设备运营商，0：移动、1：联通、2：电信
        map["uaWebView"] =  AppConst.GetWebViewUserAgent
        map["uaApp"] = DeviceInfoUtil.getAppUserAgent()//ua
        map["imei"] = DeviceInfoUtil.getImei(instance)
        map["meid"] = DeviceInfoUtil.getPhoneMEID(instance)
        LZYLog.d("LHM", "setInstall: ")
        map["deviceId"] = DeviceInfoUtil.getDeviceId(instance)
//        map["city"]=city//城市
//        map["country"]=country//国家
//        map["province"]=province//省
        map["deviceApps"] = ""//设备安装应用包名列表
//        map["deviceApps"]=gson.toJson(appList).toString()//设备安装应用包名列表
        map["deviceBrand"] = DeviceUtils.getBrand()//设备品牌
        map["deviceIp"] = AppConst.DEVICE_OUT_NET_ID//设备IP
        map["deviceIp6"] = DeviceInfoUtil.getLocalIpV6()
        map["deviceLevel"] = 1//设备等级
        map["deviceModel"] = DeviceUtils.getSystemModel()//	设备型号
        //map["deviceStatus"] = if (TextUtils.isEmpty(riskInfo)) 0 else 1//设备状态，0：正常、1：异常
        map["deviceTag"] = AppConst.riskInfo//设备异常标签，正常、代理、异常、模拟器、root、无SIM
        map["idfa"] = ""//IOS广告标示符
        map["idfv"] = "1"//Vendor标识符
//        map["imei"]=DeviceInfoUtil.getImei(activity)//国际移动设备识别码
        map["installDatetime"] = installDatetime//首次安装时间，时间戳（秒）
        map["mac"] = DeviceInfoUtil.getMacFromHardware(activity)//MAC地址
        map["networkAccess"] = networkAccess//入网类型：0：WIFI、1：4G、2：5G

        val oaid = UserInfoModel.getOaid()
        val oaidU = UserInfoModel.getOaidU()
        val oaidH = ""
        map["oaId"] = UserInfoModel.getOaid()
        map["oaIdU"] = UserInfoModel.getOaidU()
        map["oaIdH"] = ""
        LZYLog.e("LoggingInterceptor","oaId:${oaid},oaIdU:${oaidU},oaIdH:${oaidH}")


//        map["openUdid"]= openUdid!!//	Open UDID



        val systemCode = if (DeviceUtils.isHarmonyOs()) 2 else 0
        map["os"] = systemCode //操作系统, 0：Android、1：iOS 2.鸿蒙
        if(systemCode == 2){
            map["idfv"] = DeviceUtils.getSystemVersion()//	操作系统版本
            map["osVersion"] = DeviceUtils.getHarmonyVersion()//	鸿蒙 版本
        }else{
            map["osVersion"] = DeviceUtils.getSystemVersion()//	操作系统版本
        }


        map["resolution"] =
            "${DisplayUtil.getWindowWidth(activity)}*${DisplayUtil.getWindowHeight(activity)}"//设备分辨率
//        map["resolution"]="800*500"//设备分辨率
        map["sdkVersion"] = android.os.Build.VERSION.SDK_INT//SDK版本
        map["downloadChannel"] = AppConst.CHANNEL
        map["charge"] =if (isBatteryCharging ) 1 else 0 //0:未充电、1:充电
//        map["umengAppkey"] = PushConstants.APP_KEY
//        map["umengAtoken"] = umengZID
//        LZYLog.i("Alex", "umengZID=${umengZID}")
        XtmHttp.toSubscribe(
            RetrofitFactory.instance.httpApi?.setInstallHttp(getRequestBody(map))!!,
            object : XtmObserver<StartRet>() {
                override fun onNext(t: ResponseBase<StartRet>) {
                    super.onNext(t)

                    if (t.code == 200) {
                        val responseData = t.data
//                        LZYLog.i("tttt", "okhttp成功  --install---responseData=${responseData}")
                        if (null != responseData) {
                            val gson = Gson()
                            AppConst.is_show_ad = responseData.adSwitch.equals("1") // 0:OFF   1：ON
                            val isCurrChannel =
                                if (TextUtils.isEmpty(responseData.currChannel)) "0" else responseData.currChannel
                            AppConst.is_curr_channel=isCurrChannel//当前安装渠道，0：自然量、1：巨量引擎、2：磁力引擎
                            UserInfoModel.setIsCurrChannel(isCurrChannel)
                            UserInfoModel.setIsShowAd(responseData.adSwitch.equals("1"))
                            val toJson = gson.toJson(responseData)
                            UserInfoModel.setStartHttpRespon(toJson.toString());
//                            LZYLog.i("Alex", "okhttp成功  -----toJson=${toJson}")
                            AppConst.is_ban_status = responseData.banStatus == "1"
                            UserInfoModel.setBanStatus(responseData.banStatus)
                            AppConst.is_adDelay = responseData.adDelay.equals("1") //device mgr
                            AppConst.is_motivationVideo=responseData.motivationVideo.equals("1") //激励视频
                            if (responseData.baiduId != null) {
                                AppConst.BAIDU_APP_ID = responseData.baiduId
                                if (!TextUtils.isEmpty(AppConst.BAIDU_APP_ID)) {
                                    SPUtils.getInstance()
                                        .setString(SPUtils.SP_BAIDU_ID, AppConst.BAIDU_APP_ID)
                                }
                            }
                            if(responseData.riseId!=null) {
                                UserInfoModel.setRiseId(responseData.riseId.toString())
                            }
                            var attribition = 1 //自然量
                            if (AppConst.is_curr_channel != "0") {
                                attribition = 0

                            }
                            UserInfoModel.setIsCheckFlag(responseData.checkFlag.equals("1"))
                            UserInfoModel.setBuryingEnable(responseData.buryingEnable.equals("1"))
                            //csj xxl 业务需求
//                            LZYLog.e("tttt","responseData.csjCheckFlag:"+responseData.csjCheckFlag)
                            if (AppConst.CHANNEL == "CSJ" || AppConst.CHANNEL.contains("XXL")) { //判断渠道
                                UserInfoModel.setIsCheckFlag(true)
                                if (responseData.csjCheckFlag =="0") {
                                    AppConst.is_show_ad = true
                                    UserInfoModel.setIsShowAd(true)
                                }
                            }
//                            try {
//                                AppPrefs.putSharedInt(
//                                    App.application,
//                                    "AP_KEY_ATTRIBUTION",
//                                    attribition
//                                )
//                            }catch (e:Exception){
//                                e.printStackTrace()
//                            }
                            LZYLog.e(
                                "ApplicationDaemon",
                                "app isAttribution = $attribition"
                            )
//                            if (attribition==0){
//                                App.initDJLib()
//                                TimerCheckRunning.startCountdown(App.application)
//                            }

                            AppConst.is_install=true
                            AppConst.is_lockSwitch = responseData.lockSwitch.equals("1") //锁屏信息流开关


                            UserInfoModel.setStorageTime(
                                System.currentTimeMillis().toString()
                            )//存储数据储存时间
                            UserInfoModel.setDjid(responseData.djId)

                            if(!TextUtils.isEmpty(responseData.isWhiteList)) {
                                UserInfoModel.setIsWhiteListState(responseData.isWhiteList)
                            }

                            if (UserInfoModel.getIsLocalShowAd()){
                                AppConst.is_show_ad=false
                                responseData.adSwitch ="0"
                                responseData.checkFlag="0"
                                responseData.currChannel = "0"
                                UserInfoModel.setIsShowAd(false)
                                UserInfoModel.setIsCheckFlag(true)
                                UserInfoModel.setIsCurrChannel("0")
                            }
                        }
                    }
//                    RxBus.getSubject().onNext(ActiveBean(true,source))
                    EventBus.getDefault().post(ActiveEvent(true,source))
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
//                    RxBus.getSubject().onNext(ActiveBean(false,source))
                    EventBus.getDefault().post(ActiveEvent(false,source))
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onComplete() {
                }
            })
    }

    private fun getRequestBody(map: Map<String, Any>): RequestBody {
        return Gson().toJson(map).toString().toRequestBody("application/json".toMediaTypeOrNull())
    }

    //获取外网地址ip
    fun getOutNetIP() {
        val httpUrl: HttpUrl = HttpUrl.Builder().scheme("https").host("ipv4.gdt.qq.com")
            .addPathSegments("get_client_ip")
            .build()
        val request: Request = Request.Builder().url(httpUrl).get().build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                LZYLog.i("Alex", "OutNetIP--okhttp失败" + e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                val responseString = response.body?.string()
                LZYLog.i("Alex", "OutNetIP--okhttp成功 responseString:${responseString}")
                if (response.code == 200) {
                    if (responseString != null) {
                        AppConst.DEVICE_OUT_NET_ID = responseString.trim()
                        getIpLocation(AppConst.DEVICE_OUT_NET_ID, 2)
                    }
                }
            }
        })
    }

    //获取外网地址ip   https://whois.pconline.com.cn/ip.jsp?ip=113.218.209.85&json=true
    fun getIpLocation(ipStr: String, type: Int) {
//        val httpUrl: HttpUrl = HttpUrl.Builder().scheme("https").host("whois.pconline.com.cn/ip.jsp?ip=113.218.209.85&json=true")
//            .build()
        val request: Request =
            Request.Builder().url("https://whois.pconline.com.cn/ip.jsp?ip=$ipStr&json=true").get()
                .build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                LZYLog.i("Alex", "OutNetIP--okhttp失败" + e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                val responseString = response.body?.string()
                LZYLog.i("Alex", "OutNetIP--okhttp成功 responseString:${responseString}")
                if (response.code == 200) {
                    if (responseString != null) {
                        AppConst.DEVICE_OUT_NET_LOCATION = responseString.trim()
                    }
                }
            }
        })
    }

    fun reportAdReport(
        actionType: Int,
        adPlatformType: String,
        adId: String,
        placementId:String,
        adType: String,
        preEcpm: String,
        scene: String,
        adChainId: String = "0",
        sceneDetail: String = "0"
    ) {
        var adPlatform = ""
        var adStyle = 0;
        val startRet = getStartRet()
        val instance = APP.instance
        if (null == startRet.appId) {
            return
        }
        var appId = startRet.appId.toString()
        when (adType) { //广告样式, 0:信息流、1:全屏、2:插屏
            AppConst.CHAPING -> {
                adStyle = 2
            }
            AppConst.QUANPING -> {
                adStyle = 1
            }
            AppConst.XINGXINLIU -> {
                adStyle = 0
            }
            AppConst.JILIVOID -> {
                adStyle = 3
            }
            AppConst.KAIPING -> {
                adStyle = 4
            }
        }
        when (adPlatformType) {
            "ks" -> {
                adPlatform = "ks"
            }
            "pangle" -> {
                adPlatform = "csj"
            }
            "gdt" -> {
                adPlatform = "ylh"
            }
            "baidu" -> {
                adPlatform = "bqt"
            }
        }
        var ecpmVal = 0.0
        try {
            if (!TextUtils.isEmpty(preEcpm)) {
                ecpmVal = preEcpm.toDouble();
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        var map = HashMap<String, Any>()

        map["actionType"] = actionType //	行为类型, 0:展示、1:点击 2请求
        map["adId"] = adId//	广告位id
        map["placementId"] = placementId//	广告位id
        map["adPlatForm"] = adPlatform//广告平台
        map["adStyle"] = adStyle //广告样式, 0:信息流、1:全屏、2:插屏
        map["appId"] = appId
        map["appName"] = instance!!.getString(R.string.app_name)
        if (!TextUtils.isEmpty(UserInfoModel.getDjid())) {
            map["djId"] = UserInfoModel.getDjid()
        } else {
            return
        }
        map["ecpm"] = ecpmVal
        map["oaId"] = AppConst.oaid//匿名设备标识符
        map["pkgName"] = instance.packageName
        map["revenue"] = 0
        map["deviceIp"] = AppConst.DEVICE_OUT_NET_ID//设备IP
        map["adChainId"] = adChainId//广告组链ID
        map["sceneDetail"] = sceneDetail//广告组链ID
        map["scene"] = scene// 应用内=iapp ; 应用外=oapp
        map["appVersion"] = DeviceUtils.getVersionName(APP.instance)

        if(actionType == 0) {
            if(adStyle == 0){
                LZYLog.e("tttt", "report 信息流展示${placementId}："+Gson().toJson(map))
            }else if(adStyle == 1){
                LZYLog.e("tttt", "report 全屏展示${placementId}："+Gson().toJson(map))

            }else if(adStyle == 2){
                LZYLog.e("tttt", "report 插屏展示${placementId}："+Gson().toJson(map))
            }else if(adStyle == 3){
                LZYLog.e("tttt", "report 激励展示${placementId}："+Gson().toJson(map))
            }else if(adStyle == 4){
                LZYLog.e("tttt", "report 开屏展示${placementId}："+Gson().toJson(map))
            }
        }else if(actionType == 2){
            if(adStyle == 0){
                LZYLog.e("tttt", "report 信息流请求${placementId}："+Gson().toJson(map))
            }else if(adStyle == 1){
                LZYLog.e("tttt", "report 全屏请求${placementId}："+Gson().toJson(map))
            }else if(adStyle == 2){
                LZYLog.e("tttt", "report 插屏请求${placementId}："+Gson().toJson(map))
            }else if(adStyle == 3){
                LZYLog.e("tttt", "report 激励请求${placementId}："+Gson().toJson(map))
            }else if(adStyle == 4){
                LZYLog.e("tttt", "report 开屏请求${placementId}："+Gson().toJson(map))
            }

        }

        XtmHttp.toSubscribe(
            RetrofitFactory.instance.httpApi?.reportingBehavior(getRequestBody(map))!!,
            object : XtmObserver<InstallBean>() {
                override fun onNext(t: ResponseBase<InstallBean>) {
                    super.onNext(t)
//                    if(actionType == 0) {
//                        LZYLog.e("tttt", "report返回参数${placementId}------------："+Gson().toJson(t))
//                    }else if(actionType == 2 ) {
//                        LZYLog.e("tttt", "report返回参数${placementId}-------------："+Gson().toJson(t))
//                    }
                    if (t.code == 200) {

                    }
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onComplete() {

                }
            })
    }

    fun setWhiteListHttp(password:String) {
        val startRet = getStartRet()
        if (null == startRet.appId) {
            return
        }
        var map = HashMap<String, String>()
        map["password"] = password
        map["djId"] = UserInfoModel.getDjid()
        map["appVersion"] = DeviceUtils.getVersionName(APP.instance)
        XtmHttp.toSubscribe(
            RetrofitFactory.instance.httpApi?.setWhiteList(getRequestBody(map))!!,
            object : XtmObserver<WhiteListBean>() {
                override fun onNext(t: ResponseBase<WhiteListBean>) {
                    super.onNext(t)
                    if (t.code == 200) {
                        if (t.data.status == "0") {
                            com.blankj.utilcode.util.ToastUtils.showLong("加入白名单成功！")
                            UserInfoModel.setIsWhiteList(true)
                        } else {
                            com.blankj.utilcode.util.ToastUtils.showLong("取消白名单成功！")
                            UserInfoModel.setIsWhiteList(false)
                        }
                        start()
                    }else{
                        if(t != null) {
                            com.blankj.utilcode.util.ToastUtils.showLong(t.msg)
                        }
                    }
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onComplete() {

                }
            })
    }

    /**
     * 获取Vip通用配置
     */
    fun getCommonConfig(mListener: OnSuccessAndFaultListener) {
        var map = HashMap<String, String>()
        XtmHttp.toSubscribe(
            RetrofitFactory.instance.httpApi?.getCommonConfig(map)!!,
            object : XtmObserver<ArrayList<CommonConfigBean>>() {
                override fun onNext(t: ResponseBase<ArrayList<CommonConfigBean>>) {
                    super.onNext(t)
                    val gson  = Gson()
                    if (t.code == 200) {
                        mListener.onSuccess(t.data)
                    }
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    mListener.onFault()
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onComplete() {

                }
            })
    }

    /**
     * 获取客服问题标签列表
     */
    fun getIssuesList(mListener: OnSuccessAndFaultListener) {
        var map = HashMap<String, String>()
        XtmHttp.toSubscribe(
            RetrofitFactory.instance.httpApi?.issuesList(map)!!,
            object : XtmObserver<ArrayList<HelpQuestionBean>>() {
                override fun onNext(t: ResponseBase<ArrayList<HelpQuestionBean>>) {
                    super.onNext(t)
                    val gson  = Gson()
                    if (t.code == 200) {
                        mListener.onSuccess(t.data)
                    }
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    mListener.onFault()
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onComplete() {

                }
            })
    }

    /**
     * 售后服务表单
     */
    fun uploadAfterSalesForm(contact:String,content:String,type:String,mListener: OnSuccessAndFaultListener) {
        val startRet = getStartRet()
        LZYLog.i("Alex", "okhttp成功  -----startRet.appId=${startRet.appId}")
        if (null == startRet.appId) {
            return
        }
        var appId = startRet.appId.toString()
        var map = HashMap<String, Any>()
        map["appId"] = appId
        map["djId"] = UserInfoModel.getDjid()
        map["issuesId"] = type
        map["content"] = content
        map["contact"] = contact
//        LZYLog.e("tttt","上传数据："+Gson().toJson(map))
        XtmHttp.toSubscribe(
            RetrofitFactory.instance.httpApi?.afterSalesForm(getRequestBody(map))!!,
            object : XtmObserver<OpenMemberBean>() {
                override fun onNext(t: ResponseBase<OpenMemberBean>) {
                    super.onNext(t)

                    val gson  = Gson()
//                    LZYLog.e("tttt","上传问题："+gson.toJson(t))
                    if (t.code == 200) {
                        mListener.onSuccess(t)
                    }
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onComplete() {

                }
            })
    }

    /**
     * 设备扩展信息
     */
    fun deviceInfoExtend(registrationId:String="",deviceApps:Boolean=false,deviceAppsStr:String="") {
        val startRet = getStartRet()
//        LZYLog.i("Alex", "registrationId=${registrationId}")
//        LZYLog.i("Alex", "deviceApps=${deviceAppsStr}")
        if (null == startRet.appId) {
            return
        }

        var appId = startRet.appId.toString()
        var map = HashMap<String, Any>()
        map["appId"] = appId
        map["djId"] = UserInfoModel.getDjid()
//        LZYLog.e("tttt","deviceAppsStr:"+deviceAppsStr)
        if (deviceApps) {
            map["deviceApps"] = deviceAppsStr
        }
        if (!TextUtils.isEmpty(registrationId)) {
            map["registrationId"] = registrationId
        }
        XtmHttp.toSubscribe(
            RetrofitFactory.instance.httpApi?.deviceInfoExtend(getRequestBody(map))!!,
            object : XtmObserver<OpenMemberBean>() {
                override fun onNext(t: ResponseBase<OpenMemberBean>) {
                    super.onNext(t)
//                    LZYLog.e("tttt","上传应用列表返回："+Gson().toJson(t))
                    if (t.code == 200) {

                    }
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onComplete() {

                }
            })
    }

    interface OnSuccessAndFaultListener {

        fun onSuccess(t: Any)

        fun onFault()
    }

    interface OnSuccessAndFailedListener{
        fun onSuccess(t:Any)
        fun onFailed(t:Any)
    }
}