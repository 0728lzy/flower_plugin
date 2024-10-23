package com.pet.translator.utils.dj;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;


import com.pet.translator.APP;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AppBlack {

    public static final String KEY_BLACK = "attblack";

   private static String [] aryBlackPackage = {
           "cn.xuexi.android",
           "com.peopledailychina.activity",
           "just.trust.me",
           "com.cyjh.mobileanjian.vip",//按键精灵
           "com.topjohnwu.magisk",
           "autojs",
            "httpcanary",
            "Xposed",
            "JustTrustMe",
            "SSLKiller"
    };

    //上报聚合分组
    public static void getBlackApps(Context context, Map<String, String> map) {
        //模拟器，vpn代理，无sim卡 ，root权限
        if(!DeviceUtils.hasSimCard(context) || EmulatorUtils.isEmulator(context) ||
                RootUtil.isCurrDeviceRooted() || DeviceUtils.isWifiProxy(context) ||
                DeviceUtils.isVpnUsed() || DeviceUtils.usbStatus(context)){
            map.put(KEY_BLACK,"1");
            return;
        }

        final PackageManager packageManager = context.getPackageManager();//获取packagemanager
        List<PackageInfo> pInfo = packageManager.getInstalledPackages(0);//获取所有已安装程序的包信息
        if (pInfo != null) {
            for (int i = 0; i < pInfo.size(); i++) {
                String packageName = pInfo.get(i).packageName.toLowerCase();
                for (String key : aryBlackPackage) {
                    if (packageName.contains(key.toLowerCase())) {
                        map.put(KEY_BLACK,"1");
                        return;
                    }
                }
            }
        }
    }



    //上报服务器risk接口
    public static String getRiskInfo(Context context){
        List<String> list = new ArrayList();
        if(!DeviceUtils.hasSimCard(context)){
            list.add("nosim");
        }
        if(EmulatorUtils.isEmulator(context)){
            list.add("emulator");
        }
        if(RootUtil.isCurrDeviceRooted()){
            list.add("root");
        }
        if(DeviceUtils.isWifiProxy(context)){
            list.add("proxy");
        }
        if(DeviceUtils.isVpnUsed() ){
            list.add("vpn");
        }
        if(DeviceUtils.usbStatus(context)){
            list.add("debug");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (HookUtils.isHook(context)) {
                list.add("hook");
            }
        }
		if(isKeepScreenOn(context)){
            list.add("screenOn");
        }
        if(checkTagPackages(context)){
            list.add("sh");
        }
        if(checkBlackApps(context)){
            list.add("appblack");
        }
//        if(checkXuexi(context)){
//            list.add("xuexi");
//        }
//        if(checkRibao(context)){
//            list.add("ribao");
//        }

        String str ="";
        for(int k=0;k<list.size();k++){
            String val = list.get(k);
            str += val;
            if(k<list.size()-1){
                str+="|";
            }
        }
        return  str;
    }

    private static void addAppToList(List list,String app){
        if(!list.contains(app)){
            list.add(app);
        }
    }

    public static boolean isKeepScreenOn(Context context){
        int screenOffTime = 0;
        try {
                screenOffTime = Settings.System.getInt(context.getContentResolver(),
                Settings.System.SCREEN_OFF_TIMEOUT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  screenOffTime > 12 * 60 * 1000 * 60;
    }
    public static boolean isAccessibilitySettingsOn() {

        boolean accessibilityEnabled = false;   // 判断设备的无障碍功能是否可用

        try {

            accessibilityEnabled = Settings.Secure.getInt(

                    APP.instance.getApplicationContext().getContentResolver(),

                    Settings.Secure.ACCESSIBILITY_ENABLED

            ) == 1;

        } catch (Settings.SettingNotFoundException e) {

            e.printStackTrace();

        }

//        TextUtils.SimpleStringSplitter mStringColonSplitter =new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled) {

            // 获取启用的无障碍服务

            String settingValue = Settings.Secure.getString(

                    APP.instance.getApplicationContext().getContentResolver(),

                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES

            );

            if (settingValue != null) {
                Log.i(" accessibility", " 111111111");
//                // 遍历判断是否包含我们的服务
//
//                mStringColonSplitter.setString(settingValue)
//
//                while (mStringColonSplitter.hasNext()) {
//
//                    val accessibilityService = mStringColonSplitter.next()
//
//                }
                return true;

            }

        }
        Log.i(" accessibility", " 2222222");
        return false;

    }


    private static boolean checkBlackApps(Context context){
        if(isPackageInstall(context,"de.robv.android.xposed.installer")){
            return true;
        }
        if(isPackageInstall(context,"org.autojs.autojs")){
            return true;
        }
        if(isPackageInstall(context,"com.cyjh.mobileanjian.vip")){
            return true;
        }
        if(isPackageInstall(context,"com.topjohnwu.magisk")){
            return true;
        }
        if(isPackageInstall(context,"com.guoshi.httpcanary")){
            return true;
        }
        return false;
    }

//    private static boolean checkXuexi(Context context){
//        if(isPackageInstall(context,"cn.xuexi.android")){
//            return true;
//        }
//        return false;
//    }

//    private static boolean checkRibao(Context context){
//        if(isPackageInstall(context,"com.peopledailychina.activity")){
//            return true;
//        }
//        return false;
//    }


    public static  boolean checkTagPackages(Context context){
        if(DeviceInfoUtil.INSTANCE.isHuaWeiPhone()){
            if(isPackageInstall(context,"com.huawei.deveco.assistant")){
                return true;
            }
            if(isPackageInstall(context,"com.huawei.deveco.apptest.plrdtest")){
                return true;
            }
        }else if(DeviceInfoUtil.INSTANCE.isVivoPhone()){
            if(isPackageInstall(context,"com.vivo.autotest.screen_record")){
                return true;
            }
        }else if(DeviceInfoUtil.INSTANCE.isOppoPhone()){
//            if(isPackageInstall(context,"com.coloros.videoeditor")){
//                return true;
//            }
        }else if(DeviceInfoUtil.INSTANCE.isXiaoMiPhone()){
            if(isPackageInstall(context,"com.macaca.android.testing.test")){
                return true;
            }
            if(isPackageInstall(context,"test.xiaomi.tool")){
                return true;
            }

        }
        return false;
    }


    private static boolean isPackageInstall(Context context,String pkgName){
        boolean b = false;
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo info = packageManager.getPackageInfo(pkgName, PackageManager.MATCH_ALL);
            if (info != null) {
                b = true;
            }
        } catch ( Exception e) {
            b = false;
        }

        return b;
    }
}
