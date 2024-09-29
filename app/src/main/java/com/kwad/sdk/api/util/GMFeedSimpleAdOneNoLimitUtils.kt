package com.kwad.sdk.api.util


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.baidu.mobads.sdk.api.RequestParameters
import com.baidu.mobads.sdk.api.StyleParams
import com.bytedance.sdk.openadsdk.*
import com.bytedance.sdk.openadsdk.mediation.MediationConstant
import com.bytedance.sdk.openadsdk.mediation.ad.MediationAdSlot
import com.bytedance.sdk.openadsdk.mediation.ad.MediationExpressRenderListener
import com.pet.translator.AppConst
import com.pet.translator.utils.dj.GetHttpDataUtil
import com.pet.translator.utils.dj.GetHttpDataUtil.reportAdReport
import com.google.android.gms.ads.formats.NativeAdOptions
import com.qq.e.ads.cfg.DownAPPConfirmPolicy
import com.qq.e.ads.cfg.VideoOption

@SuppressLint("StaticFieldLeak")
object GMFeedSimpleAdOneNoLimitUtils {

    private var mAdUnitId =  AppConst.FEEDSIMPLE_ID_ONE
    var mTTFeedAd: TTFeedAd? = null
    private lateinit var mContext: Context
    lateinit var mListener: GirdMenuStateListener
    var mIsLoadAndShow = false

    var adNetworkPlatformName = ""
    var adNetworkRitId = ""
    var preEcpm = ""
    var  showNum =0
    var  clickNum =0
    interface GirdMenuStateListener {
        fun onSuccess()
        fun onError()
    }


    fun init(context: Context, listener: GirdMenuStateListener){
        mListener = listener
        mContext = context
    }

    fun initPreloading(){
//        if (!AppConst.is_show_ad && AppConst.CHANNEL != "BAIDU") {
//            return
//        }
        val admobNaitveAdOptions =  NativeAdOptions.Builder().build()
        //baidu 百度请求参数
        val baiduRequestParameters = RequestParameters.Builder().build()
        //baidu 百度智能优选支持自定义视图样式，可以通过StyleParams来配置相关UI参数。
        val baiduSmartOptStyleParams = StyleParams.Builder().build()
        val gdtVideoOption = VideoOption.Builder().build()
        val gdtDownAppConfirmPolicy = DownAPPConfirmPolicy.NOConfirm

        val adNativeLoader = TTAdSdk.getAdManager().createAdNative(mContext)
        val adslot = AdSlot.Builder()
            .setCodeId(mAdUnitId)
//            .setImageAcceptedSize(DisplayUtil.getWindowWidth(mContext)-60,0) ////自渲染使用尺寸单位px
//            .setImageAcceptedSize(UISimpleUtils.getScreenWidthInPx(mContext), UISimpleUtils.dp2px(mContext, 340F)) // 单位px
            .setImageAcceptedSize(UISimpleUtils.getScreenWidthInPx(mContext)-90,0) // 单位px
//                .setExpressViewAcceptedSize(2000f,3000f)//模板使用尺寸单位dp
            .setAdCount(1)
            .setUserID("1234")
            .setOrientation(TTAdConstant.VERTICAL)
            .setMediationAdSlot(
                MediationAdSlot.Builder()
                    .setExtraObject(MediationConstant.ADN_PANGLE, "pangle media_extra")
                    .setExtraObject(MediationConstant.ADN_GDT, "gdt custom data")
                    .setExtraObject(MediationConstant.ADN_KS, "ks custom data")
                    .setExtraObject(
                        MediationConstant.CUSTOM_DATA_KEY_GROMORE_EXTRA,
                        "gromore serverside verify extra data"
                    )
                    .setExtraObject(MediationConstant.KEY_ADMOB_NATIVE_OPTIONS,admobNaitveAdOptions)
//                        .setExtraObject(MediationConstant.KEY_BAIDU_APPSID,"appsid")
                    .setExtraObject(MediationConstant.KEY_BAIDU_CACHE_VIDEO_ONLY_WIFI,true)
                    .setExtraObject(MediationConstant.KEY_BAIDU_REQUEST_PARAMETERS,baiduRequestParameters)
                    .setExtraObject(MediationConstant.KEY_BAIDU_NATIVE_SMART_OPT_STYLE_PARAMS,baiduSmartOptStyleParams)
                    .setExtraObject(MediationConstant.KEY_GDT_MIN_VIDEO_DURATION,1000)
                    .setExtraObject(MediationConstant.KEY_GDT_MAX_VIDEO_DURATION,2000)
                    .setExtraObject(MediationConstant.KEY_GDT_VIDEO_OPTION,gdtVideoOption)
                    .setExtraObject(MediationConstant.KEY_GDT_DOWN_APP_CONFIG_POLICY,gdtDownAppConfirmPolicy)
                    .setMuted(true)
                    .setVolume(0.7f)
                    .setUseSurfaceView(true)
                    .setBidNotify(true)
                    .setScenarioId("scenarioid")
                    .setSplashShakeButton(true)
                    .setSplashPreLoad(true)
                    .setRewardName("rewardname")
                    .setRewardAmount(500)
                    .setAllowShowCloseBtn(true)
                    .build()
            )
            .build()
//            flContent.removeAllViews()
        adNativeLoader.loadFeedAd(adslot, object : TTAdNative.FeedAdListener {
            override fun onError(code: Int, message: String?) {
                Log.i(AppConst.TAG, "onError code = ${code} msg = ${message}")
                mListener?.onError()

            }

            override fun onFeedAdLoad(ads: MutableList<TTFeedAd>?) {
                Log.i(AppConst.TAG, "onFeedAdLoad list.size = ${ads?.size}")
                ads?.let {
                    if (it.size > 0) {
                        mTTFeedAd = it[0]
                        mTTFeedAd?.let {
                            PrintUtil.printLoadInfo(it.mediationManager)
                        }
                    }
                    mListener?.onSuccess()
                    reportAdReport(
                        AppConst.REPORT_TYPE_REQUEST_OK,
                        "GroMore",
                        "",
                        mAdUnitId,
                        AppConst.XINGXINLIU,
                        "",
                        AppConst.IAPP_SCENE,
                        "0",
                        "0"
                    )

                }
            }
        })

        reportAdReport(
            AppConst.REPORT_TYPE_REQUEST,
            "GroMore",
            "",
            mAdUnitId,
            AppConst.XINGXINLIU,
            "",
            AppConst.IAPP_SCENE,
            "0",
            "0"
        )
    }


    fun showAd(flContent: ViewGroup, activity: Activity){

        if (mTTFeedAd == null) {
            Log.i(AppConst.TAG, "请先加载广告或等待广告加载完毕后再调用show方法")
            return
        }
        showNum++
        mTTFeedAd?.let { it ->
            flContent.removeAllViews()
            Log.i(AppConst.TAG, "button text ${it.buttonText}")
            Log.i(AppConst.TAG, "isReady = ${it.mediationManager.isReady}")
            Log.i(AppConst.TAG, "isExpress = ${it.mediationManager.isExpress}")
            if(it.mediationManager.isReady){
                it.setExpressRenderListener(object : MediationExpressRenderListener {
                    override fun onRenderSuccess(p0: View?, p1: Float, p2: Float, p3: Boolean) {
                        Log.i(AppConst.TAG, "onRenderSuccess")
                        it.setDislikeCallback(activity,object :TTAdDislike.DislikeInteractionCallback{
                            override fun onShow() {
                                Log.i(AppConst.TAG, "express dislike 点击show")
                            }

                            override fun onSelected(position: Int, value: String?, enforce: Boolean) {
                                Log.i(AppConst.TAG, "express 点击 $value")
                                //用户选择不喜欢原因后，移除广告展示
                                flContent.removeAllViews()
                            }

                            override fun onCancel() {
                                Log.i(AppConst.TAG, "express dislike 点击了取消")
                            }

                        })
                        it.adView?.let { view ->
                            if (view.parent != null) {
                                (view.parent as ViewGroup).removeView(view)
                            }
                            flContent.addView(view)
                        }
                    }

                    override fun onRenderFail(p0: View?, p1: String?, p2: Int) {
                        Log.i(AppConst.TAG, "onRenderFail")
                    }

                    override fun onAdClick() {
                        Log.i(AppConst.TAG, "onAdClick")
                        if (clickNum!= showNum) {
                            GetHttpDataUtil.reportAdReport(
                                AppConst.REPORT_TYPE_CLICK,
                                adNetworkPlatformName,
                                adNetworkRitId,
                                mAdUnitId,
                                AppConst.XINGXINLIU,
                                preEcpm,
                                AppConst.IAPP_SCENE,
                                "0",
                                "0"
                            )
                            clickNum= showNum
                        }
                    }

                    override fun onAdShow() {
                        Log.i(AppConst.TAG, "onAdShow")
                        Log.i(AppConst.TAG, "ad mediaExtraInfo ${it.mediaExtraInfo}")

                        var manager = it.mediationManager;
                        if (manager != null && manager.showEcpm != null) {
                            adNetworkPlatformName = manager.showEcpm.sdkName
                            adNetworkRitId = manager.showEcpm.slotId
                            preEcpm = manager.showEcpm.ecpm
                            Log.i(
                                AppConst.TAG,
                                "InterstitialFullActivity onAdShow  ecpm:" + manager.showEcpm.ecpm + "  sdkName:" + manager.showEcpm.sdkName + "   slotId:" + manager.showEcpm.slotId
                            )
                        }
                        reportAdReport(
                            AppConst.REPORT_TYPE_SHOW,
                            adNetworkPlatformName,
                            adNetworkRitId,
                            mAdUnitId,
                            AppConst.XINGXINLIU,
                            preEcpm,
                            AppConst.IAPP_SCENE,
                            "0",
                            "0"
                        )
                    }

                })
                it.render()
            } else {
//                val view: View? = when (it.imageMode) {
//                    TTAdConstant.IMAGE_MODE_SMALL_IMG -> getSmallAdView(flContent)
//                    TTAdConstant.IMAGE_MODE_LARGE_IMG -> getLargeAdView(flContent)
//                    TTAdConstant.IMAGE_MODE_GROUP_IMG -> getGroupAdView(flContent)
//                    TTAdConstant.IMAGE_MODE_VIDEO -> getVideoView(flContent)
//                    TTAdConstant.IMAGE_MODE_VERTICAL_IMG -> getVerticalAdView(flContent)
//                    TTAdConstant.IMAGE_MODE_VIDEO_VERTICAL -> getVideoView(flContent)
//                    else -> {
//                        Log.i(AppAppConst.TAG, "展示样式错误")
//                        null
//                    }
//                }
//                view?.let {
//                    it.layoutParams = ViewGroup.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.MATCH_PARENT
//                    )
//                    flContent.removeAllViews()
//                    flContent.addView(it)
//                }
            }
        }
    }

}