package com.pet.translator.utils.lzy

import android.app.Activity
import android.util.Log
import android.widget.FrameLayout
import com.pet.translator.AppConst
import com.pet.translator.utils.dj.AntiRepeatClickUtils
import com.pet.translator.utils.dj.UserInfoModel
import com.kwad.sdk.api.util.GMCPAdNoLimitUtils
import com.kwad.sdk.api.util.GMCPAdUtils
import com.kwad.sdk.api.util.GMCPTwoAdUtils
import com.kwad.sdk.api.util.GMFeedSimpleAdOneUtils
import com.kwad.sdk.api.util.GMFeedSimpleAdTwoUtils
import com.kwad.sdk.api.util.GMRVAdUtils
import com.pet.translator.widget.dialog.LoadingDiaLog

class LZYADSUtils(val tag: String,val activity: Activity?){
    val TAG = "LZYADSUtils"
    //adv-预加载广告--------------------------------------------------start
    fun initAdCpCheck(){
        if (!(!UserInfoModel.getIsCheckFlag() || AppConst.is_show_ad)) {
            return
        }
        GMCPAdNoLimitUtils.init(activity, object : GMCPAdNoLimitUtils.GirdMenuStateListener {
            override fun onSuccess() {
                Log.e(TAG, "first one cp onSuccess")
            }

            override fun onError() {
                Log.e(TAG, "first one cp onError")
            }

            override fun showVideoClosed() {
            }

            override fun onShowError() {
            }
        })
        if (!GMCPAdNoLimitUtils.isReady()) {
            Log.i("tttt","审核模式下的插屏预加载没有")
            GMCPAdNoLimitUtils.initPreloading("")
        } else {
            Log.i("tttt","审核模式下的插屏预加载是有的")
        }
    }

    fun initAdCp1(){
        if (!AppConst.is_show_ad) {
            return
        }
        GMCPAdUtils.init(activity, object : GMCPAdUtils.GirdMenuStateListener {
            override fun onSuccess() {
                Log.e(tag, "tab cp onSuccess")
            }

            override fun onError() {
                Log.e(tag, "tab cp onError")
            }

            override fun showVideoClosed() {
                Log.e(tag, "tab cp showVideoClosed")

            }

            override fun onShowError() {
                Log.e(tag, "tab cp onShowError")
            }
        })
        if (!GMCPAdUtils.isReady()) {
            Log.i(TAG,"预加载插屏1")
            GMCPAdUtils.initPreloading("")
            UserInfoModel.setShowChapingYynTime(System.currentTimeMillis())
        }
    }
    fun initAdCp2(){
        if (!AppConst.is_show_ad) {
            return
        }
        GMCPTwoAdUtils.init(activity, object : GMCPTwoAdUtils.GirdMenuStateListener {
            override fun onSuccess() {
                Log.e(tag, "tab cp onSuccess")
            }

            override fun onError() {
                Log.e(tag, "tab cp onError")
            }

            override fun showVideoClosed() {
            }

            override fun onShowError() {
            }

        })
        if (!GMCPTwoAdUtils.isReady()) {
            Log.i(TAG,"预加载插屏2")
            GMCPTwoAdUtils.initPreloading("")
            UserInfoModel.setShowChapingHomeYynTime2(System.currentTimeMillis())
        }
    }
    fun initAdJL() {
        if (AppConst.is_show_ad&&activity!=null) {
            GMRVAdUtils.init(object : GMRVAdUtils.GirdMenuStateListener {
                override fun onShowError() {
                }

                override fun showVideoClosed() {
                }

                override fun onEarnRewards() {
                    // Implement your logic for onEarnRewards
                }

                override fun onLoadError() {
                    // Implement your logic for onLoadError
                }

                override fun onLoadSuccess() {
                    // Implement your logic for onLoadSuccess
                }
            }, activity)
            if (!GMRVAdUtils.isReady()) {
                GMRVAdUtils.initPreloading("")
            }
        }
    }
    //adv-预加载广告--------------------------------------------------end



    //adv加载广告---------------------------------------------------start
    fun showAdCp1() {
        if (!AppConst.is_show_ad) {
            return
        }
        GMCPAdUtils.init(activity, object : GMCPAdUtils.GirdMenuStateListener {
            override fun onShowError() {
                // Do nothing for onShowError in Kotlin
            }

            override fun showVideoClosed() {
//                initAdCp1()
            }

            override fun onSuccess() {
                Log.e(tag, "tab cp onSuccess")
                GMCPAdUtils.showInterstitialFullAd(activity)
            }

            override fun onError() {
                Log.e(tag, "tab cp onError")
            }
        })

        if (!GMCPAdUtils.isReady()) {
            GMCPAdUtils.initPreloading("")
        } else {
            GMCPAdUtils.showInterstitialFullAd(activity)
        }
    }
    fun showAdCp2() {
        if (!AppConst.is_show_ad) {
            return
        }
        GMCPTwoAdUtils.init(activity, object : GMCPTwoAdUtils.GirdMenuStateListener {
            override fun onSuccess() {
                Log.e(tag, "tab cp2 onSuccess")
                GMCPTwoAdUtils.showInterstitialFullAd(activity)
            }

            override fun onError() {
                Log.e(tag, "tab cp2 onError")
            }

            override fun showVideoClosed() {
//                initAdCp2()
            }

            override fun onShowError() {
            }

        })
        if (!GMCPTwoAdUtils.isReady()) {
            GMCPTwoAdUtils.initPreloading("")
        } else {
            GMCPTwoAdUtils.showInterstitialFullAd(activity)
        }
    }
    fun showAdJL(diaLog: LoadingDiaLog,play:()->Unit) {
        if (AppConst.is_show_ad&&activity!=null) {
            if (!AntiRepeatClickUtils.isFastClickJL()) {
                return
            }
            GMRVAdUtils.init(object : GMRVAdUtils.GirdMenuStateListener {
                override fun showVideoClosed() {
                    Log.e(tag, "main jl showVideoClosed")
                    play()
                }

                override fun onShowError() {
                    Log.e(tag, "main jl onLoadError")
                    play()
                }

                override fun onEarnRewards() {
                }

                override fun onLoadError() {
                    diaLog.dismiss()
                    play()
                }

                override fun onLoadSuccess() {
                    Log.e(tag, "jl onLoadSuccess")
                    diaLog.dismiss()
                    GMRVAdUtils.showRewardAd(activity)
                }
            }, activity)
            Log.e(tag, "激励 进来了GMRVAdUtils.isReady():" + GMRVAdUtils.isReady())
            if (GMRVAdUtils.isReady()) {
                GMRVAdUtils.showRewardAd(activity)
            } else {
                GMRVAdUtils.initPreloading("")
            }
        } else {
            diaLog.dismiss()
            play()
        }
    }

    fun showAdJL(play:()->Unit) {
        if (AppConst.is_show_ad&&activity!=null) {
            if (!AntiRepeatClickUtils.isFastClickJL()) {
                return
            }
            GMRVAdUtils.init(object : GMRVAdUtils.GirdMenuStateListener {
                override fun showVideoClosed() {
                    Log.e(tag, "main jl showVideoClosed")
                    play()
//                    initAdJL()
                }

                override fun onShowError() {
                    Log.e(tag, "main jl onLoadError")
                    play()
                }

                override fun onEarnRewards() {
                }

                override fun onLoadError() {
                    play()
                }

                override fun onLoadSuccess() {
                    Log.e(tag, "jl onLoadSuccess")
                    GMRVAdUtils.showRewardAd(activity)
                }
            }, activity)
            Log.e(tag, "激励 进来了GMRVAdUtils.isReady():" + GMRVAdUtils.isReady())
            if (GMRVAdUtils.isReady()) {
                GMRVAdUtils.showRewardAd(activity)
            } else {
                GMRVAdUtils.initPreloading("")
            }
        } else {
            play()
        }
    }
    fun showAdCpTurn(){
        when(AppConst.adsFlag){
            0 -> {
                showAdCp1()
            }
            1 -> {
                showAdCp2()
            }
        }
        AppConst.adsFlag = (AppConst.adsFlag + 1) % 2
    }
    //adv加载广告---------------------------------------------------end

    //adv有限制的加载广告---------------------------------------------------start
    fun showAdCp1WithLimit() {
        if (!AppConst.is_show_ad) {
            return
        }
        val currentTimeMillis = System.currentTimeMillis()
        val showTime = UserInfoModel.getShowChapingYynTime()
        if (currentTimeMillis - showTime > 2 * 1000) {
            Log.e(tag, " showAdCp 进来了 GMCPAdUtils.isReady():" + GMCPAdUtils.isReady())
            UserInfoModel.setShowChapingYynTime(currentTimeMillis)
            GMCPAdUtils.init(activity, object : GMCPAdUtils.GirdMenuStateListener {
                override fun onSuccess() {
                    Log.e(tag, "tab cp onSuccess")
                    GMCPAdUtils.showInterstitialFullAd(activity)
                }

                override fun onError() {
                    Log.e(tag, "tab cp onError")
                }

                override fun showVideoClosed() {
                    Log.e(tag, "tab cp showVideoClosed")
//                    initAdCp1()
                }

                override fun onShowError() {
                    Log.e(tag, "tab cp onShowError")
                }
            })
            if (!GMCPAdUtils.isReady()) {
                GMCPAdUtils.initPreloading("")
            } else {
                GMCPAdUtils.showInterstitialFullAd(activity)
            }
        }
    }
    fun showAdCp2WithLimit() {
        if (!AppConst.is_show_ad) {
            return
        }
        val currentTimeMillis = System.currentTimeMillis()
        val showTime = UserInfoModel.getShowChapingHomeYynTime2()
        if (currentTimeMillis - showTime > 2 * 1000) {
            Log.e(TAG, " showAdCp2 进来了 GMCPTwoAdUtils.isReady():" + GMCPTwoAdUtils.isReady())
            UserInfoModel.setShowChapingHomeYynTime2(currentTimeMillis)
            GMCPTwoAdUtils.init(activity, object : GMCPTwoAdUtils.GirdMenuStateListener {
                override fun onSuccess() {
                    Log.e(tag, "tab cp2 onSuccess")
                    GMCPTwoAdUtils.showInterstitialFullAd(activity)
                }

                override fun onError() {
                    Log.e(tag, "tab cp2 onError")
                }

                override fun showVideoClosed() {
                    Log.e(tag, "tab cp2 showVideoClosed")
//                    initAdCp2()
                }

                override fun onShowError() {
                    Log.e(tag, "tab cp2 onShowError")
                }

            })
            if (!GMCPTwoAdUtils.isReady()) {
                GMCPTwoAdUtils.initPreloading("")
            } else {
                GMCPTwoAdUtils.showInterstitialFullAd(activity)
            }
        }
    }
    //adv---------------------------------------------------end




    //adv加载信息流---------------------------------------------------start
    fun loadSimpleAd1(fragment: FrameLayout?) {
        if (activity != null && AppConst.is_show_ad) {
            GMFeedSimpleAdOneUtils.init(activity, object : GMFeedSimpleAdOneUtils.GirdMenuStateListener {
                override fun onSuccess() {
                    Log.e(TAG, " GMFeedSimpleAdOneUtils onSuccess")
                    if (fragment != null&&activity!=null) {
                        Log.i("tttt","准备刷新视频列表的小信息流")
                        GMFeedSimpleAdOneUtils.showAd(fragment, activity)
                    }
                }

                override fun onError() {
                    Log.e(TAG, " loadSimpleAdOne onError")
                }
            })
        }
        GMFeedSimpleAdOneUtils.initPreloading()
    }

    fun loadSimpleAd2(fragment: FrameLayout?) {
        if (activity != null && AppConst.is_show_ad) {
            GMFeedSimpleAdTwoUtils.init(activity, object : GMFeedSimpleAdTwoUtils.GirdMenuStateListener {
                override fun onSuccess() {
                    Log.e(TAG, " GMFeedSimpleAdTwoUtils onSuccess")
                    if (fragment != null&&activity!=null) {
                        GMFeedSimpleAdTwoUtils.showAd(fragment, activity)
                    }
                }

                override fun onError() {
                    Log.e(TAG, " loadSimpleAdOne onError")
                }
            })
        }
        GMFeedSimpleAdTwoUtils.initPreloading("")
    }
    fun loadSimpleAd1(fragment: FrameLayout?,dip:Int) {
        if (activity != null && AppConst.is_show_ad) {
            GMFeedSimpleAdOneUtils.init(activity, object : GMFeedSimpleAdOneUtils.GirdMenuStateListener {
                override fun onSuccess() {
                    Log.e(TAG, " GMFeedSimpleAdOneUtils onSuccess")
                    if (fragment != null&&activity!=null) {
                        Log.i("tttt","准备刷新视频列表的小信息流")
                        GMFeedSimpleAdOneUtils.showAd(fragment, activity)
                    }
                }

                override fun onError() {
                    Log.e(TAG, " loadSimpleAdOne onError")
                }
            })
        }
        GMFeedSimpleAdOneUtils.initPreloading(dip)
    }

    fun loadSimpleAd2(fragment: FrameLayout?,dip:Int) {
        if (activity != null && AppConst.is_show_ad) {
            GMFeedSimpleAdTwoUtils.init(activity, object : GMFeedSimpleAdTwoUtils.GirdMenuStateListener {
                override fun onSuccess() {
                    Log.e(TAG, " GMFeedSimpleAdTwoUtils onSuccess")
                    if (fragment != null&&activity!=null) {
                        GMFeedSimpleAdTwoUtils.showAd(fragment, activity)
                    }
                }

                override fun onError() {
                    Log.e(TAG, " loadSimpleAdOne onError")
                }
            })
        }
        GMFeedSimpleAdTwoUtils.initPreloading("",dip)
    }
    fun loadSimpleAdTurn(fragment: FrameLayout?,dip:Int){
        when(AppConst.adsInfoFlag){
            0 -> {
                if (dip == -1){
                    loadSimpleAd1(fragment)
                }else{
                    loadSimpleAd1(fragment,dip)
                }
            }
            1 -> {
                if (dip == -1){
                    loadSimpleAd2(fragment)
                }else{
                    loadSimpleAd2(fragment,dip)
                }
            }
        }
        AppConst.adsInfoFlag = (AppConst.adsInfoFlag + 1) % 2
    }
    //adv加载信息流---------------------------------------------------end
    fun loadSimpleAd1WithLimit(fragment: FrameLayout?,second:Int){
        val currentTimeMillis = System.currentTimeMillis()
        val showTime = UserInfoModel.getShowXinxiluiYynTime()
        if (currentTimeMillis - showTime > second * 1000) {
            UserInfoModel.setShowXinxiluiYynTime(currentTimeMillis)
            loadSimpleAd1(fragment)
        }
    }

    fun loadSimpleAd2WithLimit(fragment: FrameLayout?,second:Int){
        val currentTimeMillis = System.currentTimeMillis()
        val showTime = UserInfoModel.getShowXinxiluiYynTime2()
        if (currentTimeMillis - showTime > second * 1000) {
            UserInfoModel.setShowXinxiluiYynTime2(currentTimeMillis)
            loadSimpleAd2(fragment)
        }
    }
}