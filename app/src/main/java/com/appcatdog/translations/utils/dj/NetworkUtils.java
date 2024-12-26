package com.appcatdog.translations.utils.dj;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.util.Enumeration;

/**
 * Created by lihui on 15/12/15.
 */
public class NetworkUtils {


    /**
     * Returns whether the network is available
     */
    public static boolean isNetworkAvaiable(Context context) {

        if (context == null) {
            return false;
        }

        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
        } else {
            @SuppressLint("MissingPermission") NetworkInfo[] info = manager.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0, length = info.length; i < length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /**
     * 判断当前网络类型
     *
     * @param context
     * @return 1 代表gprs,2代表wifi
     */
    public static String getNetworkType(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null || !info.isConnected()) {
            return "UNCNCT"; // not connected
        }
        if (info.getType() == ConnectivityManager.TYPE_WIFI) {
            return "WIFI";
        }
        if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            int networkType = info.getSubtype();
            switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    return "2G";
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    return "3G";
                case TelephonyManager.NETWORK_TYPE_LTE:
                    return "4G";
                case TelephonyManager.NETWORK_TYPE_NR:
                    return "5G";
                default:
                    return "UNKNOWN";
            }
        }
        return "UNKNOWN";
    }


    /**
     * 判断移动网络是否连接
     *
     * @param context
     * @return
     * @throws Exception
     */
    @SuppressLint("MissingPermission")
    public static boolean isMobileDataEnable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isMobileDataEnable = false;

        isMobileDataEnable = connectivityManager.getNetworkInfo(
                ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();

        return isMobileDataEnable;
    }

    /**
     * 判断wifi是否连接
     *
     * @param context
     * @return
     * @throws Exception
     */
    @SuppressLint("MissingPermission")
    public static boolean isWifiDataEnable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isWifiDataEnable = false;
        isWifiDataEnable = connectivityManager.getNetworkInfo(
                ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        return isWifiDataEnable;
    }


    public static String getWIFILocalIpAdress(Context mContext) {

        //获取wifi服务
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
        String ip = "0.0.0.0";
        if (!wifiManager.isWifiEnabled()) {
            ip = getGPRSLocalIpAddress();
        } else {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            ip = formatIpAddress(ipAddress);
        }
        return ip;
    }

    public static String getGPRSLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
        }
        return "";
    }

    private static String formatIpAddress(int ipAdress) {

        return (ipAdress & 0xFF) + "." +
                ((ipAdress >> 8) & 0xFF) + "." +
                ((ipAdress >> 16) & 0xFF) + "." +
                (ipAdress >> 24 & 0xFF);
    }

    public static String getPrintSize(long size) {
        //如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        if (size < 1024) {
            return String.valueOf(size) + "B";
        } else {
            size = size / 1024;
        }
        //如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        //因为还没有到达要使用另一个单位的时候
        //接下去以此类推
        if (size < 1024) {
            return String.valueOf(size) + "KB";
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            //因为如果以MB为单位的话，要保留最后1位小数，
            //因此，把此数乘以100之后再取余
            size = size * 100;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + "MB";
        } else {
            //否则如果要以GB为单位的，先除于1024再作同样的处理
            size = size * 100 / 1024;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + "GB";
        }
    }

    public static String getWIFISSID(Activity activity) {
        String ssid = "WiFi";

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O || Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

            WifiManager mWifiManager = (WifiManager) activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

            assert mWifiManager != null;
            WifiInfo info = mWifiManager.getConnectionInfo();
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                return info.getSSID();
            } else {
                return info.getSSID().replace("\"", "");
            }
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O_MR1) {

            ConnectivityManager connManager = (ConnectivityManager) activity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            assert connManager != null;
            NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
            if (networkInfo.isConnected()) {
                if (networkInfo.getExtraInfo() != null) {
                    return networkInfo.getExtraInfo().replace("\"", "");
                }
            }
        }
        return ssid;
    }
    /**
     * 格式化数据
     *
     * @param data
     * @return
     */
    //定义TB的计算常量
    private static final double TB = 1024f * 1024f * 1024f * 1024f;
    //定义GB的计算常量
    private static final double GB = 1024f * 1024f * 1024f;
    //定义MB的计算常量
    private static final double MB = 1024f * 1024f;
    //定义KB的计算常量
    private static final double KB = 1024f;
    public static String formatByte(long data) {

        DecimalFormat format = new DecimalFormat("####.#");
        if (data < KB) {
            return data + "B";
        } else if (data < MB) {
            return format.format(data / KB) + "KB";
        } else if (data < GB) {
            return format.format(data / MB) + "MB";
        } else if (data < TB) {
            return format.format(data / GB) + "GB";
        } else {
            return format.format(data / GB / 1024) + "TB";
//            return "超出统计范围";
        }
    }

}
