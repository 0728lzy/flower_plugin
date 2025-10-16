package com.cslt.maogoufanyi.utils.dj;

import static android.content.Context.BATTERY_SERVICE;
import static android.os.Build.UNKNOWN;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.cslt.maogoufanyi.APP;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;

public class DeviceUtils {


    /**
     * 获取手机IMEI号
     */
    public static String getIMEI(Context context) {
        String deviceId = "";
        try {
            deviceId = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
//            Log.e("LHM", "getIMEI: "+ deviceId);
//            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
//                if (telephonyManager.getDeviceId() != null) {
//                    deviceId = telephonyManager.getDeviceId();
//                } else {
//                    deviceId = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
//                }
//            }else{
//                deviceId = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (deviceId.length() == 0) {
            Log.e("LHM", "getIMEI:  return null");
        }
        return deviceId;
    }


    //AppVersion
    public static String getVersionName(Context context) {
        String version = "1";
        // 获取packagemanager的实例
        try {
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int versionCode;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                versionCode = (int) packInfo.getLongVersionCode(); // avoid huge version numbers and you will be ok
            } else {
                versionCode = packInfo.versionCode;
            }
            version = packInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return version;
    }

    /**
     * 获取产品 / 硬件品牌信息
     *
     * @return 产品 / 硬件品牌信息
     */
    public static String getBrand() {
        try {
            String brand = Build.BRAND;
            if (!TextUtils.isEmpty(brand)) {
                return brand.toLowerCase();
            }
        } catch (Throwable ignore) {
        }
        return UNKNOWN;
    }

    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getSystemModel() {
        String model = Build.MODEL;
        return model;
    }

    public static String getIPAddress(Context context) {
        try {
            @SuppressLint("MissingPermission") NetworkInfo info = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                    String ip = getLanIp(context);
                    return ip;
                }
            } else {
                //当前无网络连接,请在设置中打开网络
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }


        return "0.0.0.0";
    }

    public static String getLanIp(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        String ipAddress = (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
        ;//得到IPV4地址

        return ipAddress;
    }

    /**
     * 获取外网ip地址的方法2
     *
     * @return
     */
    public static String getNetIp() {
        URL infoUrl = null;
        InputStream inStream = null;
        String line = null;
        try {
            infoUrl = new URL("https://httpbin.org/ip"); //json格式信息的API，使用案例。
            URLConnection connection = infoUrl.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inStream = httpConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "utf-8"));
                StringBuilder strber = new StringBuilder();
                while ((line = reader.readLine()) != null)
                    strber.append(line + "\n");
                inStream.close();
                // 从反馈的结果中提取出IP地址
                int start = strber.indexOf("{");
                int end = strber.indexOf("}");
                String json = strber.substring(start, end + 1);
                if (json != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        line = jsonObject.optString("origin");
//                        System.out.println("IP：" + line);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return line;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line;
    }

    public static String getNetIp2() {
        String[] platforms = {
                "http://pv.sohu.com/cityjson",
        };

        BufferedReader buff = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(platforms[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(5000);//读取超时
            urlConnection.setConnectTimeout(5000);//连接超时
            urlConnection.setDoInput(true);
            urlConnection.setUseCaches(false);

            int responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {//找到服务器的情况下,可能还会找到别的网站返回html格式的数据
                InputStream is = urlConnection.getInputStream();
                buff = new BufferedReader(new InputStreamReader(is, "UTF-8"));//注意编码，会出现乱码
                StringBuilder builder = new StringBuilder();
                String line = null;
                while ((line = buff.readLine()) != null) {
                    builder.append(line);
                }

                buff.close();//内部会关闭 InputStream
                urlConnection.disconnect();

                Log.e("LHM", builder.toString());

                //截取字符串
                int satrtIndex = builder.indexOf("{");//包含[
                int endIndex = builder.indexOf("}");//包含]
                String json = builder.substring(satrtIndex, endIndex + 1);//包含[satrtIndex,endIndex)
                JSONObject jo = new JSONObject(json);
                String ip = jo.getString("cip");

                return ip;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    /**
     * 检测当的网络（WLAN、3G/2G）状态
     *
     * @param context Context
     * @return true 表示网络可用
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            @SuppressLint("MissingPermission") NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }


    public static int getBatteryLevel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BatteryManager batteryManager = (BatteryManager) context.getSystemService(BATTERY_SERVICE);
            return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        } else {
            Intent intent = new ContextWrapper(APP.Companion.getInstance()).
                    registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            return (intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) * 100) /
                    intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        }
    }


    /*
     * 判断设备 是否使用代理上网
     * */
    public static boolean isWifiProxy(Context context) {
// 是否大于等于4.0
        final boolean IS_ICS_OR_LATER = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
        String proxyAddress;
        int proxyPort;
        if (IS_ICS_OR_LATER) {
            proxyAddress = System.getProperty("http.proxyHost");
            String portStr = System.getProperty("http.proxyPort");
            proxyPort = Integer.parseInt((portStr != null ? portStr : "-1"));
        } else {
            proxyAddress = android.net.Proxy.getHost(context);
            proxyPort = android.net.Proxy.getPort(context);
        }
        return (!TextUtils.isEmpty(proxyAddress)) && (proxyPort != -1);
    }

    /**
     * 检测是否正在使用VPN，如果在使用返回true,反之返回false
     */
    public static boolean isVpnUsed() {
        try {
            Enumeration niList = NetworkInterface.getNetworkInterfaces();
            if (niList != null) {
                while (niList.hasMoreElements()) {
                    NetworkInterface intf = (NetworkInterface) niList.nextElement();
                    if (!intf.isUp() || intf.getInterfaceAddresses().size() == 0) {
                        continue;
                    }
                    Log.d("-----", "isVpnUsed() NetworkInterface Name: " + intf.getName());
                    if ("tun0".equals(intf.getName()) || "ppp0".equals(intf.getName())) {
                        return true; // The VPN is up
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断是否包含SIM卡
     *
     * @return 状态
     */
    public static boolean hasSimCard(Context context) {
        TelephonyManager telMgr = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        int simState = telMgr.getSimState();
        boolean result = true;
        switch (simState) {
            case TelephonyManager.SIM_STATE_ABSENT:
                result = false; // 没有SIM卡
                break;
            case TelephonyManager.SIM_STATE_UNKNOWN:
                result = false;
                break;
        }
        Log.d("try", result ? "有SIM卡" : "无SIM卡");
        return result;
    }

    /**
     * 判断当前手机是否有ROOT权限
     *
     * @return
     */
    public boolean isRoot() {
        boolean bool = false;

        try {
            if ((!new File("/system/bin/su").exists()) && (!new File("/system/xbin/su").exists())) {
                bool = false;
            } else {
                bool = true;
            }
//            Log.d(TAG, "bool = " + bool);
        } catch (Exception e) {

        }
        return bool;
    }


    /**
     * @return true 开启调试，false 未开启调试
     * @author James
     * @Description 是否是usb调试模式
     */
    public static boolean usbStatus(Context mContext) {
        boolean enableAdb = (Settings.Secure.getInt(
                mContext.getContentResolver(), Settings.Secure.ADB_ENABLED, 0) > 0);
        Log.d("LHM", "usbStatus: " + enableAdb);
        return enableAdb;
    }
    /**
     * 是否为鸿蒙系统
     *
     * @return true为鸿蒙系统
     */
    public static boolean isHarmonyOs() {
        try {
            Class<?> buildExClass = Class.forName("com.huawei.system.BuildEx");
            Object osBrand = buildExClass.getMethod("getOsBrand").invoke(buildExClass);
            return "Harmony".equalsIgnoreCase(osBrand.toString());
        } catch (Throwable x) {
            return false;
        }
    }
    /**
     * 获取鸿蒙系统版本号
     *
     * @return 版本号
     */
    public static String getHarmonyVersion() {
        if (!isHarmonyOs()){
            return "1.0.0";
        }
        return getProp("hw_sc.build.platform.version", "1.0.0");
    }
    private static String getProp(String property, String defaultValue) {
        try {
            Class spClz = Class.forName("android.os.SystemProperties");
            Method method = spClz.getDeclaredMethod("get", String.class);
            String value = (String) method.invoke(spClz, property);
            if (TextUtils.isEmpty(value)) {
                return defaultValue;
            }
            return value;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

}
