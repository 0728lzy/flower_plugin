package com.weini.catdog.utils.dj;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.weini.catdog.APP;
import com.weini.catdog.AppConst;
import com.weini.catdog.helper.dj.PushHelper;
import com.umeng.commonsdk.utils.UMUtils;
import com.yl.adsdk.YlLib;

import java.util.Timer;
import java.util.TimerTask;

public class TimerInitSDK {
    private static String TAG = "TMediationSDK_DEMO_";
    private static Timer countdownTimer;
    private static Context myContext;

    public static void startCountdown(Context context) {
        myContext = context;
        if (countdownTimer == null ) {
            countdownTimer = new Timer();
            countdownTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if(isInstallAfterTime()){
                        stopCountDown();
                        initAllSDk();
                    }
                }
            }, 0, 5000);
        }
    }

    public static boolean isInstallAfterTime(){
        long installTime =  SPUtils.getInstance().getLong(SPUtils.SP_INSTALL_TIME);
        long curr = System.currentTimeMillis();
        Log.d(TAG, "TimerInitSDK: install passed  :" + (curr - installTime)/1000 + " s" );
        if(curr - installTime >= 60 * 60 * 1000 ){
            Log.d(TAG, "isInstallAfterHour: yes");
            return true;
        }
        return false;
    }

    public static void initAllSDk(){

        if(Build.VERSION.SDK_INT >= 33){
            return;
        }
        AppConst.AndroidId = DeviceInfoUtil.INSTANCE.getAndroidId(APP.instance);
         AppConst.riskInfo = YlLib.getRiskInfo(APP.instance);//设备异常标签，正常、代理、异常、模拟器、root、无SIM

        UserInfoModel.setIsFirstTime(false);
        GetHttpDataUtil.INSTANCE.getOutNetIP();
        if(!UserInfoModel.getIsCheckFlag() || UserInfoModel.getIsShowAd()) {
            APP.Companion.initAdSdk();
        }
        initUmeng();
        DeviceInfoUtil.INSTANCE.init(APP.instance,AppConst.INSTALL_FROM_APP);
    }


    //友盟初始化 已经同意
    private static void initUmeng() {
        //用户点击隐私协议同意按钮后，初始化PushSDK
//        PushHelper.init(applicationContext)
        //用户点击隐私协议同意按钮后，初始化PushSDKPushHelper.init(applicationContext)
        boolean isMainProcess = UMUtils.isMainProgress(APP.instance);
        if (isMainProcess) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    PushHelper.init(APP.instance);
                }
            }).start();

        } else {
            PushHelper.init(APP.instance);
        }
    }

    public static void stopCountDown(){
        if (countdownTimer != null) {
            countdownTimer.cancel();
            countdownTimer.purge();
            countdownTimer = null;
        }
    }

}
