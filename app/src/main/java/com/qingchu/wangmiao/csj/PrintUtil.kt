package com.qingchu.wangmiao.csj

import android.util.Log
import com.bytedance.sdk.openadsdk.mediation.manager.MediationBaseManager
import com.qingchu.wangmiao.AppConst


class PrintUtil {
    companion object {
        /**
         * 打印其他加载信息
         */
        fun printLoadInfo(adInfo: MediationBaseManager?) {
            adInfo?.let {
                val adLoadInfo = adInfo.adLoadInfo
                Log.i(AppConst.TAG, "--------------------- 广告加载信息 start ------------------------")
                Log.i(AppConst.TAG, "------ 广告加载信息 ")
                adLoadInfo.forEach { it ->
                    Log.i(
                        AppConst.TAG,
                        "代码位id = ${it.mediationRit} adnName = ${it.adnName} adType = ${it.adType}" +
                                " errCode = ${it.errCode} errMsg = ${it.errMsg}"
                    )
                }

                Log.i(AppConst.TAG, "------ 广告价格信息 ")
                val multiBiddingEcpm = adInfo.multiBiddingEcpm
                multiBiddingEcpm?.let {
                    it.forEach { item ->
                        Log.i(
                            AppConst.TAG,
                            "sdkName = ${item.sdkName} slotId = ${item.slotId} levelTag = ${item.levelTag}" +
                                    " ecpm = ${item.ecpm} reqBiddingType = ${item.reqBiddingType} errorMsg = ${item.errorMsg} requestId = ${item.requestId}"
                        )
                    }
                }

                Log.i(AppConst.TAG, "------ 最优广告价格信息 ")
                val bestEcpm = adInfo.bestEcpm
                bestEcpm?.let {
                    Log.i(
                        AppConst.TAG,
                        "sdkName = ${bestEcpm.sdkName} slotId = ${bestEcpm.slotId} levelTag = ${bestEcpm.levelTag}" +
                                " ecpm = ${bestEcpm.ecpm} reqBiddingType = ${bestEcpm.reqBiddingType} " +
                                "errorMsg = ${bestEcpm.errorMsg} requestId = ${bestEcpm.requestId}"
                    )
                }

                Log.i(AppConst.TAG, "------ 当前缓存池的全部信息 ")
                val cacheList = adInfo.cacheList
                cacheList?.let {
                    it.forEach { item ->
                        Log.i(
                            AppConst.TAG,
                            "sdkName = ${item.sdkName} slotId = ${item.slotId} levelTag = ${item.levelTag}" +
                                    " ecpm = ${item.ecpm} reqBiddingType = ${item.reqBiddingType} errorMsg = ${item.errorMsg} requestId = ${item.requestId}"
                        )
                    }
                }
                Log.i(AppConst.TAG, "--------------------- 广告加载信息 end ------------------------")
            }
        }

        /**
         * 打印展示
         */
        fun printShowInfo(adInfo: MediationBaseManager?) {
            adInfo?.let {
                Log.i(AppConst.TAG, "--------------------- 广告展示信息 start ------------------------")
                val showEcpm = adInfo.showEcpm
                if(showEcpm != null){
                    Log.i(
                        AppConst.TAG,
                        "sdkName = ${showEcpm.sdkName} slotId = ${showEcpm.slotId} levelTag = ${showEcpm.levelTag}" +
                                " ecpm = ${showEcpm.ecpm} reqBiddingType = ${showEcpm.reqBiddingType} " +
                                "errorMsg = ${showEcpm.errorMsg} requestId = ${showEcpm.requestId}"
                    )
                } else {
                    Log.i(AppConst.TAG, "showEcpm is null")
                }
                Log.i(AppConst.TAG, "--------------------- 广告加载信息 end ------------------------")
            }
        }
    }
}