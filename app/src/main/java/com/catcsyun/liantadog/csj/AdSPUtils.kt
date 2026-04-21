package com.catcsyun.liantadog.csj

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Handler
import android.util.Log
import android.widget.FrameLayout
import com.bytedance.sdk.openadsdk.*
import com.bytedance.sdk.openadsdk.mediation.MediationConstant
import com.bytedance.sdk.openadsdk.mediation.ad.MediationAdSlot
import com.bytedance.sdk.openadsdk.mediation.ad.MediationSplashRequestInfo
import com.catcsyun.liantadog.AppConst
import com.catcsyun.liantadog.utils.dj.DisplayUtil
import com.catcsyun.liantadog.utils.dj.GetHttpDataUtil


/**
 * 开屏
 * */
@SuppressLint("StaticFieldLeak")
object AdSPUtils {

    private var mAdUnitId = AppConst.GMSPAd_ID
    private var mContext: Activity? = null

    private var adNetworkPlatformName = ""
    private var adNetworkRitId = ""
    private var preEcpm = ""
    private val adType = AppConst.KAIPING

    var mTTSplashAd: CSJSplashAd? = null


    private var mIsRequestInfo = false
    private var initPreloading = false

    private var mListener: GmSplashAdListener? = null
    var showNum = 0
    var clickNum = 0

    interface GmSplashAdListener {

        fun onLoadFinish()
        fun onLoadFail()
        fun onClose()
    }

    /**
    public MediationSplashRequestInfo(String adnName, String adnSlotId, String appId, String appkey)
    adnName 参考MediationConstant.ADN_MINTEGRALadnSlotId 注意这里是代码位idappId adn idappkey如果没有可以传空
     */
    val pangleSplashBottom = object : MediationSplashRequestInfo(
        MediationConstant.ADN_PANGLE,
        AppConst.GMDDAd_ID, AppConst.Ad_ID, ""
    ) {} //ok
//    val gdtSplashBottom = object : MediationSplashRequestInfo(MediationConstant.ADN_GDT,"9093517612222759", "1106706357", ""){} //ok
//    val ksSplashBottom = object : MediationSplashRequestInfo(MediationConstant.ADN_KS,"4000000042", "90009", ""){} //ok
//    val baiduSplashBottom = object : MediationSplashRequestInfo(MediationConstant.ADN_BAIDU,"2058622", "e866cfb0", ""){} //ok


    fun init(listener: GmSplashAdListener, activity: Activity, isRequestInfo: Boolean) {
        mContext = activity
        mListener = listener
//        if (AppConst.is_show_ad) {
        initPreloading = false
        mIsRequestInfo = isRequestInfo

        //加载开屏广告
        loadSplashAd()
//        }
    }

    /**
     *  预加载
     */
    fun initPreloading(listener: GmSplashAdListener, activity: Activity, isRequestInfo: Boolean) {
        mContext = activity
        mListener = listener
//        if (AppConst.is_show_ad) {
        initPreloading = true
        mIsRequestInfo = isRequestInfo

        //加载开屏广告
        loadSplashAd()
//        }
    }

    /**
     * 加载开屏广告
     */
    private fun loadSplashAd() {
        GetHttpDataUtil.reportAdReport(
            AppConst.REPORT_TYPE_REQUEST,
            "GroMore",
            "",
            mAdUnitId,
            adType,
            "",
            AppConst.IAPP_SCENE
        )
        val adNativeLoader = TTAdSdk.getAdManager().createAdNative(mContext)
        val adslot = AdSlot.Builder()
            .setCodeId(mAdUnitId)
            .setImageAcceptedSize(
                DisplayUtil.getWindowWidth(mContext), DisplayUtil.getWindowHeight(
                    mContext
                )
            )
            .setMediationAdSlot(
                MediationAdSlot.Builder()
                    .setMediationSplashRequestInfo(pangleSplashBottom)
                    .setBidNotify(true)//lhm add
                    .build()
            )
            .build()


        adNativeLoader.loadSplashAd(adslot, object : TTAdNative.CSJSplashAdListener {


            override fun onSplashLoadSuccess(p0: CSJSplashAd?) {

            }

            override fun onSplashLoadFail(csjAdError: CSJAdError?) {
                Log.d(
                    AppConst.TAG,
                    "splash load fail, errCode: " + csjAdError?.getCode() + ", errMsg: " + csjAdError?.getMsg()
                );
                mListener?.onLoadFail()
            }

            override fun onSplashRenderSuccess(CSJSplashAd: CSJSplashAd?) {
                mTTSplashAd = CSJSplashAd
                mListener?.onLoadFinish()
                GetHttpDataUtil.reportAdReport(
                    AppConst.REPORT_TYPE_REQUEST_OK,
                    "GroMore",
                    "",
                    mAdUnitId,
                    adType,
                    "",
                    AppConst.IAPP_SCENE
                )
            }

            override fun onSplashRenderFail(CSJSplashAd: CSJSplashAd?, csjAdError: CSJAdError?) {
                Log.d(
                    AppConst.TAG,
                    "splash render fail, errCode: " + csjAdError!!.code + ", errMsg: " + csjAdError.msg
                )
                mListener?.onLoadFail()

            }
        }, 3500)


        Log.i(AppConst.TAG, "AppConst.REPORT_TYPE_REQUEST" + AppConst.REPORT_TYPE_REQUEST)
    }

    fun isReady(): Boolean {
        if (null != mTTSplashAd && mTTSplashAd?.mediationManager != null && mTTSplashAd?.mediationManager!!.isReady) {
            return mTTSplashAd?.mediationManager!!.isReady
        }
        return false
    }

    /**
     * 展示开屏广告
     */
    fun showSplash(mSplashContainer: FrameLayout) {
        showNum++
        mTTSplashAd?.let {
            it.setSplashAdListener(object : CSJSplashAd.SplashAdListener {

                override fun onSplashAdShow(csjSplashAd: CSJSplashAd?) {
                    Log.i(AppConst.TAG, "onSplashAdShow")
                    var manager = it.mediationManager;
                    if (manager != null && manager.showEcpm != null) {
                        adNetworkPlatformName = manager.showEcpm.sdkName
                        adNetworkRitId = manager.showEcpm.slotId
                        preEcpm = manager.showEcpm.ecpm
                    }
                    GetHttpDataUtil.reportAdReport(
                        AppConst.REPORT_TYPE_SHOW,
                        adNetworkPlatformName,
                        adNetworkRitId,
                        mAdUnitId,
                        adType,
                        preEcpm, AppConst.IAPP_SCENE
                    )
                }

                override fun onSplashAdClick(csjSplashAd: CSJSplashAd?) {
                    Log.i(AppConst.TAG, "onSplashAdClicked")
                    if (clickNum != showNum) {
                        GetHttpDataUtil.reportAdReport(
                            AppConst.REPORT_TYPE_CLICK,
                            adNetworkPlatformName,
                            adNetworkRitId,
                            mAdUnitId,
                            adType,
                            preEcpm, AppConst.IAPP_SCENE
                        )
                        clickNum = showNum
                    }
                }

                override fun onSplashAdClose(csjSplashAd: CSJSplashAd?, closeType: Int) {
                    if (closeType == CSJSplashCloseType.CLICK_SKIP) {
                        Log.d(AppConst.TAG, "开屏广告点击跳过")
                    } else if (closeType == CSJSplashCloseType.COUNT_DOWN_OVER) {
                        Log.d(AppConst.TAG, "开屏广告点击倒计时结束")
                    } else if (closeType == CSJSplashCloseType.CLICK_JUMP) {
                        Log.d(AppConst.TAG, "点击跳转")
                    }
                    mListener?.onClose()
                }

            })
            Log.i(AppConst.TAG, "onAdTimeOver")
            mSplashContainer.removeAllViews()
            try {
                Handler().postDelayed({
                    it.splashView?.let { splashView ->
                        mSplashContainer.addView(splashView)
                    }
                }, 100)
            } catch (e: Exception) {

            }
        }
    }
}