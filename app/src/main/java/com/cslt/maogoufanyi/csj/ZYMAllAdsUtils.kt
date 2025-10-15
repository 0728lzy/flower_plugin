package com.cslt.maogoufanyi.csj

import android.app.Activity
import android.util.Log
import android.widget.FrameLayout
import com.cslt.maogoufanyi.AppConst
import com.cslt.maogoufanyi.utils.dj.AntiRepeatClickUtils
import com.cslt.maogoufanyi.utils.dj.UserInfoModel
import com.cslt.maogoufanyi.utils.lzy.LZYLog
import com.cslt.maogoufanyi.widget.dialog.LoadingDiaLog


object ZYMAllAdsUtils {
    //插屏

    //记录间隔时间
    var record_time_cp1 = 0L
    var record_time_cp2 = 0L
    var record_time_cp3 = 0L
    var record_time_cp4 = 0L

    //最大间隔时间
    var intervalTime_cp = 5000;

    //加载状态
    var state_cp_1=0//0代表加载中，1代表加载成功，2代表加载失败
    var state_cp_2=0//0代表加载中，1代表加载成功，2代表加载失败
    var state_cp_3=0//0代表加载中，1代表加载成功，2代表加载失败
    var state_cp_4=0//0代表加载中，1代表加载成功，2代表加载失败


    //adv-预加载广告--------------------------------------------------start
    fun initAdCp1(activity: Activity, tag:String= activity::class.java.simpleName){
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
            if (((System.currentTimeMillis()- record_time_cp1) > intervalTime_cp)|| state_cp_1 ==2) {
                LZYLog.i(tag,"预加载插屏1")
                AdCPUtils.initPreloading()
                state_cp_1 =0
                record_time_cp1 =System.currentTimeMillis()
            }
        }
    }
    fun initAdCp2(activity: Activity, tag:String= activity::class.java.simpleName){
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
            if (((System.currentTimeMillis()- record_time_cp2) > intervalTime_cp)|| state_cp_2 ==2) {
                LZYLog.i(tag,"预加载插屏2")
                AdCPTwoUtils.initPreloading()
                state_cp_2 =0
                record_time_cp2 =System.currentTimeMillis()
            }
        }
    }
    fun initAdCp3(activity: Activity, tag:String= activity::class.java.simpleName){
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
            if (((System.currentTimeMillis()- record_time_cp3) > intervalTime_cp)|| state_cp_3 ==2) {
                LZYLog.i(tag,"预加载插屏3")
                AdCPThreeUtils.initPreloading()
                state_cp_3 =0
                record_time_cp3 =System.currentTimeMillis()
            }
        }
    }
    fun initAdCp4(activity: Activity, tag:String= activity::class.java.simpleName){
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
            if (((System.currentTimeMillis()- record_time_cp4) > intervalTime_cp)|| state_cp_4 ==2) {
                LZYLog.i(tag,"预加载插屏4")
                AdCPFourUtils.initPreloading()
                state_cp_4 =0
                record_time_cp4 =System.currentTimeMillis()
            }
        }
    }
    //adv-预加载广告--------------------------------------------------end
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
            if (((System.currentTimeMillis()- record_time_cp1) > intervalTime_cp)|| state_cp_1 ==2) {
                AdCPUtils.initPreloading()
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
            if (((System.currentTimeMillis()- record_time_cp2) > intervalTime_cp)|| state_cp_2 ==2) {
                AdCPTwoUtils.initPreloading()
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
            if (((System.currentTimeMillis()- record_time_cp3) > intervalTime_cp)|| state_cp_3 ==2) {
                AdCPThreeUtils.initPreloading()
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
            if (((System.currentTimeMillis()- record_time_cp4) > intervalTime_cp)|| state_cp_4 ==2) {
                AdCPFourUtils.initPreloading()
                state_cp_4 =0
                record_time_cp4 =System.currentTimeMillis()
            }
        } else {
            AdCPFourUtils.showInterstitialFullAd(activity)
        }
    }

    private val cpList = mutableListOf<Int>() //这个是用来来给我们存调取插屏的次序的
    private var currentIndex = 0 //当前轮到了哪一个索引

    fun showAdCpTurnTab(activity: Activity,tag:String= activity::class.java.simpleName) {
        if (cpList.isEmpty() || currentIndex >= cpList.size) {  //如果是空的，说明需要重新填充次序；或者是如果看完一轮了，说明要清空然后重新填次序
            cpList.clear()
            cpList.addAll(listOf(0, 1).shuffled())
            currentIndex = 0
        }
        when (cpList[currentIndex]) {
            0 -> {
                showAdCp3(activity,tag)
                showAdCp4(activity,tag)
            }
            1 -> {
                showAdCp1(activity,tag)
                showAdCp2(activity,tag)
            }
        }
        currentIndex++
    }

    private val cpList2 = mutableListOf<Int>() //这个是用来来给我们存调取插屏的次序的
    private var currentIndex2 = 0 //当前轮到了哪一个索引

    fun showACpTurnNormal(activity: Activity,tag:String= activity::class.java.simpleName) {
        if (cpList2.isEmpty() || currentIndex2 >= cpList2.size) {  //如果是空的，说明需要重新填充次序；或者是如果看完一轮了，说明要清空然后重新填次序
            cpList2.clear()
            cpList2.addAll(listOf(0, 1, 2,3).shuffled())
            currentIndex2 = 0
        }
        when (cpList2[currentIndex2]) {
            0 -> {
                showAdCp1(activity,tag)
            }
            1 -> {
                showAdCp2(activity,tag)
            }
            2 -> {
                showAdCp3(activity,tag)
            }
            3 -> {
                showAdCp4(activity,tag)
            }
        }
        currentIndex2++
    }
    //adv加载广告---------------------------------------------------start


    //信息流




    //激励视频
    //记录间隔时间
    var record_time_jl1 = 0L
    var record_time_jl2 = 0L

    //最大间隔时间
    var intervalTime_jl = 5000;

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
                if (((System.currentTimeMillis()- record_time_jl1) > intervalTime_jl)|| state_jl_1 ==2) {
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
                if (((System.currentTimeMillis()- record_time_jl2) > intervalTime_jl)|| state_jl_2 ==2) {
                    AdRVTwoUtils.initPreloading("")
                    state_jl_2 =0
                    record_time_jl2 =System.currentTimeMillis()
                }
            }
        }
    }

    fun showAdJL(activity: Activity?, tag: String = activity?.javaClass?.simpleName ?: "", play: () -> Unit) {
        if (AppConst.is_show_ad && activity != null && !activity.isFinishing && !activity.isDestroyed) {

            if (!AntiRepeatClickUtils.isFastClickJL()) {
                return
            }

            val dialog = LoadingDiaLog(activity,"加载中...")
            dialog.show()

            AdRVUtils.init(object : AdRVUtils.GirdMenuStateListener {
                override fun showVideoClosed() {
                    LZYLog.e(tag, "main jl showVideoClosed")
                    if (dialog.isShowing) dialog.dismiss()
                    play()
                    initAdJL2(activity, tag)
                }

                override fun onShowError() {
                    LZYLog.e(tag, "main jl onLoadError")
                    if (dialog.isShowing) dialog.dismiss()
                    play()
                }

                override fun onEarnRewards() {}

                override fun onLoadError() {
                    if (dialog.isShowing) dialog.dismiss()
                    state_jl_1 = 2
                    play()
                }

                override fun onLoadSuccess() {
                    LZYLog.e(tag, "jl onLoadSuccess")
                    if (dialog.isShowing) dialog.dismiss()
                    state_jl_1 = 1
                    AdRVUtils.showRewardAd(activity)
                }
            }, activity)

            LZYLog.e(tag, "激励 进来了AdRVUtils.isReady():" + AdRVUtils.isReady())
            if (AdRVUtils.isReady()) {
                if (dialog.isShowing) dialog.dismiss()
                AdRVUtils.showRewardAd(activity)
            } else {
                if (((System.currentTimeMillis() - record_time_jl1) > intervalTime_jl) || state_jl_1 == 2) {
                    AdRVUtils.initPreloading("")
                    state_jl_1 = 0
                    record_time_jl1 = System.currentTimeMillis()
                }
            }
        } else {
            play()
        }
    }

    fun showAdJLTwo(activity: Activity?, tag: String = activity?.javaClass?.simpleName ?: "", play: () -> Unit) {
        if (AppConst.is_show_ad && activity != null && !activity.isFinishing && !activity.isDestroyed) {

            if (!AntiRepeatClickUtils.isFastClickJL()) {
                return
            }

            val dialog = LoadingDiaLog(activity,"加载中...")
            dialog.show()

            AdRVTwoUtils.init(object : AdRVTwoUtils.GirdMenuStateListener {
                override fun showVideoClosed() {
                    LZYLog.e(tag, "main jl showVideoClosed")
                    if (dialog.isShowing) dialog.dismiss()
                    play()
                    initAdJL(activity, tag)
                }

                override fun onShowError() {
                    LZYLog.e(tag, "main jl onLoadError")
                    if (dialog.isShowing) dialog.dismiss()
                    play()
                }

                override fun onEarnRewards() {}

                override fun onLoadError() {
                    if (dialog.isShowing) dialog.dismiss()
                    state_jl_2 = 2
                    play()
                }

                override fun onLoadSuccess() {
                    LZYLog.e(tag, "jl onLoadSuccess")
                    if (dialog.isShowing) dialog.dismiss()
                    state_jl_2 = 1
                    AdRVTwoUtils.showRewardAd(activity)
                }
            }, activity)

            LZYLog.e(tag, "激励 进来了AdRVTwoUtils.isReady():" + AdRVTwoUtils.isReady())
            if (AdRVTwoUtils.isReady()) {
                if (dialog.isShowing) dialog.dismiss()
                AdRVTwoUtils.showRewardAd(activity)
            } else {
                if (((System.currentTimeMillis() - record_time_jl2) > intervalTime_jl) || state_jl_2 == 2) {
                    AdRVTwoUtils.initPreloading("")
                    state_jl_2 = 0
                    record_time_jl2 = System.currentTimeMillis()
                }
            }
        } else {
            play()
        }
    }

    private val jlList = mutableListOf<Int>() //这个是用来来给我们存调取激励视频的次序的
    private var currentIndex_jl = 0 //当前轮到了哪一个索引

    fun showAdJLTurn(activity: Activity?, tag:String= activity!!::class.java.simpleName, play:()->Unit) {
        if (jlList.isEmpty() || currentIndex_jl >= jlList.size) {  //如果是空的，说明需要重新填充次序；或者是如果看完一轮了，说明要清空然后重新填次序
            jlList.clear()
            jlList.addAll(listOf(0, 1).shuffled())
            currentIndex_jl = 0
        }
        when (jlList[currentIndex_jl]) {
            0 -> {
                showAdJL(activity,tag, play)
            }
            1 -> {
                showAdJLTwo(activity,tag,play)
            }
        }
        currentIndex_jl++
    }

    fun loadSimpleNoLimitAd1(activity: Activity?, tag:String= activity!!::class.java.simpleName,fragment: FrameLayout?) {
        if (activity != null&&(!UserInfoModel.getIsCheckFlag()||AppConst.is_show_ad)) {
            AdFeedSimpleOneNoLimitUtils.init(activity, object : AdFeedSimpleOneNoLimitUtils.GirdMenuStateListener {
                override fun onSuccess() {
                    LZYLog.e(tag, " AdFeedSimpleOneUtils onSuccess")
                    if (fragment != null&&activity!=null) {
                        Log.i("tttt","准备刷新视频列表的小信息流")
                        AdFeedSimpleOneNoLimitUtils.showAd(fragment, activity)
                    }
                }

                override fun onError() {
                    LZYLog.e(tag, " loadSimpleAdOne onError")
                }
            })
            AdFeedSimpleOneNoLimitUtils.initPreloading()
        }
    }

    fun loadSimpleAd1(activity: Activity?, tag:String= activity!!::class.java.simpleName,fragment: FrameLayout?) {
        if (activity != null && AppConst.is_show_ad) {
            AdFeedSimpleOneUtils.init(activity, object : AdFeedSimpleOneUtils.GirdMenuStateListener {
                override fun onSuccess() {
                    LZYLog.e(tag, " AdFeedSimpleOneUtils onSuccess")
                    if (fragment != null&&activity!=null) {
                        Log.i("tttt","准备刷新视频列表的小信息流")
                        AdFeedSimpleOneUtils.showAd(fragment, activity)
                    }
                }

                override fun onError() {
                    LZYLog.e(tag, " loadSimpleAdOne onError")
                }
            })
            AdFeedSimpleOneUtils.initPreloading()
        }
    }

    fun loadSimpleAd2(activity: Activity?, tag:String= activity!!::class.java.simpleName,fragment: FrameLayout?) {
        if (activity != null && AppConst.is_show_ad) {
            AdFeedSimpleTwoUtils.init(activity, object : AdFeedSimpleTwoUtils.GirdMenuStateListener {
                override fun onSuccess() {
                    LZYLog.e(tag, " AdFeedSimpleTwoUtils onSuccess")
                    if (fragment != null&&activity!=null) {
                        LZYLog.e(tag, " AdFeedSimpleTwoUtils onShowing")
                        AdFeedSimpleTwoUtils.showAd(fragment, activity)
                    }
                }

                override fun onError() {
                    LZYLog.e(tag, " loadSimpleAdOne onError")
                }
            })
            AdFeedSimpleTwoUtils.initPreloading("")
        }
    }

    fun loadSimpleAd3(activity: Activity?, tag:String= activity!!::class.java.simpleName,fragment: FrameLayout?) {
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

    fun loadSimpleAd4(activity: Activity?, tag:String= activity!!::class.java.simpleName,fragment: FrameLayout?) {
        if (activity != null && AppConst.is_show_ad) {
            AdFeedSimpleFourUtils.init(activity, object : AdFeedSimpleFourUtils.GirdMenuStateListener {
                override fun onSuccess() {
                    if (fragment != null&&activity!=null) {
                        AdFeedSimpleFourUtils.showAd(fragment, activity)
                    }
                }

                override fun onError() {
                }
            })
            AdFeedSimpleFourUtils.initPreloading("")
        }
    }

    private val simpleList = mutableListOf<Int>() //这个是用来来给我们存调取信息流的次序的
    private var currentIndex_simple = 0 //当前轮到了哪一个索引

    fun loadSimpleSmall(activity: Activity?, tag:String= activity!!::class.java.simpleName,fragment: FrameLayout?) {
        if (simpleList.isEmpty() || currentIndex_simple >= simpleList.size) {  //如果是空的，说明需要重新填充次序；或者是如果看完一轮了，说明要清空然后重新填次序
            simpleList.clear()
            simpleList.addAll(listOf(0, 1).shuffled())
            currentIndex_simple = 0
        }
        when (simpleList[currentIndex_simple]) {
            0 -> {
                loadSimpleAd3(activity,tag,fragment)
            }
            1 -> {
                loadSimpleAd4(activity,tag,fragment)
            }
        }
        currentIndex_simple++
    }

    private val simpleList2 = mutableListOf<Int>() //这个是用来来给我们存调取信息流的次序的
    private var currentIndex_simple2 = 0 //当前轮到了哪一个索引

    fun loadSimpleBig(activity: Activity?, tag:String= activity!!::class.java.simpleName,fragment: FrameLayout?) {
        if (simpleList2.isEmpty() || currentIndex_simple2 >= simpleList2.size) {  //如果是空的，说明需要重新填充次序；或者是如果看完一轮了，说明要清空然后重新填次序
            simpleList2.clear()
            simpleList2.addAll(listOf(0, 1).shuffled())
            currentIndex_simple2 = 0
        }
        when (simpleList2[currentIndex_simple2]) {
            0 -> {
                loadSimpleAd1(activity,tag,fragment)
            }
            1 -> {
                loadSimpleAd2(activity,tag,fragment)
            }
        }
        currentIndex_simple2++
    }

    private val simpleList3 = mutableListOf<Int>() //这个是用来来给我们存调取信息流的次序的
    private var currentIndex_simple3 = 0 //当前轮到了哪一个索引

    fun loadSimpleAll(activity: Activity?, tag:String= activity!!::class.java.simpleName,fragment: FrameLayout?) {
        if (simpleList3.isEmpty() || currentIndex_simple3 >= simpleList3.size) {  //如果是空的，说明需要重新填充次序；或者是如果看完一轮了，说明要清空然后重新填次序
            simpleList3.clear()
            simpleList3.addAll(listOf(0, 1,2,3).shuffled())
            currentIndex_simple3 = 0
        }
        when (simpleList3[currentIndex_simple3]) {
            0 -> {
                loadSimpleAd1(activity,tag,fragment)
            }
            1 -> {
                loadSimpleAd2(activity,tag,fragment)
            }
            2 -> {
                loadSimpleAd3(activity,tag,fragment)
            }
            3 -> {
                loadSimpleAd4(activity,tag,fragment)
            }
        }
        currentIndex_simple3++
    }

}