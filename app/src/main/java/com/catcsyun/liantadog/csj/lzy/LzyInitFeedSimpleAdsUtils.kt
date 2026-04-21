package com.catcsyun.liantadog.csj.lzy

import android.app.Activity
import android.widget.FrameLayout
import com.catcsyun.liantadog.AppConst
import com.catcsyun.liantadog.utils.lzy.LZYLog

object LzyInitFeedSimpleAdsUtils {

    fun initSimpleAd3(activity: Activity,tag:String= activity::class.java.simpleName) {
        if (!AppConst.is_show_ad) {
            return
        }
        if (activity != null && AppConst.is_show_ad) {
            AdFeedSimpleInitThreeUtils.init(
                activity,
                object : AdFeedSimpleInitThreeUtils.GirdMenuStateListener {
                    override fun onLoadSuccess() {
                        LZYLog.e("zzzzz", " 1.信息流预加载 onSuccess")
                    }

                    override fun onLoadError() {
                        LZYLog.e("zzzzz", " 1.信息流预加载 onError")
                    }

                    override fun onShowSuccess() {
                    }
                })
            if (!AdFeedSimpleInitThreeUtils.isReady()) {
                LZYLog.e("zzzzz", " 0.信息流尝试预加载")
                AdFeedSimpleInitThreeUtils.initPreloading()
            }
        }
    }

    fun loadSimpleAd3(activity: Activity,tag:String= activity::class.java.simpleName,fragment: FrameLayout?) {
        if (!AppConst.is_show_ad) {
            return
        }
        if (activity != null && AppConst.is_show_ad) {
            AdFeedSimpleInitThreeUtils.init(
                activity,
                object : AdFeedSimpleInitThreeUtils.GirdMenuStateListener {
                    override fun onLoadSuccess() {
                        LZYLog.e("zzzzz", " 3.信息流预加载 onSuccess")
                        if (fragment != null && activity != null) {
                            AdFeedSimpleInitThreeUtils.showAd(fragment, activity)
                        }
                    }

                    override fun onLoadError() {
                        LZYLog.e("zzzzz", " 3.信息流预加载 onError")
                    }

                    override fun onShowSuccess() {
                        LZYLog.e("zzzzz", " 3.信息流展示")
                    }
                })
            if (AdFeedSimpleInitThreeUtils.isReady()) {
                LZYLog.e("zzzzz", " 2.直接展示信息流了")
                if (fragment != null && activity != null) {
                    AdFeedSimpleInitThreeUtils.showAd(fragment, activity)
                }
            }
            else {
                LZYLog.e("zzzzz", " 2.还是要申请信息流")
                AdFeedSimpleInitThreeUtils.initPreloading()
            }
        }
    }
}