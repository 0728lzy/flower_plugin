package com.cslt.maogoufanyi.utils.dj

import android.annotation.SuppressLint
import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.cslt.maogoufanyi.AppConst
import com.cslt.maogoufanyi.bean.dj.AdIdBean

object AdDynamicUtils {

    val TAG = "AdDynamicUtils"


//    {
//        1: "信息流广告",
//        2: "Banner(横幅)"
//        3: "开屏"
//        4: "插屏"
//        5: "激励视频"
//        6: "全屏视频"
//        7: "draw信息流"
//        10:"插全屏"
//    }

    var CPIndex = -1
    var JLIndex = -1
    var XXLIndex = -1
    var KPIndex = -1



    fun setAdInfo(adUnitJson: String) {
         CPIndex = 0   //插屏下标
         JLIndex = 0    //激励下标
         XXLIndex = 0   //信息流下标
         KPIndex = 0    //开屏下标
        if(!TextUtils.isEmpty(adUnitJson)) {
            val listType = object : TypeToken<List<AdIdBean>>() {}.type
            var adsList = Gson().fromJson<List<AdIdBean>>(adUnitJson, listType)
            for (item in adsList){
//                Log.e(TAG,"获取广告id信息："+Gson().toJson(item))
                AppConst.Ad_ID = item.app_id.toString();
                if(item.status == 1L){

                   if(item.ad_unit_type.equals("1")){
                        //赋值信息流
                       if(XXLIndex == 0){
                           AppConst.FEEDSIMPLE_ID_ONE = item.ad_unit_id.toString()
                           XXLIndex++;
                       }else if(XXLIndex == 1){
                           AppConst.FEEDSIMPLE_ID_TWO = item.ad_unit_id.toString()
                           XXLIndex++;
                       }else if(XXLIndex == 2){
                           AppConst.FEEDSIMPLE_ID_THREE = item.ad_unit_id.toString()
                           XXLIndex++;
                       }else if(XXLIndex == 3){
                           AppConst.FEEDSIMPLE_ID_FOUR = item.ad_unit_id.toString()
                           XXLIndex++;
                       }
                   }else if(item.ad_unit_type.equals("5")){
                       //激励
                       if(JLIndex == 0){
                           AppConst.GMRDAd_ID_IN = item.ad_unit_id.toString()
                           JLIndex++
                       } else if(JLIndex == 1){
                           AppConst.GMRDAd_ID_TWO = item.ad_unit_id.toString()
                           JLIndex++
                       }
                   }else if(item.ad_unit_type.equals("10")){
                       //插屏
                       if(CPIndex == 0){
                           AppConst.GMCPAd_ID_IN = item.ad_unit_id.toString()
                           CPIndex++
                       }else if(CPIndex == 1){
                           AppConst.GMCPAd_ID_IN_TWO = item.ad_unit_id.toString()
                           CPIndex++
                       }else if(CPIndex == 2){
                           AppConst.GMCPAd_THREE_ID_IN = item.ad_unit_id.toString()
                           CPIndex++
                       }else if(CPIndex == 3){
                           AppConst.GMCPAd_FOUR_ID_IN = item.ad_unit_id.toString()
                           CPIndex++
                       }
                   }else if(item.ad_unit_type.equals("3")){
                       //开屏
                       if(KPIndex == 0){
                           AppConst.GMSPAd_ID = item.ad_unit_id.toString()
                           KPIndex++
                       }else if(KPIndex == 1){
                           AppConst.GMSPAd_TWO_ID = item.ad_unit_id.toString()
                           KPIndex++
                       }
                   }
                }
            }
            var adJson = Gson().toJson(adsList)
            outputAdInfo()
            UserInfoModel.setSaveAdJson(adJson)
        }
    }


//    @JvmField var  Ad_ID = "" //穿山甲广告APP ID
//    @JvmField var  GMCPAd_ID_IN = "" //插屏应用内
//    @JvmField var  GMCPAd_ID_IN_TWO = "" //插屏应用内2
//    @JvmField var  GMCPAd_THREE_ID_IN = "" //插屏应用内 3
//    @JvmField var  GMCPAd_FOUR_ID_IN = "" //插屏应用内 4
//    @JvmField var  FEEDSIMPLE_ID_ONE = "" //信息流首页1
//    @JvmField var  FEEDSIMPLE_ID_TWO = "" //信息流首页2
//    @JvmField var  FEEDSIMPLE_ID_THREE = "" //信息流首页3
//    @JvmField var  FEEDSIMPLE_ID_FOUR = "" //信息流首页4
//    @JvmField var  GMSPAd_ID = "" //开屏ID
//    @JvmField var  GMSPAd_TWO_ID = "" //开屏ID 2
//    @JvmField var  GMRDAd_ID_IN = "" //激励视频


    private fun outputAdInfo(){
//        Log.e(TAG,"穿山甲id:${AppConst.Ad_ID}")
//        Log.e(TAG,"插屏1:${AppConst.GMCPAd_ID_IN}")
//        Log.e(TAG,"插屏2:${AppConst.GMCPAd_ID_IN_TWO}")
//        Log.e(TAG,"插屏3:${AppConst.GMCPAd_THREE_ID_IN}")
//        Log.e(TAG,"插屏4:${AppConst.GMCPAd_FOUR_ID_IN}")
//        Log.e(TAG,"信息流1:${AppConst.FEEDSIMPLE_ID_ONE}")
//        Log.e(TAG,"信息流2:${AppConst.FEEDSIMPLE_ID_TWO}")
//        Log.e(TAG,"信息流3:${AppConst.FEEDSIMPLE_ID_THREE}")
//        Log.e(TAG,"信息流4:${AppConst.FEEDSIMPLE_ID_FOUR}")
//        Log.e(TAG,"开屏1:${AppConst.GMSPAd_ID}")
//        Log.e(TAG,"开屏2:${AppConst.GMSPAd_TWO_ID}")
//        Log.e(TAG,"激励:${AppConst.GMRDAd_ID_IN}")

    }

    @SuppressLint("SuspiciousIndentation")
    fun getAdInto(){
      var adJson =  UserInfoModel.getSaveAdJson()
        if(!TextUtils.isEmpty(adJson)) {
            setAdInfo(adJson)
        }
    }


}