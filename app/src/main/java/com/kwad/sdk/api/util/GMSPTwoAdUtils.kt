package com.kwad.sdk.api.util

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.bytedance.sdk.openadsdk.*
import com.bytedance.sdk.openadsdk.mediation.MediationConstant
import com.bytedance.sdk.openadsdk.mediation.ad.MediationAdSlot
import com.bytedance.sdk.openadsdk.mediation.ad.MediationSplashRequestInfo
import com.pet.translator.AppConst
import com.pet.translator.utils.dj.DisplayUtil
import com.pet.translator.utils.dj.GetHttpDataUtil
import com.pet.translator.utils.dj.UIUtils


/**
* 开屏2
* */
@SuppressLint("StaticFieldLeak")
object GMSPTwoAdUtils {


    private var mAdUnitId = AppConst.GMSPAd_TWO_ID
    private var mContext: Activity? = null

    private var adNetworkPlatformName = ""
    private var adNetworkRitId = ""
    private var preEcpm = ""
    private val adType = AppConst.KAIPING

    var mTTSplashAd: TTSplashAd? = null



    private  var mIsRequestInfo = false
    private  var initPreloading = false

    private var  mListener : GmSplashAdListener? = null
    var  showNum =0
    var  clickNum =0
    interface GmSplashAdListener {

        fun onLoadFinish()
        fun onLoadFail()
        fun onClose()
    }

    /**
    public MediationSplashRequestInfo(String adnName, String adnSlotId, String appId, String appkey)
    adnName 参考MediationConstant.ADN_MINTEGRALadnSlotId 注意这里是代码位idappId adn idappkey如果没有可以传空
     */
    val pangleSplashBottom = object : MediationSplashRequestInfo(MediationConstant.ADN_PANGLE,AppConst.GMDDAd_TWO_ID, AppConst.Ad_ID, ""){} //ok
//    val gdtSplashBottom = object : MediationSplashRequestInfo(MediationConstant.ADN_GDT,"9093517612222759", "1106706357", ""){} //ok
//    val ksSplashBottom = object : MediationSplashRequestInfo(MediationConstant.ADN_KS,"4000000042", "90009", ""){} //ok
//    val baiduSplashBottom = object : MediationSplashRequestInfo(MediationConstant.ADN_BAIDU,"2058622", "e866cfb0", ""){} //ok


    fun init(listener: GmSplashAdListener, activity: Activity, isRequestInfo:Boolean) {
        mContext = activity
        mListener = listener
        if (AppConst.is_show_ad) {
        initPreloading = false
        mIsRequestInfo =isRequestInfo

        //加载开屏广告
        loadSplashAd()
        }
    }

    /**
     *  预加载
     */
    fun initPreloading(listener: GmSplashAdListener, activity: Activity, isRequestInfo:Boolean) {
        mContext = activity
        mListener = listener
        if (AppConst.is_show_ad) {
        initPreloading = true
        mIsRequestInfo =isRequestInfo

        //加载开屏广告
        loadSplashAd()
        }
    }

    /**
     * 加载开屏广告
     */
    private fun loadSplashAd(){

        val adNativeLoader = TTAdSdk.getAdManager().createAdNative(mContext)
        val adslot = AdSlot.Builder()
            .setCodeId(mAdUnitId)
            .setImageAcceptedSize(
                DisplayUtil.getWindowWidth(mContext), DisplayUtil.getWindowHeight(
                mContext
            ))
            .setMediationAdSlot(
                MediationAdSlot.Builder()
                    .setMediationSplashRequestInfo(pangleSplashBottom)
                    .setBidNotify(true)//lhm add
                    .build()
            )
            .build()
        adNativeLoader.loadSplashAd(adslot, object : TTAdNative.SplashAdListener {
            override fun onError(code: Int, message: String?) {
                Log.i(AppConst.TAG, "onError code = ${code} msg = ${message}")
                mListener?.onLoadFail()

            }

            override fun onTimeout() {
                Log.i(AppConst.TAG, "onTimeout")
                mListener?.onLoadFail()

            }

            override fun onSplashAdLoad(ad: TTSplashAd?) {
                Log.i(AppConst.TAG, "onSplashAdLoad")
                mTTSplashAd = ad
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
//                showSplashAd(ad);
            }
        })

        GetHttpDataUtil.reportAdReport(
            AppConst.REPORT_TYPE_REQUEST,
            "GroMore",
            "",
            mAdUnitId,
            adType,
            "",
            AppConst.IAPP_SCENE
        )
    }

    fun isReady(): Boolean {
        if (null!= mTTSplashAd && mTTSplashAd?.mediationManager!=null && mTTSplashAd?.mediationManager!!.isReady) {
            return mTTSplashAd?.mediationManager!!.isReady
        }
        return false
    }
    /**
     * 展示开屏广告
     */
    fun showSplash(mSplashContainer: FrameLayout){
        showNum++
        mTTSplashAd?.let {
            it.setSplashInteractionListener(object : TTSplashAd.AdInteractionListener{
                override fun onAdClicked(view: View?, type: Int) {
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

                override fun onAdShow(view: View?, type: Int) {
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

                override fun onAdSkip() {
                    Log.i(AppConst.TAG, "onSplashAdSkip")
                    mListener?.onClose()
                }

                override fun onAdTimeOver() {
                    Log.i(AppConst.TAG, "onSplashAdTimeOver")
                    mListener?.onClose()

                }

            })
            it.setSplashClickEyeListener(object: ISplashClickEyeListener {
                override fun onSplashClickEyeAnimationStart() {
                    Log.i(AppConst.TAG, "SplashActivity onSplashClickEyeAnimationStart")
                }

                override fun onSplashClickEyeAnimationFinish() {
                    Log.i(AppConst.TAG, "SplashActivity onSplashClickEyeAnimationFinish")
                }

                override fun isSupportSplashClickEye(isSupport: Boolean): Boolean {
                    Log.i(AppConst.TAG, "SplashActivity isSupportSplashClickEye")
                    var dp = it.splashClickEyeSizeToDp

                    // 点睛相关处理

                    // 点睛相关处理
                    val minWindowSizeFromSdk: IntArray = it.getSplashClickEyeSizeToDp()

                    val params: ViewGroup.LayoutParams = mSplashContainer.getLayoutParams() as ViewGroup.LayoutParams
                    params.height = UIUtils.dp2px(mContext, minWindowSizeFromSdk[1].toFloat())
                    params.width = UIUtils.dp2px(mContext, minWindowSizeFromSdk[0].toFloat())
//                            params.addr(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
//                            params.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE)
//                            params.rightMargin = 100
//                            params.bottomMargin = 150
                    mSplashContainer.setLayoutParams(params)
//                    flContent.setBackgroundResource(R.color.common_half_alpha)
                    mSplashContainer.setBackgroundColor(0x55000000)
                    mSplashContainer.translationX = 200f
                    mSplashContainer.translationY = 200f
                    it.splashClickEyeAnimationFinish()
                    return false
                }
            })
            it.setSplashCardListener(object : ISplashCardListener {
                override fun onSplashEyeReady() {
                    Log.i(AppConst.TAG, "onSplashEyeReady")
                    it.splashClickEyeAnimationFinish()
                }

                override fun onSplashClickEyeClose() {
                    Log.i(AppConst.TAG, "onSplashClickEyeClose")
                    //finish()
                }

                override fun setSupportSplashClickEye(isSupport: Boolean) {
                    Log.i(AppConst.TAG, "setSupportSplashClickEye:$isSupport")
                }

                override fun getActivity(): Activity {
                    Log.i(AppConst.TAG, "getActivity")
                    return mContext!!
                }

            })
            Log.i(AppConst.TAG, "onAdTimeOver")
            mSplashContainer.removeAllViews()
            it.splashView?.let {  splashView ->
                mSplashContainer.addView(splashView)
            }
        }
    }
}