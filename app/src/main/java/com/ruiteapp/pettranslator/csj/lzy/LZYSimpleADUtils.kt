package com.ruiteapp.pettranslator.csj.lzy

import android.app.Activity
import android.util.Log
import android.widget.FrameLayout
import com.ruiteapp.pettranslator.AppConst
import com.ruiteapp.pettranslator.csj.AdFeedSimpleOneNoLimitUtils
import com.ruiteapp.pettranslator.csj.AdFeedSimpleOneUtils
import com.ruiteapp.pettranslator.csj.AdFeedSimpleThreeUtils
import com.ruiteapp.pettranslator.csj.AdFeedSimpleTwoUtils
import com.ruiteapp.pettranslator.utils.lzy.LZYLog


object LZYSimpleADUtils{
    val TAG = "LZYSimpleADUtils"

    var interval_simple1 = 0L
    var interval_simple2 = 0L
    var interval_simple3 = 0L


    var interval_simple4 = 0L


    var intervalTime = 5000;

//    fun loadSimpleAd1(activity: Activity,fragment: FrameLayout?,dip:Int) {
//        if (activity != null && AppConst.is_show_ad) {
//            AdFeedSimpleOneUtils.init(activity, object : AdFeedSimpleOneUtils.GirdMenuStateListener {
//                override fun onSuccess() {
//                    LZYLog.e(TAG, " GMFeedSimpleAdOneUtils onSuccess")
//                    if (fragment != null&&activity!=null) {
//                        Log.i("tttt","准备刷新视频列表的小信息流")
//                        AdFeedSimpleOneUtils.showAd(fragment, activity)
//                    }
//                }
//
//                override fun onError() {
//                    LZYLog.e(TAG, " loadSimpleAdOne onError")
//                }
//            })
//            AdFeedSimpleOneUtils.initPreloading(dip)
//        }
//    }
    fun loadSimpleAd1(activity: Activity,fragment: FrameLayout?) {
        if (activity != null && AppConst.is_show_ad) {
            AdFeedSimpleOneUtils.init(activity, object : AdFeedSimpleOneUtils.GirdMenuStateListener {
                override fun onSuccess() {
                    LZYLog.e(TAG, " GMFeedSimpleAdOneUtils onSuccess")
                    if (fragment != null&&activity!=null) {
                        Log.i("tttt","准备刷新视频列表的小信息流")
                        AdFeedSimpleOneUtils.showAd(fragment, activity)
                    }
                }

                override fun onError() {
                    LZYLog.e(TAG, " loadSimpleAdOne onError")
                }
            })
            AdFeedSimpleOneUtils.initPreloading()
        }
    }

    fun loadSimpleNoLimitAd1(activity: Activity,fragment: FrameLayout?) {
        if (activity != null) {
            AdFeedSimpleOneNoLimitUtils.init(activity, object : AdFeedSimpleOneNoLimitUtils.GirdMenuStateListener {
                override fun onSuccess() {
                    LZYLog.e(TAG, " GMFeedSimpleAdOneUtils onSuccess")
                    if (fragment != null&&activity!=null) {
                        Log.i("tttt","准备刷新视频列表的小信息流")
                        AdFeedSimpleOneNoLimitUtils.showAd(fragment, activity)
                    }
                }

                override fun onError() {
                    LZYLog.e(TAG, " loadSimpleAdOne onError")
                }
            })
            AdFeedSimpleOneNoLimitUtils.initPreloading()
        }
    }
    fun loadSimpleAd2(activity: Activity,fragment: FrameLayout?) {
        if (activity != null && AppConst.is_show_ad) {
            AdFeedSimpleTwoUtils.init(activity, object : AdFeedSimpleTwoUtils.GirdMenuStateListener {
                override fun onSuccess() {
                    LZYLog.e(TAG, " GMFeedSimpleAdTwoUtils onSuccess")
                    if (fragment != null&&activity!=null) {
                        LZYLog.e(TAG, " GMFeedSimpleAdTwoUtils onShowing")
                        AdFeedSimpleTwoUtils.showAd(fragment, activity)
                    }
                }

                override fun onError() {
                    LZYLog.e(TAG, " loadSimpleAdOne onError")
                }
            })
            AdFeedSimpleTwoUtils.initPreloading("")
        }
    }

    fun loadSimpleAd3(activity: Activity,fragment: FrameLayout?) {
        if (activity != null && AppConst.is_show_ad) {
            AdFeedSimpleThreeUtils.init(activity, object : AdFeedSimpleThreeUtils.GirdMenuStateListener {
                override fun onSuccess() {
                    if (fragment != null&&activity!=null) {
                        AdFeedSimpleThreeUtils.showAd(fragment, activity)
                    }
                }

                override fun onError() {
                }
            })
            AdFeedSimpleThreeUtils.initPreloading("")
        }
    }

//    fun loadSimpleAd4(activity: Activity,fragment: FrameLayout?) {
//        if (activity != null && AppConst.is_show_ad) {
//            AdFeedSimpleFourUtils.init(activity, object : AdFeedSimpleFourUtils.GirdMenuStateListener {
//                override fun onSuccess() {
//                    if (fragment != null&&activity!=null) {
//                        AdFeedSimpleFourUtils.showAd(fragment, activity)
//                    }
//                }
//
//                override fun onError() {
//                }
//            })
//            AdFeedSimpleFourUtils.initPreloading("")
//        }
//    }
//
//
//
//
//    fun initSimpleAdCach3(activity: Activity) {
//        if (activity != null && AppConst.is_show_ad) {
//            AdFeedSimpleThreeUtils.init(activity, object : AdFeedSimpleThreeUtils.GirdMenuStateListener {
//                override fun onSuccess() {
//                }
//                override fun onError() {
//                }
//            })
//            if(AdFeedSimpleThreeUtils.isReady()){
//
//            }else{
//                AdFeedSimpleThreeUtils.initPreloading("")
//            }
//
//        }
//    }
//
//    fun loadSimpleAdCache3(activity: Activity,fragment: FrameLayout?) {
//        if (activity != null && AppConst.is_show_ad) {
//            AdFeedSimpleThreeUtils.init(activity, object : AdFeedSimpleThreeUtils.GirdMenuStateListener {
//                override fun onSuccess() {
//                    if (fragment != null&&activity!=null) {
//                        AdFeedSimpleThreeUtils.showAd(fragment, activity)
//                    }
//                }
//
//                override fun onError() {
//                }
//            })
//            if(AdFeedSimpleThreeUtils.isReady()){
//                if (fragment != null&&activity!=null) {
//                    AdFeedSimpleThreeUtils.showAd(fragment, activity)
//                }
//            }else{
//                AdFeedSimpleThreeUtils.initPreloading("")
//            }
//
//        }
//    }



}