package com.qingchu.wangmiao.utils.lzy

import android.app.Activity
import android.util.Log
import android.widget.FrameLayout
import com.qingchu.wangmiao.csj.AdCPNoLimitUtils
import com.qingchu.wangmiao.csj.AdCPUtils
import com.qingchu.wangmiao.csj.AdCPTwoUtils
import com.qingchu.wangmiao.csj.AdFeedSimpleOneUtils
import com.qingchu.wangmiao.csj.AdFeedSimpleTwoUtils
import com.qingchu.wangmiao.csj.AdRVUtils
import com.qingchu.wangmiao.AppConst
import com.qingchu.wangmiao.csj.AdCPFourUtils
import com.qingchu.wangmiao.csj.AdCPThreeUtils
import com.qingchu.wangmiao.csj.AdFeedSimpleFourUtils
import com.qingchu.wangmiao.csj.AdFeedSimpleThreeUtils
import com.qingchu.wangmiao.utils.dj.AntiRepeatClickUtils
import com.qingchu.wangmiao.utils.dj.UserInfoModel
import com.qingchu.wangmiao.widget.dialog.LoadingDiaLog

class LZYADSUtils(val tag: String,val activity: Activity?){
    val TAG = "LZYADSUtils"
    //adv-预加载广告--------------------------------------------------start
    fun initAdCpCheck(){
        if (!(!UserInfoModel.getIsCheckFlag() || AppConst.is_show_ad)) {
            return
        }
        AdCPNoLimitUtils.init(activity, object : AdCPNoLimitUtils.GirdMenuStateListener {
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
        if (!AdCPNoLimitUtils.isReady()) {
            Log.i("tttt","审核模式下的插屏预加载没有")
            AdCPNoLimitUtils.initPreloading("")
        } else {
            Log.i("tttt","审核模式下的插屏预加载是有的")
        }
    }

    fun initAdCp1(){
        if (!AppConst.is_show_ad) {
            return
        }
        AdCPUtils.init(activity, object : AdCPUtils.GirdMenuStateListener {
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
        if (!AdCPUtils.isReady()) {
            Log.i(TAG,"预加载插屏1")
            AdCPUtils.initPreloading("")
            UserInfoModel.setShowChapingYynTime(System.currentTimeMillis())
        }
    }
    fun initAdCp2(){
        if (!AppConst.is_show_ad) {
            return
        }
        AdCPTwoUtils.init(activity, object : AdCPTwoUtils.GirdMenuStateListener {
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
        if (!AdCPTwoUtils.isReady()) {
            Log.i(TAG,"预加载插屏2")
            AdCPTwoUtils.initPreloading("")
            UserInfoModel.setShowChapingHomeYynTime2(System.currentTimeMillis())
        }
    }
    fun initAdJL() {
        if (AppConst.is_show_ad&&activity!=null) {
            AdRVUtils.init(object : AdRVUtils.GirdMenuStateListener {
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
            if (!AdRVUtils.isReady()) {
                AdRVUtils.initPreloading("")
            }
        }
    }
    //adv-预加载广告--------------------------------------------------end



    //adv加载广告---------------------------------------------------start
    fun showAdCp1() {
        if (!AppConst.is_show_ad) {
            return
        }
        AdCPUtils.init(activity, object : AdCPUtils.GirdMenuStateListener {
            override fun onShowError() {
                // Do nothing for onShowError in Kotlin
            }

            override fun showVideoClosed() {
//                initAdCp1()
            }

            override fun onSuccess() {
                Log.e(tag, "tab cp onSuccess")
                AdCPUtils.showInterstitialFullAd(activity)
            }

            override fun onError() {
                Log.e(tag, "tab cp onError")
            }
        })

        if (!AdCPUtils.isReady()) {
            AdCPUtils.initPreloading("")
        } else {
            AdCPUtils.showInterstitialFullAd(activity)
        }
    }
    fun showAdCp2() {
        if (!AppConst.is_show_ad) {
            return
        }
        AdCPTwoUtils.init(activity, object : AdCPTwoUtils.GirdMenuStateListener {
            override fun onSuccess() {
                Log.e(tag, "tab cp2 onSuccess")
                AdCPTwoUtils.showInterstitialFullAd(activity)
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
        if (!AdCPTwoUtils.isReady()) {
            AdCPTwoUtils.initPreloading("")
        } else {
            AdCPTwoUtils.showInterstitialFullAd(activity)
        }
    }
    fun showAdCp3() {
        if (!AppConst.is_show_ad) {
            return
        }
        AdCPThreeUtils.init(activity, object : AdCPThreeUtils.GirdMenuStateListener {
            override fun onSuccess() {
                Log.e(tag, "tab cp2 onSuccess")
                AdCPThreeUtils.showInterstitialFullAd(activity)
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
        if (!AdCPThreeUtils.isReady()) {
            AdCPThreeUtils.initPreloading("")
        } else {
            AdCPThreeUtils.showInterstitialFullAd(activity)
        }
    }
    fun showAdCp4() {
        if (!AppConst.is_show_ad) {
            return
        }
        AdCPFourUtils.init(activity, object : AdCPFourUtils.GirdMenuStateListener {
            override fun onSuccess() {
                Log.e(tag, "tab cp2 onSuccess")
                AdCPFourUtils.showInterstitialFullAd(activity)
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
        if (!AdCPFourUtils.isReady()) {
            AdCPFourUtils.initPreloading("")
        } else {
            AdCPFourUtils.showInterstitialFullAd(activity)
        }
    }
    fun showAdJL(diaLog: LoadingDiaLog,play:()->Unit) {
        if (AppConst.is_show_ad&&activity!=null) {
            if (!AntiRepeatClickUtils.isFastClickJL()) {
                return
            }
            AdRVUtils.init(object : AdRVUtils.GirdMenuStateListener {
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
                    AdRVUtils.showRewardAd(activity)
                }
            }, activity)
            Log.e(tag, "激励 进来了GMRVAdUtils.isReady():" + AdRVUtils.isReady())
            if (AdRVUtils.isReady()) {
                AdRVUtils.showRewardAd(activity)
            } else {
                AdRVUtils.initPreloading("")
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
            AdRVUtils.init(object : AdRVUtils.GirdMenuStateListener {
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
                    AdRVUtils.showRewardAd(activity)
                }
            }, activity)
            Log.e(tag, "激励 进来了GMRVAdUtils.isReady():" + AdRVUtils.isReady())
            if (AdRVUtils.isReady()) {
                AdRVUtils.showRewardAd(activity)
            } else {
                AdRVUtils.initPreloading("")
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
            2 -> {
                showAdCp3()
            }
            3 -> {
                showAdCp4()
            }
        }
        AppConst.adsFlag = (AppConst.adsFlag + 1) % 4
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
            Log.e(tag, " showAdCp 进来了 GMCPAdUtils.isReady():" + AdCPUtils.isReady())
            UserInfoModel.setShowChapingYynTime(currentTimeMillis)
            AdCPUtils.init(activity, object : AdCPUtils.GirdMenuStateListener {
                override fun onSuccess() {
                    Log.e(tag, "tab cp onSuccess")
                    AdCPUtils.showInterstitialFullAd(activity)
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
            if (!AdCPUtils.isReady()) {
                AdCPUtils.initPreloading("")
            } else {
                AdCPUtils.showInterstitialFullAd(activity)
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
            Log.e(TAG, " showAdCp2 进来了 GMCPTwoAdUtils.isReady():" + AdCPTwoUtils.isReady())
            UserInfoModel.setShowChapingHomeYynTime2(currentTimeMillis)
            AdCPTwoUtils.init(activity, object : AdCPTwoUtils.GirdMenuStateListener {
                override fun onSuccess() {
                    Log.e(tag, "tab cp2 onSuccess")
                    AdCPTwoUtils.showInterstitialFullAd(activity)
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
            if (!AdCPTwoUtils.isReady()) {
                AdCPTwoUtils.initPreloading("")
            } else {
                AdCPTwoUtils.showInterstitialFullAd(activity)
            }
        }
    }
    //adv---------------------------------------------------end




    //adv加载信息流---------------------------------------------------start
    fun loadSimpleAd1(fragment: FrameLayout?) {
        if (activity != null && AppConst.is_show_ad) {
            AdFeedSimpleOneUtils.init(activity, object : AdFeedSimpleOneUtils.GirdMenuStateListener {
                override fun onSuccess() {
                    Log.e(TAG, " GMFeedSimpleAdOneUtils onSuccess")
                    if (fragment != null&&activity!=null) {
                        Log.i("tttt","准备刷新视频列表的小信息流")
                        AdFeedSimpleOneUtils.showAd(fragment, activity)
                    }
                }

                override fun onError() {
                    Log.e(TAG, " loadSimpleAdOne onError")
                }
            })
            AdFeedSimpleOneUtils.initPreloading()
        }
    }

    fun loadSimpleAd2(fragment: FrameLayout?) {
        if (activity != null && AppConst.is_show_ad) {
            AdFeedSimpleTwoUtils.init(activity, object : AdFeedSimpleTwoUtils.GirdMenuStateListener {
                override fun onSuccess() {
                    Log.e(TAG, " GMFeedSimpleAdTwoUtils onSuccess")
                    if (fragment != null&&activity!=null) {
                        AdFeedSimpleTwoUtils.showAd(fragment, activity)
                    }
                }

                override fun onError() {
                    Log.e(TAG, " loadSimpleAdOne onError")
                }
            })
            AdFeedSimpleTwoUtils.initPreloading("")
        }
    }
    fun initSimpleAd3(activity: Activity) {
        if (activity != null && AppConst.is_show_ad) {
            AdFeedSimpleThreeUtils.init(activity, object : AdFeedSimpleThreeUtils.GirdMenuStateListener {
                override fun onSuccess() {
                    Log.e(TAG, " GMFeedSimpleAdTwoUtils onSuccess")

                }

                override fun onError() {
                    Log.e(TAG, " loadSimpleAdOne onError")
                }
            })
            AdFeedSimpleThreeUtils.initPreloading("")
        }
    }
    fun initSimpleAd4(activity: Activity) {
        if (AppConst.is_show_ad) {
            AdFeedSimpleFourUtils.init(
                activity,
                object : AdFeedSimpleFourUtils.GirdMenuStateListener {


                    override fun onSuccess() {
                    }

                    override fun onError() {
                    }

                })
            if (!AdFeedSimpleFourUtils.isReady()) {
                AdFeedSimpleFourUtils.initPreloading("")
            }

        }
    }
    fun loadSimpleAd1(fragment: FrameLayout?,dip:Int) {
        if (activity != null && AppConst.is_show_ad) {
            AdFeedSimpleOneUtils.init(activity, object : AdFeedSimpleOneUtils.GirdMenuStateListener {
                override fun onSuccess() {
                    Log.e(TAG, " GMFeedSimpleAdOneUtils onSuccess")
                    if (fragment != null&&activity!=null) {
                        Log.i("tttt","准备刷新视频列表的小信息流")
                        AdFeedSimpleOneUtils.showAd(fragment, activity)
                    }
                }

                override fun onError() {
                    Log.e(TAG, " loadSimpleAdOne onError")
                }
            })
            AdFeedSimpleOneUtils.initPreloading(dip)
        }
    }

    fun loadSimpleAd2(fragment: FrameLayout?,dip:Int) {
        if (activity != null && AppConst.is_show_ad) {
            AdFeedSimpleTwoUtils.init(activity, object : AdFeedSimpleTwoUtils.GirdMenuStateListener {
                override fun onSuccess() {
                    Log.e(TAG, " GMFeedSimpleAdTwoUtils onSuccess")
                    if (fragment != null&&activity!=null) {
                        AdFeedSimpleTwoUtils.showAd(fragment, activity)
                    }
                }

                override fun onError() {
                    Log.e(TAG, " loadSimpleAdOne onError")
                }
            })
            AdFeedSimpleTwoUtils.initPreloading("",dip)
        }
    }
    fun loadSimpleAd3(activity: Activity,fragment: FrameLayout?) {
        if (activity != null && AppConst.is_show_ad) {
            AdFeedSimpleThreeUtils.init(activity, object : AdFeedSimpleThreeUtils.GirdMenuStateListener {
                override fun onSuccess() {
                    Log.e(TAG, " GMFeedSimpleAdTwoUtils onSuccess")
                    if (fragment != null&&activity!=null) {
                        AdFeedSimpleThreeUtils.showAd(fragment, activity)
                    }
                }

                override fun onError() {
                    Log.e(TAG, " loadSimpleAdOne onError")
                }
            })
            if (!AdFeedSimpleThreeUtils.isReady()) {
                AdFeedSimpleThreeUtils.initPreloading("")
            }else{
                if (fragment != null&&activity!=null) {
                    AdFeedSimpleThreeUtils.showAd(fragment, activity)
                }
            }

        }
    }
    fun loadSimpleAd4(activity: Activity,fragment: FrameLayout?) {
        if (activity != null && AppConst.is_show_ad) {
            AdFeedSimpleFourUtils.init(activity, object : AdFeedSimpleFourUtils.GirdMenuStateListener {
                override fun onSuccess() {
                    Log.e(TAG, " GMFeedSimpleAdTwoUtils onSuccess")
                    if (fragment != null&&activity!=null) {
                        AdFeedSimpleFourUtils.showAd(fragment, activity)
                    }
                }

                override fun onError() {
                    Log.e(TAG, " loadSimpleAdOne onError")
                }
            })
            if (!AdFeedSimpleFourUtils.isReady()) {
                AdFeedSimpleFourUtils.initPreloading("")
            }else{
                if (fragment != null&&activity!=null) {
                    AdFeedSimpleFourUtils.showAd(fragment, activity)
                }
            }
        }
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