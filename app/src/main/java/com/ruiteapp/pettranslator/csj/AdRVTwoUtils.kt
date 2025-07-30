package com.ruiteapp.pettranslator.csj

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.bytedance.sdk.openadsdk.*
import com.bytedance.sdk.openadsdk.mediation.ad.MediationAdSlot
import com.ruiteapp.pettranslator.AppConst
import com.ruiteapp.pettranslator.R
import com.ruiteapp.pettranslator.utils.dj.GetHttpDataUtil
import com.yl.adsdk.YlLib


/**
 * 激励视频
 */
@SuppressLint("StaticFieldLeak")
object AdRVTwoUtils {
    private var mAdUnitId = AppConst.GMRDAd_ID_IN
    var mTTRewardVideoAd: TTRewardVideoAd? = null

    private var adNetworkPlatformName = ""
    private var adNetworkRitId = ""
    private var preEcpm = ""

    private var mIsLoadedAndShow = false


    private var mContext: Activity? = null
    lateinit var mListener: GirdMenuStateListener
    var  showNum =0
    var  clickNum =0
    interface GirdMenuStateListener {
        fun showVideoClosed()
        fun onShowError()
        fun onEarnRewards()

        fun onLoadError()
        fun onLoadSuccess()

    }

    fun init(listener: GirdMenuStateListener, activity: Activity) {
        mListener = listener
        mContext = activity
    }

    /**
     * 预加载广告
     */
    fun initPreloading(scenarioId:String="") {
        if (AppConst.is_show_ad) {
            Log.e(AppConst.TAG, "激励预加载！")
            mIsLoadedAndShow = false
            loadRwdAd(scenarioId)

        }
    }

    /**
     * 直接展示广告
     */
//    fun directShow(adType: Int,scenarioId:String) {
//        if (AppConst.is_show_ad) {
//            Log.e(AppConst.TAG, "激励预加载directShow！")
//            mIsLoadedAndShow = true
//            loadRwdAd(scenarioId)
//        }
//    }



    private fun loadRwdAd(scenarioId:String){
        val adNativeLoader = TTAdSdk.getAdManager().createAdNative(mContext)
        /** 这里为激励视频的简单功能，如需使用复杂功能，如gromore的服务端奖励验证，请参考demo中的AdUtils.kt类中激励部分 */
        val adslot = AdSlot.Builder()
            .setCodeId(mAdUnitId)
            .setUserID("rewardTestId")
            .setOrientation(TTAdConstant.VERTICAL)
            .setMediationAdSlot(
                MediationAdSlot.Builder()
                    .setRewardAmount(2000)
                    .setRewardName("rewardname")
                    .setBidNotify(true) //lhm add
                    .setScenarioId(scenarioId)
                    .build()
            )
            .build()
        adNativeLoader.loadRewardVideoAd(adslot, object : TTAdNative.RewardVideoAdListener {
            override fun onError(code: Int, message: String?) {
                Log.i(AppConst.TAG, "onError code = ${code} msg = ${message}")
//                Toast.makeText(mContext, "load fail", Toast.LENGTH_SHORT).show()
                mListener?.onLoadError()

            }

            override fun onRewardVideoAdLoad(ad: TTRewardVideoAd?) {
                Log.i(AppConst.TAG, "onRewardVideoAdLoad")
                mTTRewardVideoAd = ad
                mTTRewardVideoAd?.let {
//                        PrintUtil.printLoadInfo(it.csjmAdInfo)
                }
//                Toast.makeText(mContext, "load success", Toast.LENGTH_SHORT)
//                    .show()
            }

            override fun onRewardVideoCached() {
                Log.i(AppConst.TAG, "onRewardVideoCached")
//                Toast.makeText(
//                    mContext,
//                    "load video cached",
//                    Toast.LENGTH_SHORT
//                ).show()
            }

            override fun onRewardVideoCached(ad: TTRewardVideoAd?) {
                Log.i(AppConst.TAG, "onRewardVideoCached")
//                Toast.makeText(
//                    mContext,
//                    "load video cached params",
//                    Toast.LENGTH_SHORT
//                ).show()
                mTTRewardVideoAd = ad
                if(mIsLoadedAndShow){
                    showRewardAd(mContext!!)
                    return
                }
                mListener?.onLoadSuccess()
                GetHttpDataUtil.reportAdReport(
                    AppConst.REPORT_TYPE_REQUEST_OK,
                    "GroMore",
                    "",
                    mAdUnitId,
                    AppConst.JILIVOID,
                    "",
                    AppConst.IAPP_SCENE
                )

            }
        })

        GetHttpDataUtil.reportAdReport(
            AppConst.REPORT_TYPE_REQUEST,
            "GroMore",
            "",
            mAdUnitId,
            AppConst.JILIVOID,
            "",
            AppConst.IAPP_SCENE
        )


    }

    fun isReady(): Boolean {
        if(mTTRewardVideoAd ==null ) {
            return false
        }
        if(mTTRewardVideoAd!!.mediationManager == null){
            return false
        }
        return mTTRewardVideoAd!!.mediationManager.isReady



    }

    /**
     * 展示广告
     */
    fun showRewardAd(activity: Activity){

        if (!AppConst.is_show_ad) {
            return
        }


        if (mTTRewardVideoAd == null) {
            Log.i(AppConst.TAG, "请先加载广告或等待广告加载完毕后再调用show方法")
        }
        showNum++
        mTTRewardVideoAd?.let {
            if (it.mediationManager.isReady) {
                it.setRewardAdInteractionListener(object :
                    TTRewardVideoAd.RewardAdInteractionListener {
                    override fun onAdShow() {
                        Log.i(AppConst.TAG, "onAdShow")
                        var manager = it.mediationManager;

                        YlLib.showRewardTip("功能解锁中...请勿退出")


                        if (manager != null && manager.showEcpm != null) {
                            Log.i(
                                AppConst.TAG,
                                "mTTRewardVideoAd onAdShow  ecpm:" + manager.showEcpm.ecpm + "  sdkName:" + manager.showEcpm.sdkName + "   slotId:" + manager.showEcpm.slotId
                            )
                            adNetworkPlatformName = manager.showEcpm.sdkName
                            adNetworkRitId = manager.showEcpm.slotId
                            preEcpm = manager.showEcpm.ecpm

                        }
                        GetHttpDataUtil.reportAdReport(
                            AppConst.REPORT_TYPE_SHOW,
                            adNetworkPlatformName,
                            adNetworkRitId,
                            mAdUnitId,
                            AppConst.JILIVOID,
                            preEcpm, AppConst.IAPP_SCENE
                        )

//                        Toast.makeText(
//                            this@MediationRewardActivity,
//                            "adShow",
//                            Toast.LENGTH_SHORT
//                        ).show()
                    }

                    override fun onAdVideoBarClick() {
//                        Toast.makeText(
//                            this@MediationRewardActivity,
//                            "adclick",
//                            Toast.LENGTH_SHORT
//                        ).show()
                        Log.i(AppConst.TAG, "onAdVideoBarClick")
                        if (clickNum != showNum) {
                            GetHttpDataUtil.reportAdReport(
                                AppConst.REPORT_TYPE_CLICK,
                                adNetworkPlatformName,
                                adNetworkRitId,
                                mAdUnitId,
                                AppConst.JILIVOID,
                                preEcpm, AppConst.IAPP_SCENE
                            )
                            clickNum = showNum
                        }

                    }

                    override fun onAdClose() {
//                        Toast.makeText(
//                            this@MediationRewardActivity,
//                            "adclose",
//                            Toast.LENGTH_SHORT
//                        ).show()
                        Log.i(AppConst.TAG, "onAdClose")
                        mListener?.showVideoClosed()
                    }

                    override fun onVideoComplete() {
//                        Toast.makeText(
//                            this@MediationRewardActivity,
//                            "onVideoComplete",
//                            Toast.LENGTH_SHORT
//                        ).show()
                        Log.i(AppConst.TAG, "onVideoComplete")
                    }

                    override fun onVideoError() {
//                        Toast.makeText(
//                            this@MediationRewardActivity,
//                            "onVideoError",
//                            Toast.LENGTH_SHORT
//                        ).show()
                        mListener?.onShowError()
                        Log.i(AppConst.TAG, "onVideoError")
                    }

                    override fun onRewardVerify(
                        rewardVerify: Boolean,
                        rewardAmount: Int,
                        rewardName: String?,
                        errorCode: Int,
                        errorMsg: String?
                    ) {
                        //此方法不生效
                    }

                    override fun onRewardArrived(
                        isRewardValid: Boolean,
                        rewardType: Int,
                        extraInfo: Bundle?
                    ) {
                        /** 如果使用了Gromore服务端奖励验证功能，可参考AdUtils.kt类中的示例 */
                        Log.i(AppConst.TAG, "onRewardArrived, extra: " + extraInfo?.toString())
                        mListener?.onEarnRewards()
                        YlLib.hideRewardTip()
//                        Toast.makeText(
//                            this@MediationRewardActivity,
//                            "onRewardArrived",
//                            Toast.LENGTH_SHORT
//                        ).show()
                    }

                    override fun onSkippedVideo() {
//                        Toast.makeText(
//                            this@MediationRewardActivity,
//                            "onSkippedVideo",
//                            Toast.LENGTH_SHORT
//                        ).show()
                        Log.i(AppConst.TAG, "onSkippedVideo")
                    }

                })
                it.showRewardVideoAd(activity)
            }
        }
    }
}