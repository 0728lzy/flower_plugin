package com.kwad.sdk.api.util

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import com.bytedance.sdk.openadsdk.*
import com.bytedance.sdk.openadsdk.mediation.ad.MediationAdSlot
import com.pet.translator.AppConst
import com.pet.translator.utils.dj.GetHttpDataUtil


@SuppressLint("StaticFieldLeak")
object GMCPAdNoLimitUtils {
    private var mAdUnitId = AppConst.GMCPAd_ID_IN
    private var mContext: Activity? = null
    var mTTFullScreenVideoAd: TTFullScreenVideoAd? = null

    private var adNetworkPlatformName = ""
    private var adNetworkRitId = ""
    private var preEcpm = ""
    private val adType = AppConst.CHAPING
    private var mIsLoadedAndShow = false
    lateinit var mListener: GirdMenuStateListener
    var  showNum =0
    var  clickNum =0
    interface GirdMenuStateListener {
        fun onSuccess()
        fun onError()
        fun showVideoClosed()
        fun onShowError()
    }


    fun init(context: Activity?, listener: GirdMenuStateListener) {
        mContext = context;
        mListener = listener
    }

    /**
     * 预加载广告
     */
    fun initPreloading(scenarioId :String="") {
        Log.i(AppConst.TAG, "initPreloading cp1")
        mIsLoadedAndShow = false
        loadInterstitialFullAd(scenarioId)
    }

//    fun directShow(scenarioId :String) {
//        mIsLoadedAndShow = true
//        loadInterstitialFullAd(scenarioId)
//    }

    /**
     * 加载插全屏广告
     */
    private fun loadInterstitialFullAd(scenarioId :String) {
//        if (!AppConst.is_show_ad && AppConst.CHANNEL != "BAIDU") {
//            return
//        }
        val adNativeLoader =
            TTAdSdk.getAdManager().createAdNative(mContext)
        val adslot = AdSlot.Builder()
            .setCodeId(mAdUnitId)
            .setOrientation(TTAdConstant.VERTICAL)
            .setUserID("user123")
            .setMediationAdSlot( //lhm add
                MediationAdSlot.Builder()
                    .setMuted(false)
                    .setVolume(0.7f)
                    .setUseSurfaceView(true)
                    .setBidNotify(true)
                    .setScenarioId("scenarioid")
                    .setSplashShakeButton(true)
                    .setSplashPreLoad(true)
                    .setRewardName("rewardname")
                    .setRewardAmount(500)
                    .setScenarioId(scenarioId)
                    .setAllowShowCloseBtn(true)
                    .build()
            )
            .build()
        adNativeLoader.loadFullScreenVideoAd(
            adslot,
            object : TTAdNative.FullScreenVideoAdListener {
                override fun onError(code: Int, message: String?) {
                    Log.i(AppConst.TAG, "onError code = ${code} msg = ${message}")
                    mListener?.onError()
//                    EventBus.getDefault().post(CpResultEvent("1"))
                }

                override fun onFullScreenVideoAdLoad(ad: TTFullScreenVideoAd?) {
                    Log.i(AppConst.TAG, "onCPN1AdLoad")
                    mTTFullScreenVideoAd = ad
                    mTTFullScreenVideoAd?.let {


                    }
                }

                override fun onFullScreenVideoCached() {
                    Log.i(AppConst.TAG, "onCpN1Cached")
                    if (mIsLoadedAndShow) {
                        showInterstitialFullAd(mContext)
                    } else {
                        mListener?.onSuccess()
                        GetHttpDataUtil.reportAdReport(
                            AppConst.REPORT_TYPE_REQUEST_OK,
                            "GroMore",
                            "",
                            mAdUnitId,
                            AppConst.CHAPING,
                            "",
                            AppConst.IAPP_SCENE
                        )

                    }

                }

                override fun onFullScreenVideoCached(ad: TTFullScreenVideoAd?) {
                    Log.i(AppConst.TAG, "onCpN1Cached")
                    mTTFullScreenVideoAd = ad


                }
            })
        GetHttpDataUtil.reportAdReport(
            AppConst.REPORT_TYPE_REQUEST,
            "GroMore",
            "",
            mAdUnitId,
            AppConst.CHAPING,
            "",
            AppConst.IAPP_SCENE
        )
    }

    var GMCPShowFail = 101   //插屏应用内 显示失败
    var GMCPFullClosed = 102   //插屏应用内 关闭
    var GMCPVideoComplete = 103   //插屏应用内 播放完成
    var GMCPSkippedVideo = 104   //插屏应用内 跳过
    fun isReady(): Boolean {
        if (mTTFullScreenVideoAd != null && mTTFullScreenVideoAd?.mediationManager != null && mTTFullScreenVideoAd?.mediationManager!!.isReady) {
            return true
        }
        return false
    }

    /**
     * 展示插全屏广告
     */
    fun showInterstitialFullAd(activity: Activity?) {
        if (mTTFullScreenVideoAd == null) {
            mListener.onShowError()
//            EventBus.getDefault().post(CpResultEvent("1"))
            Log.i(AppConst.TAG, "请先加载广告或等待广告加载完毕后再调用show方法")
        }
        showNum++
        mTTFullScreenVideoAd?.let {
            if (it.mediationManager.isReady) {
                it.setFullScreenVideoAdInteractionListener(object :
                    TTFullScreenVideoAd.FullScreenVideoAdInteractionListener {
                    override fun onAdShow() {
                        var manager = it.mediationManager;
                        if (manager != null && manager.showEcpm != null) {
                            Log.i(
                                AppConst.TAG,
                                "InterstitialFullActivity onAdShow CPN1  ecpm:" + manager.showEcpm.ecpm + "  sdkName:" + manager.showEcpm.sdkName + "   slotId:" + manager.showEcpm.slotId
                            )
                            adNetworkPlatformName = manager.showEcpm.sdkName
                            adNetworkRitId = manager.showEcpm.slotId
                            preEcpm = manager.showEcpm.ecpm
                        }else{
//                            EventBus.getDefault().post(CpResultEvent("2"))
                        }
                        GetHttpDataUtil.reportAdReport(AppConst.REPORT_TYPE_SHOW,
                            adNetworkPlatformName,
                            adNetworkRitId,
                            mAdUnitId,
                            adType,
                            preEcpm,AppConst.IAPP_SCENE
                        )
                        Log.e(AppConst.TAG, " showInterstitialFullAd  CpN1  onAdShow");

                    }

                    override fun onAdVideoBarClick() {
                        Log.e(AppConst.TAG, "InterstitialFullActivity CpN1 onAdVideoBarClick");
                        if (clickNum!= showNum) {
                            GetHttpDataUtil.reportAdReport(
                                AppConst.REPORT_TYPE_CLICK,
                                adNetworkPlatformName,
                                adNetworkRitId,
                                mAdUnitId,
                                adType,
                                preEcpm, AppConst.IAPP_SCENE
                            )
                            clickNum= showNum
                        }
                    }

                    override fun onAdClose() {
                        mListener?.showVideoClosed()
                        Log.e(AppConst.TAG, "InterstitialFullActivity CpN1 onAdClose");
//                        EventBus.getDefault().post(CpResultEvent("1"))
                    }

                    override fun onVideoComplete() {
                        Log.e(AppConst.TAG, "InterstitialFullActivity CpN1 onVideoComplete");
//                        EventBus.getDefault().post(CpResultEvent("1"))
                    }

                    override fun onSkippedVideo() {
                        Log.e(AppConst.TAG, "InterstitialFullActivity CpN1 onSkippedVideo");
//                        EventBus.getDefault().post(CpResultEvent("1"))
                    }

                })
                it.showFullScreenVideoAd(activity)
            } else {
                mListener?.onShowError()
//                EventBus.getDefault().post(CpResultEvent("1"))
                Log.i(AppConst.TAG, "showInterstitialFullAd CpN1 is not ready")
            }
        }
    }


}