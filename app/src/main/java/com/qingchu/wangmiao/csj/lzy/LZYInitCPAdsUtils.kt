package com.qingchu.wangmiao.csj.lzy

import android.app.Activity
import com.qingchu.wangmiao.AppConst
import com.qingchu.wangmiao.csj.AdCPFourUtils
import com.qingchu.wangmiao.csj.AdCPThreeUtils
import com.qingchu.wangmiao.csj.AdCPTwoUtils
import com.qingchu.wangmiao.csj.AdCPUtils
import com.qingchu.wangmiao.utils.lzy.LZYLog

object LZYInitCPAdsUtils {

    //记录间隔时间
    var record_time_cp1 = 0L
    var record_time_cp2 = 0L
    var record_time_cp3 = 0L
    var record_time_cp4 = 0L
    var record_time_cp5 = 0L
    var record_time_cp6 = 0L
    var record_time_cp7 = 0L
    var record_time_cp8 = 0L

    //最大间隔时间
    var intervalTime = 5000;

    //加载状态
    var state_cp_1=0//0代表加载中，1代表加载成功，2代表加载失败
    var state_cp_2=0//0代表加载中，1代表加载成功，2代表加载失败
    var state_cp_3=0//0代表加载中，1代表加载成功，2代表加载失败
    var state_cp_4=0//0代表加载中，1代表加载成功，2代表加载失败
    var state_cp_5=0//0代表加载中，1代表加载成功，2代表加载失败
    var state_cp_6=0//0代表加载中，1代表加载成功，2代表加载失败
    var state_cp_7=0//0代表加载中，1代表加载成功，2代表加载失败
    var state_cp_8=0//0代表加载中，1代表加载成功，2代表加载失败


    //adv-预加载广告--------------------------------------------------start
    fun initAdCp1(activity: Activity,tag:String= activity::class.java.simpleName){
        if (!AppConst.is_show_ad) {
            return
        }
        AdCPUtils.init(activity, object : AdCPUtils.GirdMenuStateListener {
            override fun onSuccess() {
                LZYLog.e(tag, "tab cp onSuccess")
                state_cp_1 =1
            }

            override fun onError() {
                LZYLog.e(tag, "tab cp onError")
                state_cp_1 =2
            }

            override fun showVideoClosed() {

            }

            override fun onShowError() {
            }
        })
        if (!AdCPUtils.isReady()) {
            if (((System.currentTimeMillis()- record_time_cp1) > intervalTime)|| state_cp_1 ==2) {
                LZYLog.i(tag, "预加载插屏1")
                AdCPUtils.initPreloading("")
                state_cp_1 =0
                record_time_cp1 =System.currentTimeMillis()
            }
        }
    }
    fun initAdCp2(activity: Activity,tag:String= activity::class.java.simpleName){
        if (!AppConst.is_show_ad) {
            return
        }
        AdCPTwoUtils.init(activity, object : AdCPTwoUtils.GirdMenuStateListener {
            override fun onSuccess() {
                LZYLog.e(tag, "tab cp2 onSuccess")
                state_cp_2 =1
            }

            override fun onError() {
                LZYLog.e(tag, "tab cp2 onError")
                state_cp_2 =2
            }

            override fun showVideoClosed() {
            }

            override fun onShowError() {
            }

        })
        if (!AdCPTwoUtils.isReady()) {
            if (((System.currentTimeMillis()- record_time_cp2) > intervalTime)|| state_cp_2 ==2) {
                LZYLog.i(tag, "预加载插屏2")
                AdCPTwoUtils.initPreloading("")
                state_cp_2 =0
                record_time_cp2 =System.currentTimeMillis()
            }
        }
    }
    fun initAdCp3(activity: Activity,tag:String= activity::class.java.simpleName){
        if (!AppConst.is_show_ad) {
            return
        }
        AdCPThreeUtils.init(activity, object : AdCPThreeUtils.GirdMenuStateListener {
            override fun onSuccess() {
                LZYLog.e(tag, "tab cp3 onSuccess")
                state_cp_3 =1
            }

            override fun onError() {
                LZYLog.e(tag, "tab cp3 onError")
                state_cp_3 =2
            }

            override fun showVideoClosed() {
            }

            override fun onShowError() {
            }

        })
        if (!AdCPThreeUtils.isReady()) {
            if (((System.currentTimeMillis()- record_time_cp3) > intervalTime)|| state_cp_3 ==2) {
                LZYLog.i(tag, "预加载插屏3")
                AdCPThreeUtils.initPreloading("")
                state_cp_3 =0
                record_time_cp3 =System.currentTimeMillis()
            }
        }
    }
    fun initAdCp4(activity: Activity,tag:String= activity::class.java.simpleName){
        if (!AppConst.is_show_ad) {
            return
        }
        AdCPFourUtils.init(activity, object : AdCPFourUtils.GirdMenuStateListener {
            override fun onSuccess() {
                LZYLog.e(tag, "tab cp4 onSuccess")
                state_cp_4 =1
            }

            override fun onError() {
                LZYLog.e(tag, "tab cp4 onError")
                state_cp_4 =2
            }

            override fun showVideoClosed() {
            }

            override fun onShowError() {
            }

        })
        if (!AdCPFourUtils.isReady()) {
            if (((System.currentTimeMillis()- record_time_cp4) > intervalTime)|| state_cp_4 ==2) {
                LZYLog.i(tag, "预加载插屏4")
                AdCPFourUtils.initPreloading("")
                state_cp_4 =0
                record_time_cp4 =System.currentTimeMillis()
            }
        }
    }
//    fun initAdCp5(activity: Activity,tag:String= activity::class.java.simpleName){
//        if (!AppConst.is_show_ad) {
//            return
//        }
//        AdCPFiveUtils.init(activity, object : AdCPFiveUtils.GirdMenuStateListener {
//            override fun onSuccess() {
//                LZYLog.e(tag, "tab cp4 onSuccess")
//                state_cp_5 =1
//            }
//
//            override fun onError() {
//                LZYLog.e(tag, "tab cp4 onError")
//                state_cp_5 =2
//            }
//
//            override fun showVideoClosed() {
//            }
//
//            override fun onShowError() {
//            }
//
//        })
//        if (!AdCPFiveUtils.isReady()) {
//            if (((System.currentTimeMillis()- record_time_cp5) > intervalTime)|| state_cp_5 ==2) {
//                LZYLog.i(tag, "预加载插屏5")
//                AdCPFiveUtils.initPreloading("")
//                state_cp_5 =0
//                record_time_cp5 =System.currentTimeMillis()
//            }
//        }
//    }
//    fun initAdCp6(activity: Activity,tag:String= activity::class.java.simpleName){
//        if (!AppConst.is_show_ad) {
//            return
//        }
//        AdCPSixUtils.init(activity, object : AdCPSixUtils.GirdMenuStateListener {
//            override fun onSuccess() {
//                LZYLog.e(tag, "tab cp6 onSuccess")
//                state_cp_6 =1
//            }
//
//            override fun onError() {
//                LZYLog.e(tag, "tab cp6 onError")
//                state_cp_6 =2
//            }
//
//            override fun showVideoClosed() {
//            }
//
//            override fun onShowError() {
//            }
//
//        })
//        if (!AdCPSixUtils.isReady()) {
//            if (((System.currentTimeMillis()- record_time_cp6) > intervalTime)|| state_cp_6 ==2) {
//                LZYLog.i(tag, "预加载插屏6")
//                AdCPSixUtils.initPreloading("")
//                state_cp_6 =0
//                record_time_cp6 =System.currentTimeMillis()
//            }
//        }
//    }
//    fun initAdCp7(activity: Activity,tag:String= activity::class.java.simpleName){
//        if (!AppConst.is_show_ad) {
//            return
//        }
//        AdCPSevenUtils.init(activity, object : AdCPSevenUtils.GirdMenuStateListener {
//            override fun onSuccess() {
//                LZYLog.e(tag, "tab cp7 onSuccess")
//                state_cp_7 =1
//            }
//
//            override fun onError() {
//                LZYLog.e(tag, "tab cp7 onError")
//                state_cp_7 =2
//            }
//
//            override fun showVideoClosed() {
//            }
//
//            override fun onShowError() {
//            }
//
//        })
//        if (!AdCPSevenUtils.isReady()) {
//            if (((System.currentTimeMillis()- record_time_cp7) > intervalTime)|| state_cp_7 ==2) {
//                LZYLog.i(tag, "预加载插屏7")
//                AdCPSevenUtils.initPreloading("")
//                state_cp_7 =0
//                record_time_cp7 =System.currentTimeMillis()
//            }
//        }
//    }


    //adv加载广告---------------------------------------------------start
    fun showAdCp1(activity: Activity,tag:String= activity::class.java.simpleName) {
        if (!AppConst.is_show_ad) {
            return
        }
        AdCPUtils.init(activity, object : AdCPUtils.GirdMenuStateListener {
            override fun onShowError() {
            }

            override fun showVideoClosed() {
            }

            override fun onSuccess() {
                LZYLog.e(tag, "tab cp onSuccess")
                state_cp_1 =1
                AdCPUtils.showInterstitialFullAd(activity)
            }

            override fun onError() {
                LZYLog.e(tag, "tab cp onError")
                state_cp_1 =2
            }
        })
        if (!AdCPUtils.isReady()) {
            if (((System.currentTimeMillis()- record_time_cp1) > intervalTime)|| state_cp_1 ==2) {
                AdCPUtils.initPreloading("")
                state_cp_1 =0
                record_time_cp1 =System.currentTimeMillis()
            }
        } else {
            AdCPUtils.showInterstitialFullAd(activity)
        }
    }

    fun showAdCp2(activity: Activity,tag:String= activity::class.java.simpleName) {
        if (!AppConst.is_show_ad) {
            return
        }
        AdCPTwoUtils.init(activity, object : AdCPTwoUtils.GirdMenuStateListener {
            override fun onSuccess() {
                LZYLog.e(tag, "tab cp2 onSuccess")
                state_cp_2 =1
                AdCPTwoUtils.showInterstitialFullAd(activity)
            }

            override fun onError() {
                LZYLog.e(tag, "tab cp2 onError")
                state_cp_2 =2
            }

            override fun showVideoClosed() {
            }

            override fun onShowError() {
            }

        })
        if (!AdCPTwoUtils.isReady()) {
            if (((System.currentTimeMillis()- record_time_cp2) > intervalTime)|| state_cp_2 ==2) {
                AdCPTwoUtils.initPreloading("")
                state_cp_2 =0
                record_time_cp2 =System.currentTimeMillis()
            }
        } else {
            AdCPTwoUtils.showInterstitialFullAd(activity)
        }
    }

    fun showAdCp3(activity: Activity,tag:String= activity::class.java.simpleName) {
        if (!AppConst.is_show_ad) {
            return
        }
        AdCPThreeUtils.init(activity, object : AdCPThreeUtils.GirdMenuStateListener {
            override fun onSuccess() {
                LZYLog.e(tag, "tab cp3 onSuccess")
                state_cp_3 =1
                AdCPThreeUtils.showInterstitialFullAd(activity)
            }

            override fun onError() {
                LZYLog.e(tag, "tab cp3 onError")
                state_cp_3 =2
            }

            override fun showVideoClosed() {
            }

            override fun onShowError() {
            }

        })
        if (!AdCPThreeUtils.isReady()) {
            if (((System.currentTimeMillis()- record_time_cp3) > intervalTime)|| state_cp_3 ==2) {
                AdCPThreeUtils.initPreloading("")
                state_cp_3 =0
                record_time_cp3 =System.currentTimeMillis()
            }
        } else {
            AdCPThreeUtils.showInterstitialFullAd(activity)
        }
    }
    fun showAdCp4(activity: Activity,tag:String= activity::class.java.simpleName) {
        if (!AppConst.is_show_ad) {
            return
        }
        AdCPFourUtils.init(activity, object : AdCPFourUtils.GirdMenuStateListener {
            override fun onSuccess() {
                LZYLog.e(tag, "tab cp4 onSuccess")
                state_cp_4 =1
                AdCPFourUtils.showInterstitialFullAd(activity)
            }

            override fun onError() {
                LZYLog.e(tag, "tab cp4 onError")
                state_cp_4 =2
            }

            override fun showVideoClosed() {
            }

            override fun onShowError() {
            }

        })
        if (!AdCPFourUtils.isReady()) {
            if (((System.currentTimeMillis()- record_time_cp4) > intervalTime)|| state_cp_4 ==2) {
                AdCPFourUtils.initPreloading("")
                state_cp_4 =0
                record_time_cp4 =System.currentTimeMillis()
            }
        } else {
            AdCPFourUtils.showInterstitialFullAd(activity)
        }
    }
//    fun showAdCp5(activity: Activity,tag:String= activity::class.java.simpleName) {
//        if (!AppConst.is_show_ad) {
//            return
//        }
//        AdCPFiveUtils.init(activity, object : AdCPFiveUtils.GirdMenuStateListener {
//            override fun onSuccess() {
//                LZYLog.e(tag, "tab cp5 onSuccess")
//                state_cp_5 =1
//                AdCPFiveUtils.showInterstitialFullAd(activity)
//            }
//
//            override fun onError() {
//                LZYLog.e(tag, "tab cp5 onError")
//                state_cp_5 =2
//            }
//
//            override fun showVideoClosed() {
//            }
//
//            override fun onShowError() {
//            }
//
//        })
//        if (!AdCPFiveUtils.isReady()) {
//            if (((System.currentTimeMillis()- record_time_cp5) > intervalTime)|| state_cp_5 ==2) {
//                AdCPFiveUtils.initPreloading("")
//                state_cp_5 =0
//                record_time_cp5 =System.currentTimeMillis()
//            }
//        } else {
//            AdCPFiveUtils.showInterstitialFullAd(activity)
//        }
//    }
//    fun showAdCp6(activity: Activity,tag:String= activity::class.java.simpleName) {
//        if (!AppConst.is_show_ad) {
//            return
//        }
//        AdCPSixUtils.init(activity, object : AdCPSixUtils.GirdMenuStateListener {
//            override fun onSuccess() {
//                LZYLog.e(tag, "tab cp6 onSuccess")
//                state_cp_6 =1
//                AdCPSixUtils.showInterstitialFullAd(activity)
//            }
//
//            override fun onError() {
//                LZYLog.e(tag, "tab cp6 onError")
//                state_cp_6 =2
//            }
//
//            override fun showVideoClosed() {
//            }
//
//            override fun onShowError() {
//            }
//
//        })
//        if (!AdCPSixUtils.isReady()) {
//            if (((System.currentTimeMillis()- record_time_cp6) > intervalTime)|| state_cp_6 ==2) {
//                AdCPSixUtils.initPreloading("")
//                state_cp_6 =0
//                record_time_cp6 =System.currentTimeMillis()
//            }
//        } else {
//            AdCPSixUtils.showInterstitialFullAd(activity)
//        }
//    }
//    fun showAdCp7(activity: Activity,tag:String= activity::class.java.simpleName) {
//        if (!AppConst.is_show_ad) {
//            return
//        }
//        AdCPSevenUtils.init(activity, object : AdCPSevenUtils.GirdMenuStateListener {
//            override fun onSuccess() {
//                LZYLog.e(tag, "tab cp7 onSuccess")
//                state_cp_7 =1
//                AdCPSevenUtils.showInterstitialFullAd(activity)
//            }
//
//            override fun onError() {
//                LZYLog.e(tag, "tab cp7 onError")
//                state_cp_7 =2
//            }
//
//            override fun showVideoClosed() {
//            }
//
//            override fun onShowError() {
//            }
//
//        })
//        if (!AdCPSevenUtils.isReady()) {
//            if (((System.currentTimeMillis()- record_time_cp7) > intervalTime)|| state_cp_7 ==2) {
//                AdCPSevenUtils.initPreloading("")
//                state_cp_7 =0
//                record_time_cp7 =System.currentTimeMillis()
//            }
//        } else {
//            AdCPSevenUtils.showInterstitialFullAd(activity)
//        }
//    }

    fun showAdCp1(activity: Activity,tag:String= activity::class.java.simpleName,play:()->Unit) {
        if (!AppConst.is_show_ad) {
            return
        }
        AdCPUtils.init(activity, object : AdCPUtils.GirdMenuStateListener {
            override fun onShowError() {
                play()
            }

            override fun showVideoClosed() {
                play()
            }

            override fun onSuccess() {
                LZYLog.e(tag, "tab cp onSuccess")
                state_cp_1 =1
                AdCPUtils.showInterstitialFullAd(activity)
            }

            override fun onError() {
                LZYLog.e(tag, "tab cp onError")
                state_cp_1 =2
                play()
            }
        })
        if (!AdCPUtils.isReady()) {
            if (((System.currentTimeMillis()- record_time_cp1) > intervalTime)|| state_cp_1 ==2) {
                AdCPUtils.initPreloading("")
                state_cp_1 =0
                record_time_cp1 =System.currentTimeMillis()
            }
        } else {
            AdCPUtils.showInterstitialFullAd(activity)
        }
    }

    fun showAdCp2(activity: Activity,tag:String= activity::class.java.simpleName,play:()->Unit) {
        if (!AppConst.is_show_ad) {
            return
        }
        AdCPTwoUtils.init(activity, object : AdCPTwoUtils.GirdMenuStateListener {
            override fun onSuccess() {
                LZYLog.e(tag, "tab cp2 onSuccess")
                state_cp_2 =1
                AdCPTwoUtils.showInterstitialFullAd(activity)
            }

            override fun onError() {
                LZYLog.e(tag, "tab cp2 onError")
                state_cp_2 =2
                play()
            }

            override fun showVideoClosed() {
                play()
            }

            override fun onShowError() {
                play()
            }

        })
        if (!AdCPTwoUtils.isReady()) {
            if (((System.currentTimeMillis()- record_time_cp2) > intervalTime)|| state_cp_2 ==2) {
                AdCPTwoUtils.initPreloading("")
                state_cp_2 =0
                record_time_cp2 =System.currentTimeMillis()
            }
        } else {
            AdCPTwoUtils.showInterstitialFullAd(activity)
        }
    }

    fun showAdCp3(activity: Activity,tag:String= activity::class.java.simpleName,play:()->Unit) {
        if (!AppConst.is_show_ad) {
            return
        }
        AdCPThreeUtils.init(activity, object : AdCPThreeUtils.GirdMenuStateListener {
            override fun onSuccess() {
                LZYLog.e(tag, "tab cp3 onSuccess")
                state_cp_3 =1
                AdCPThreeUtils.showInterstitialFullAd(activity)
            }

            override fun onError() {
                LZYLog.e(tag, "tab cp3 onError")
                state_cp_3 =2
                play()
            }

            override fun showVideoClosed() {
                play()
            }

            override fun onShowError() {
                play()
            }

        })
        if (!AdCPThreeUtils.isReady()) {
            if (((System.currentTimeMillis()- record_time_cp3) > intervalTime)|| state_cp_3 ==2) {
                AdCPThreeUtils.initPreloading("")
                state_cp_3 =0
                record_time_cp3 =System.currentTimeMillis()
            }
        } else {
            AdCPThreeUtils.showInterstitialFullAd(activity)
        }
    }
    fun showAdCp4(activity: Activity,tag:String= activity::class.java.simpleName,play:()->Unit) {
        if (!AppConst.is_show_ad) {
            return
        }
        AdCPFourUtils.init(activity, object : AdCPFourUtils.GirdMenuStateListener {
            override fun onSuccess() {
                LZYLog.e(tag, "tab cp4 onSuccess")
                state_cp_4 =1
                AdCPFourUtils.showInterstitialFullAd(activity)
            }

            override fun onError() {
                LZYLog.e(tag, "tab cp4 onError")
                state_cp_4 =2
                play()
            }

            override fun showVideoClosed() {
                play()
            }

            override fun onShowError() {
                play()
            }

        })
        if (!AdCPFourUtils.isReady()) {
            if (((System.currentTimeMillis()- record_time_cp4) > intervalTime)|| state_cp_4 ==2) {
                AdCPFourUtils.initPreloading("")
                state_cp_4 =0
                record_time_cp4 =System.currentTimeMillis()
            }
        } else {
            AdCPFourUtils.showInterstitialFullAd(activity)
        }
    }

    fun showAdCpTurnTab(activity: Activity,tag:String= activity::class.java.simpleName){
        when(AppConst.adsCPTabFlag){
            0 -> {
                showAdCp4(activity,tag)
                showAdCp3(activity,tag)
                initAdCp2(activity,tag)
//                initAdCp1(activity,tag)
            }
            1 -> {
                showAdCp2(activity,tag)
                showAdCp1(activity,tag)
                initAdCp4(activity,tag)
//                initAdCp5(activity,tag)
            }

        }
        AppConst.adsCPTabFlag = (AppConst.adsCPTabFlag + 1) % 2
    }

    fun showAdCpTurnNormal(activity: Activity,tag:String= activity::class.java.simpleName){
        when(AppConst.adsCPNormalFlag){
            0 -> {
                showAdCp4(activity,tag)
                initAdCp3(activity)
            }
            1 -> {
                showAdCp3(activity,tag)
                initAdCp2(activity)
            }
            2 -> {
                showAdCp2(activity,tag)
                initAdCp1(activity)
            }
            3 -> {
                showAdCp1(activity,tag)
                initAdCp4(activity)
            }

        }
        AppConst.adsCPNormalFlag = (AppConst.adsCPNormalFlag + 1) % 4
    }

//    fun showAdCpTurn(activity: Activity,tag:String= activity::class.java.simpleName){
//        when(AppConst.adsFlag){
//            0 -> {
//                showAdCp1(activity,tag)
//            }
//            1 -> {
//                showAdCp2(activity,tag)
//            }
//            2 -> {
//                showAdCp3(activity,tag)
//            }
//            3 -> {
//                showAdCp4(activity,tag)
//            }
//        }
//        AppConst.adsFlag = (AppConst.adsFlag + 1) % 4
//    }
//
//    fun showAdCpTurn(activity: Activity,tag:String= activity::class.java.simpleName,play: () -> Unit){
//        when(AppConst.adsFlag){
//            0 -> {
//                showAdCp1(activity,tag,play)
//            }
//            1 -> {
//                showAdCp2(activity,tag,play)
//            }
//            2 -> {
//                showAdCp3(activity,tag,play)
//            }
//            3 -> {
//                showAdCp4(activity,tag,play)
//            }
//        }
//        AppConst.adsFlag = (AppConst.adsFlag + 1) % 4
//    }
}