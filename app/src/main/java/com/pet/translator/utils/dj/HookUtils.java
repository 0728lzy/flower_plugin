package com.pet.translator.utils.dj;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class HookUtils {

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static boolean isHook(Context context) {
        if (findHookAppName(context) || findHookAppFile() || findHookStack() ) {
            return true;
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private static boolean isHookOtherFrame(Context context )   {
         List<String > whiteList = new ArrayList<>();
        whiteList.add("com.android");
        whiteList.add("java.lang");
        whiteList.add("android");
        whiteList.add(context.getPackageName());

        try {
            throw new Exception("gg");
        } catch (Exception  e) {

            List<StackTraceElement> stackList = Arrays.asList(e.getStackTrace());
            for(int i=0;i<stackList.size();i++){
                StackTraceElement element = stackList.get(i);
                List<Boolean>  retList =  whiteList.stream().map(it->element.getClassName().startsWith(it)).collect(Collectors.toList());
                if(retList.contains(true)){
                    return true;
                }
            }

        }
        return false;
    }

    private static boolean findHookAppName(Context context) {
//        PackageManager packageManager = context.getPackageManager();
//        List<ApplicationInfo> applicationInfoList = packageManager
//                .getInstalledApplications(PackageManager.GET_META_DATA);
//
//        for (ApplicationInfo applicationInfo : applicationInfoList) {
//            if (applicationInfo.packageName.equals("de.robv.android.xposed.installer")) {
//                Log.wtf("HookDetection", "Xposed found on the system.");
//                return true;
//            }
//            if (applicationInfo.packageName.equals("com.saurik.substrate")) {
//                Log.wtf("HookDetection", "Substrate found on the system.");
//                return true;
//            }
//        }
        return false;
    }

    private static boolean findHookAppFile() {
        try {
            Set<String> libraries = new HashSet<String>();
            String mapsFilename = "/proc/" + android.os.Process.myPid() + "/maps";
            BufferedReader reader = new BufferedReader(new FileReader(mapsFilename));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.endsWith(".so") || line.endsWith(".jar")) {
                    int n = line.lastIndexOf(" ");
                    libraries.add(line.substring(n + 1));
                }
            }
            reader.close();
            for (String library : libraries) {
                if (library.contains("com.saurik.substrate")) {
                    Log.wtf("HookDetection", "Substrate shared object found: " + library);
                    return true;
                }
                if (library.contains("XposedBridge.jar")) {
                    Log.wtf("HookDetection", "Xposed JAR found: " + library);
                    return true;
                }
            }
        } catch (Exception e) {
            Log.wtf("HookDetection", e.toString());
        }
        return false;
    }


    private static boolean findHookStack() {
        try {
            throw new Exception("findhook");
        } catch (Exception e) {

            // 读取栈信息
            // for(StackTraceElement stackTraceElement : e.getStackTrace()) {
            // Log.wtf("HookDetection", stackTraceElement.getClassName() + "->"+
            // stackTraceElement.getMethodName());
            // }

            int zygoteInitCallCount = 0;
            for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                if (stackTraceElement.getClassName().equals("com.android.internal.os.ZygoteInit")) {
                    zygoteInitCallCount++;
                    if (zygoteInitCallCount == 2) {
                        Log.wtf("HookDetection", "Substrate is active on the device.");
                        return true;
                    }
                }
                if (stackTraceElement.getClassName().equals("com.saurik.substrate.MS$2")
                        && stackTraceElement.getMethodName().equals("invoked")) {
                    Log.wtf("HookDetection", "A method on the stack trace has been hooked using Substrate.");
                    return true;
                }
                if (stackTraceElement.getClassName().equals("de.robv.android.xposed.XposedBridge")
                        && stackTraceElement.getMethodName().equals("main")) {
                    Log.wtf("HookDetection", "Xposed is active on the device.");
                    return true;
                }
                if (stackTraceElement.getClassName().equals("de.robv.android.xposed.XposedBridge")
                        && stackTraceElement.getMethodName().equals("handleHookedMethod")) {
                    Log.wtf("HookDetection", "A method on the stack trace has been hooked using Xposed.");
                    return true;
                }
            }
        }
        return false;
    }



}
