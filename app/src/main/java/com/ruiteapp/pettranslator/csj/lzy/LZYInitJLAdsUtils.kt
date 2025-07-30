package com.ruiteapp.pettranslator.csj.lzy

import android.app.Activity
import com.ruiteapp.pettranslator.AppConst
import com.ruiteapp.pettranslator.csj.AdRVTwoUtils
import com.ruiteapp.pettranslator.csj.AdRVUtils
import com.ruiteapp.pettranslator.utils.dj.AntiRepeatClickUtils
import com.ruiteapp.pettranslator.utils.lzy.LZYLog
import com.ruiteapp.pettranslator.widget.dialog.LoadingDiaLog

object LZYInitJLAdsUtils {

    //记录间隔时间
    var record_time_jl1 = 0L
    var record_time_jl2 = 0L

    //最大间隔时间
    var intervalTime = 5000;

    //加载状态
    var state_jl_1=0//0代表加载中，1代表加载成功，2代表加载失败
    var state_jl_2=0//0代表加载中，1代表加载成功，2代表加载失败

    fun initAdJL(activity: Activity?, tag:String= activity!!::class.java.simpleName) {
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
                    state_jl_1 =2
                }

                override fun onLoadSuccess() {
                    // Implement your logic for onLoadSuccess
                    state_jl_1 =1
                }
            }, activity)
            if (!AdRVUtils.isReady()) {
                if (((System.currentTimeMillis()- record_time_jl1) > intervalTime)|| state_jl_1 ==2) {
                    AdRVUtils.initPreloading("")
                    state_jl_1 =0
                    record_time_jl1 =System.currentTimeMillis()
                }
            }
        }
    }
    fun initAdJL2(activity: Activity?,tag:String= activity!!::class.java.simpleName) {
        if (AppConst.is_show_ad&&activity!=null) {
            AdRVTwoUtils.init(object : AdRVTwoUtils.GirdMenuStateListener {
                override fun onShowError() {
                }

                override fun showVideoClosed() {
                }

                override fun onEarnRewards() {
                    // Implement your logic for onEarnRewards
                }

                override fun onLoadError() {
                    // Implement your logic for onLoadError
                    state_jl_2 =2
                }

                override fun onLoadSuccess() {
                    // Implement your logic for onLoadSuccess
                    state_jl_2 =1
                }
            }, activity)
            if (!AdRVTwoUtils.isReady()) {
                if (((System.currentTimeMillis()- record_time_jl2) > intervalTime)|| state_jl_2 ==2) {
                    AdRVTwoUtils.initPreloading("")
                    state_jl_2 =0
                    record_time_jl2 =System.currentTimeMillis()
                }
            }
        }
    }

    fun showAdJL(diaLog: LoadingDiaLog, activity: Activity?, tag:String= activity!!::class.java.simpleName, play:()->Unit) {
        if (AppConst.is_show_ad&&activity!=null) {
            if (!AntiRepeatClickUtils.isFastClickJL()) {
                diaLog?.dismiss()
                return
            }
            AdRVUtils.init(object : AdRVUtils.GirdMenuStateListener {
                override fun showVideoClosed() {
                    LZYLog.e(tag, "main jl showVideoClosed")
                    if (diaLog.isShowing)
                        diaLog.dismiss()
                    play()
                    initAdJL2(activity, tag)
                }

                override fun onShowError() {
                    LZYLog.e(tag, "main jl onLoadError")
                    if (diaLog.isShowing)
                        diaLog.dismiss()
                    play()
                }

                override fun onEarnRewards() {
                }

                override fun onLoadError() {
                    diaLog.dismiss()
                    state_jl_1 =2
                    play()
                }

                override fun onLoadSuccess() {
                    LZYLog.e(tag, "jl onLoadSuccess")
                    diaLog.dismiss()
                    state_jl_1 =1
                    AdRVUtils.showRewardAd(activity)
                }
            }, activity)
            LZYLog.e(tag, "激励 进来了AdRVUtils.isReady():" + AdRVUtils.isReady())
            if (AdRVUtils.isReady()) {
                AdRVUtils.showRewardAd(activity)
            } else {
                if (((System.currentTimeMillis()- record_time_jl1) > intervalTime)|| state_jl_1 ==2) {
                    AdRVUtils.initPreloading("")
                    state_jl_1 =0
                    record_time_jl1 =System.currentTimeMillis()
                }
            }
        } else {
            diaLog.dismiss()
            play()
        }
    }

    fun showAdJLTwo(diaLog: LoadingDiaLog, activity: Activity?, tag:String= activity!!::class.java.simpleName, play:()->Unit) {
        if (AppConst.is_show_ad&&activity!=null) {
            if (!AntiRepeatClickUtils.isFastClickJL()) {
                return
            }
            AdRVTwoUtils.init(object : AdRVTwoUtils.GirdMenuStateListener {
                override fun showVideoClosed() {
                    LZYLog.e(tag, "main jl showVideoClosed")
                    if (diaLog.isShowing)
                        diaLog.dismiss()
                    play()
                    initAdJL(activity, tag)
                }

                override fun onShowError() {
                    LZYLog.e(tag, "main jl onLoadError")
                    if (diaLog.isShowing)
                        diaLog.dismiss()
                    play()
                }

                override fun onEarnRewards() {
                }

                override fun onLoadError() {
                    diaLog.dismiss()
                    state_jl_2 =2
                    play()
                }

                override fun onLoadSuccess() {
                    LZYLog.e(tag, "jl onLoadSuccess")
                    diaLog.dismiss()
                    state_jl_2 =1
                    AdRVTwoUtils.showRewardAd(activity)
                }
            }, activity)
            LZYLog.e(tag, "激励 进来了AdRVTwoUtils.isReady():" + AdRVTwoUtils.isReady())
            if (AdRVTwoUtils.isReady()) {
                AdRVTwoUtils.showRewardAd(activity)
            } else {
                if (((System.currentTimeMillis()- record_time_jl2) > intervalTime)|| state_jl_2 ==2) {
                    AdRVTwoUtils.initPreloading("")
                    state_jl_2 =0
                    record_time_jl2 =System.currentTimeMillis()
                }
            }
        } else {
            diaLog.dismiss()
            play()
        }
    }

    fun showAdJLTurn(diaLog: LoadingDiaLog, activity: Activity?, tag:String= activity!!::class.java.simpleName, play:()->Unit){
        LZYLog.e(tag, "showAdJL play ${AppConst.adsJLFlag}")
        when(AppConst.adsJLFlag){
            0 -> {
                showAdJL(diaLog,activity,tag, play)
            }
            1 -> {
                showAdJLTwo(diaLog,activity,tag,play)
            }
        }
        AppConst.adsJLFlag = (AppConst.adsJLFlag + 1) % 2
    }
}