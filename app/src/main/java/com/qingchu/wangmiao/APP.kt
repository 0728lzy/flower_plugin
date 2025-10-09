package com.qingchu.wangmiao

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.webkit.WebView
import androidx.appcompat.app.AppCompatDelegate
import com.baidu.mobads.sdk.api.MobadsPermissionSettings
import com.drake.net.NetConfig
import com.drake.net.interceptor.LogRecordInterceptor
import com.drake.net.interceptor.RequestInterceptor
import com.drake.net.okhttp.setConverter
import com.drake.net.okhttp.setDebug
import com.drake.net.okhttp.setRequestInterceptor
import com.drake.net.request.BaseRequest
import com.hjq.toast.ToastUtils
import com.kongzue.dialogx.DialogX
import com.qingchu.wangmiao.csj.lzy.LZYCPCounterHelper
import com.qingchu.wangmiao.csj.AdCPNoLimitUtils
import com.qingchu.wangmiao.csj.AdCPTwoUtils
import com.qingchu.wangmiao.csj.AdManagerHolder
import com.qingchu.wangmiao.csj.lzy.EventCounterHelper
import com.qingchu.wangmiao.db.RoomHelper
import com.qingchu.wangmiao.helper.dj.PushHelper
import com.qingchu.wangmiao.net.GsonConverter
import com.qingchu.wangmiao.ui.activity.QCLauncherActivity
import com.qingchu.wangmiao.utils.dj.CountdownTimeTask
import com.qingchu.wangmiao.utils.dj.GetHttpDataUtil
import com.qingchu.wangmiao.utils.dj.SPUtils
import com.qingchu.wangmiao.utils.dj.TimeUtil
import com.qingchu.wangmiao.utils.dj.UserInfoModel
import com.qingchu.wangmiao.utils.lzy.LZYLog
import com.umeng.commonsdk.UMConfigure
import com.umeng.commonsdk.utils.UMUtils
import com.qingchu.wangmiao.utils.dj.AdDynamicUtils
import com.qingchu.wangmiao.utils.lzy.LZYADSUtils
import com.yl.adsdk.YlLib
import me.jessyan.autosize.AutoSize
import me.jessyan.autosize.AutoSizeConfig
import me.jessyan.autosize.onAdaptListener
import me.jessyan.autosize.utils.AutoSizeLog
import org.litepal.LitePal
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.util.Locale
import java.util.TimerTask
import java.util.concurrent.TimeUnit


class APP : Application() {

    companion object {
        lateinit var instance: APP

        fun initAdSdk(){
            if(!UserInfoModel.getIsWhiteListState().equals("2")) {
                if (!UserInfoModel.getIsCheckFlag() || UserInfoModel.getIsShowAd()) {
                    AdManagerHolder.init(instance)
                    MobadsPermissionSettings.setPermissionReadDeviceID(true)
                }
            }
        }
        fun initCp(activity: Activity){
            if (!AdCPNoLimitUtils.isReady()) {
                AdCPNoLimitUtils.init(activity, object : AdCPNoLimitUtils.GirdMenuStateListener {
                    override fun onShowError() {

                    }

                    override fun showVideoClosed() {

                    }

                    override fun onError() {

                    }

                    override fun onSuccess() {

                    }
                }) //初始化插全屏广告
                AdCPNoLimitUtils.initPreloading()
            }
            if(AppConst.is_show_ad && !AppConst.isWaked){
                if(!AdCPTwoUtils.isReady()) {
                    AdCPTwoUtils.init(
                        activity,
                        object : AdCPTwoUtils.GirdMenuStateListener {
                            override fun onSuccess() {

                            }

                            override fun onError() {
                            }

                            override fun showVideoClosed() {
                            }

                            override fun onShowError() {
                            }
                        })
                    Handler().postDelayed({
                        AdCPTwoUtils.initPreloading()
                    },1000)
                }
            }
            if(UserInfoModel.getIsFirstVip()){
                LZYADSUtils("APP", activity).initSimpleAd3(activity)
            }
        }
    }

    var TAG = "Application"
    var appount = 0
    private var isBackground = false
    var isStarted = false

    override fun onCreate() {
        super.onCreate()
        instance = this
        RoomHelper.init(this, "app.db", 1)
        NetConfig.initialize("", this) {
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
            setDebug(com.qingchu.wangmiao.BuildConfig.DEBUG)
            setConverter(GsonConverter())
            setRequestInterceptor(object : RequestInterceptor {
                override fun interceptor(request: BaseRequest) {
                    request.addHeader(
                        "token",
                        "CeQhjW7RLybrzzt01ZDUWOm8IEHkiTwSiN+aawlGalgXgDL/2x2BUFeGQ6p2z1Tf7d9CGdKc9FVkOmKCg/N48hyakpIRseFZ2WDXnskM7MRvHynXcYAtA2n2Mi1cFCIBIMWuYmpeG/80LB9l0xxsUQ=="
                    )
                    request.addHeader("appclient", "100005")
                    request.addHeader("model", "")
                    request.addHeader("deviceid", "")
                    request.addHeader("channel", "003")
                    request.addHeader("realchannel", "002")
                    request.addHeader("version", "1.0.0")

                }
            })
            addInterceptor(LogRecordInterceptor(com.qingchu.wangmiao.BuildConfig.DEBUG))
        }
        DialogX.init(this)
        DialogX.globalTheme = DialogX.THEME.DARK
        ToastUtils.init(this)
        LZYLog.setLogEnabled(false)
        var currProcessName = getAppProcessName()
        if (currProcessName == this.packageName) {
            var installTime = SPUtils.getInstance().getLong(SPUtils.SP_INSTALL_TIME)
            YlLib.init(this)
            if (installTime == 0L) {
                SPUtils.getInstance().setLong(SPUtils.SP_INSTALL_TIME, System.currentTimeMillis())
            }
            AppConst.BAIDU_APP_ID = SPUtils.getInstance().getString(SPUtils.SP_BAIDU_ID)
            initAutoSize()
            EventCounterHelper.init(this)
            AdDynamicUtils.getAdInto()
            LZYCPCounterHelper.init(this)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            //Android 9及以上必须设置 多进程WebView兼容
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                WebView.setDataDirectorySuffix(currProcessName!!)
            }
            hideWarningShow()
            LitePal.initialize(this)
            Log.d("LHM_APP", "add addAccount")

            if (!UserInfoModel.getIsFirstTime() ) {
                initUmeng()
                setTimeCountdown()
                GetHttpDataUtil.getOutNetIP()
                if (!TextUtils.isEmpty(UserInfoModel.getDjid())){
                    GetHttpDataUtil.start()
                }
                //MSDK的初始化需要放在Application中进行
                if (!UserInfoModel.getIsCheckFlag() || UserInfoModel.getIsShowAd()) {
                    if(!UserInfoModel.getIsWhiteListState().equals("2")) {
                        AdManagerHolder.init(this)
                        MobadsPermissionSettings.setPermissionReadDeviceID(true)
                    }
                }
            }


            //安装时间只存一次
            if (UserInfoModel.getApkInstallationTime() == "") {
                UserInfoModel.setApkInstallationTime("${System.currentTimeMillis()}")
            }
            AppConst.isPowerUninstalled =
                SPUtils.getInstance().getBoolean(SPUtils.SP_UNINSTALL, false)
            intLifecycleCallbacks()
            if (UserInfoModel.getToDatTime() == 0L) {
                UserInfoModel.setToDatTime(System.currentTimeMillis())
            }

        }
    }

    //开始倒计时 半小时掉一次接口
    private fun setTimeCountdown() {
        val ountdownTimeTask = CountdownTimeTask(1000 * 1800L, object : TimerTask() {
            override fun run() {
                Log.e("LHM", "CountdownTimeTask调用了")
                if (!UserInfoModel.getIsFirstTime()) {
                    GetHttpDataUtil.start()//
                    if (!TimeUtil.IsToday(UserInfoModel.getToDatTime())) {
                        UserInfoModel.setToDatTime(System.currentTimeMillis())
                        UserInfoModel.setIsToDayAdShowTotal(0L)
                    }
                }

            }
        })
        ountdownTimeTask.start()
    }

    fun getAppProcessName(): String? {
        try {
            val localFile = File("/proc/self/cmdline")
            val localFileReader = FileReader(localFile)
            val localObject = BufferedReader(localFileReader)
            return localObject.readLine().trim { it <= ' ' }
        } catch (localException: Exception) {
            localException.printStackTrace()
        }
        return ""
    }
    @SuppressLint("SoonBlockedPrivateApi")
    private fun hideWarningShow() {
        try {
            val aClass = Class.forName("android.content.pm.PackageParser\$Package")
            val declaredConstructor = aClass.getDeclaredConstructor(
                String::class.java
            )
            declaredConstructor.setAccessible(true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            val cls = Class.forName("android.app.ActivityThread")
            val declaredMethod = cls.getDeclaredMethod("currentActivityThread")
            declaredMethod.isAccessible = true
            val activityThread = declaredMethod.invoke(null)
            val mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown")
            mHiddenApiWarningShown.isAccessible = true
            mHiddenApiWarningShown.setBoolean(activityThread, true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    //开始倒计时 1小时掉一次接口
//    private fun setTimeCountdown() {
//        val ountdownTimeTask = CountdownTimeTask(1000 * 3600, object : TimerTask() {
//            override fun run() {
//                Log.e("LHM", "CountdownTimeTask调用了")
//                if (!UserInfoModel.getIsFirstTime()) {
//                    if (AppConst.isFront) {
//                        GetHttpDataUtil.start()//
//                    }
//                    if (!TimeUtil.IsToday(UserInfoModel.getToDatTime())) {
//                        UserInfoModel.setToDatTime(System.currentTimeMillis())
//                        UserInfoModel.setIsToDayAdShowTotal(0L)
//                    }
//                }
//
//            }
//        })
//        ountdownTimeTask.start()
//    }

    fun intLifecycleCallbacks() {
        //注册activitys的生命周期的回调,对activity的生命周期进行管理
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                Log.e(TAG, "onActivityCreated: ")
            }

            override fun onActivityStarted(activity: Activity) {
                if (AppConst.is_show_ad||activity is QCLauncherActivity) {
                    Log.e(TAG, "onActivityStarted: ")
                    appount++
                    if (appount == 1 && !isBackground) {
                        Log.e(TAG, "进入前台------------- startRet")
                        isBackground = true
                        Log.e(TAG, "AppConst.isFront:${AppConst.isFront},isStarted:${isStarted}")
                        if (!AppConst.isFront && isStarted &&AppConst.is_show_ad) {
                            AppConst.isStopped = true
                            val intent = Intent(this@APP, QCLauncherActivity::class.java)
                            intent.putExtra("position", 1)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
//                        AppConst.splashBackgroundReturn = 1
                            Log.e(TAG, "跳转开屏页")

                        }

                        AppConst.isFront = true
                    }
                }
            }

            override fun onActivityResumed(activity: Activity) {
                Log.e(TAG, "onActivityResumed: ")
            }

            override fun onActivityPaused(activity: Activity) {
                Log.e(TAG, "onActivityPaused: ")
            }

            override fun onActivityStopped(activity: Activity) {
                if (AppConst.is_show_ad||activity is QCLauncherActivity) {
                    Log.e(TAG, "onActivityStopped: ")
                    appount--
                    if (appount === 0 && AppConst.isStopBoolen && AppConst.isSuspendedBoolen) {
                        Log.e(TAG, "切入后台------------- startRet")
//                    if (!AppConst.isPowerUninstalled) {
//                        AliasUtils.setAlias(activity)
//                    }
                    }
                    if (appount == 0 && isBackground) {
                        Log.e(TAG, "切入后台------------- startRet")
                        AppConst.isFront = false
                        isBackground = false
                        Log.e(TAG, "isBackground:${isBackground},isStarted:${isStarted}")
                        android.os.Handler().postDelayed({
                            if (!isBackground) {
                                isStarted = true
                                Log.e(TAG, "4秒一到 isStarted:${isStarted}")
                            }
                        }, 1000)
                    }
                }
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                Log.e(TAG, "onActivitySaveInstanceState:")
            }

            override fun onActivityDestroyed(activity: Activity) {
                Log.e(TAG, "onActivityDestroyed: ")
            }
        })
    }


    //友盟初始化
    private fun initUmeng() {
        UMConfigure.setLogEnabled(true)
        if (UserInfoModel.getIsFirstTime()) {
            //预初始化
            PushHelper.preInit(this)
        } else {
            if (!UserInfoModel.getIsCheckFlag()  || UserInfoModel.getIsShowAd()) {
                val isMainProcess = UMUtils.isMainProgress(this)
                if (isMainProcess) {
                    //启动优化：建议在子线程中执行初始化
                    Thread { PushHelper.init(applicationContext) }.start()
                } else {
                    //若不是主进程（":channel"结尾的进程），直接初始化sdk，不可在子线程中执行
                    PushHelper.init(applicationContext)
                }
            }
        }
    }

    private fun initAutoSize(){
        //当 App 中出现多进程, 并且您需要适配所有的进程, 就需要在 App 初始化时调用 initCompatMultiProcess()
        AutoSize.initCompatMultiProcess(this);

        //如果在某些特殊情况下出现 InitProvider 未能正常实例化, 导致 AndroidAutoSize 未能完成初始化
        //可以主动调用 AutoSize.checkAndInit(this) 方法, 完成 AndroidAutoSize 的初始化后即可正常使用
//        AutoSize.checkAndInit(this);

//        如何控制 AndroidAutoSize 的初始化，让 AndroidAutoSize 在某些设备上不自动启动？https://github.com/JessYanCoding/AndroidAutoSize/issues/249

        /**
         * 以下是 AndroidAutoSize 可以自定义的参数, {@link AutoSizeConfig} 的每个方法的注释都写的很详细
         * 使用前请一定记得跳进源码，查看方法的注释, 下面的注释只是简单描述!!!
         */
        AutoSizeConfig.getInstance()

            //是否让框架支持自定义 Fragment 的适配参数, 由于这个需求是比较少见的, 所以须要使用者手动开启
            //如果没有这个需求建议不开启
            .setCustomFragment(true)

            //是否屏蔽系统字体大小对 AndroidAutoSize 的影响, 如果为 true, App 内的字体的大小将不会跟随系统设置中字体大小的改变
            //如果为 false, 则会跟随系统设置中字体大小的改变, 默认为 false
//                .setExcludeFontScale(true)

            //区别于系统字体大小的放大比例, AndroidAutoSize 允许 APP 内部可以独立于系统字体大小之外，独自拥有全局调节 APP 字体大小的能力
            //当然, 在 APP 内您必须使用 sp 来作为字体的单位, 否则此功能无效, 不设置或将此值设为 0 则取消此功能
//                .setPrivateFontScale(0.8f)

            //屏幕适配监听器
            .setOnAdaptListener(object : onAdaptListener {


                override fun onAdaptBefore(target: Any?, activity: Activity?) {
                    //使用以下代码, 可以解决横竖屏切换时的屏幕适配问题
                    //使用以下代码, 可支持 Android 的分屏或缩放模式, 但前提是在分屏或缩放模式下当用户改变您 App 的窗口大小时
                    //系统会重绘当前的页面, 经测试在某些机型, 某些情况下系统不会重绘当前页面, ScreenUtils.getScreenSize(activity) 的参数一定要不要传 Application!!!
//                        AutoSizeConfig.getInstance().setScreenWidth(ScreenUtils.getScreenSize(activity)[0]);
//                        AutoSizeConfig.getInstance().setScreenHeight(ScreenUtils.getScreenSize(activity)[1]);
                    AutoSizeLog.d(String.format(Locale.ENGLISH, "%s onAdaptBefore!", target?.javaClass?.getName()));

                }

                override fun onAdaptAfter(target: Any?, activity: Activity?) {
                    AutoSizeLog.d(String.format(Locale.ENGLISH, "%s onAdaptAfter!", target?.javaClass?.getName()));

                }
            })

        //是否打印 AutoSize 的内部日志, 默认为 true, 如果您不想 AutoSize 打印日志, 则请设置为 false
//                .setLog(false)

        //是否使用设备的实际尺寸做适配, 默认为 false, 如果设置为 false, 在以屏幕高度为基准进行适配时
        //AutoSize 会将屏幕总高度减去状态栏高度来做适配
        //设置为 true 则使用设备的实际屏幕高度, 不会减去状态栏高度
        //在全面屏或刘海屏幕设备中, 获取到的屏幕高度可能不包含状态栏高度, 所以在全面屏设备中不需要减去状态栏高度，所以可以 setUseDeviceSize(true)
//                .setUseDeviceSize(true)

        //是否全局按照宽度进行等比例适配, 默认为 true, 如果设置为 false, AutoSize 会全局按照高度进行适配
//                .setBaseOnWidth(false)

        //设置屏幕适配逻辑策略类, 一般不用设置, 使用框架默认的就好
//                .setAutoAdaptStrategy(new AutoAdaptStrategy())
    }
}