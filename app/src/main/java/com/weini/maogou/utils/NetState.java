package com.weini.maogou.utils;

/**
 * 枚举网络状态
 * NET_NO：没有网络
 * NET_2G:2g网络
 * NET_3G：3g网络
 * NET_4G：4g网络
 * NET_WIFI：wifi
 * NET_UNKNOWN：未知网络
 */
public class NetState {
    public static final int NET_NO_CONNECT = 0;
    public static final int NET_WIFI = 1;
    public static final int NET_2G = 2;
    public static final int NET_3G = 3;
    public static final int NET_4G = 4;
    public static final int NET_5G = 5;
    public static final int NET_UNKNOWN = -1;
    /**
     * 没有READ_PHONE_STATE 权限,不过是可以知道当前是 移动网络,不是wifi
     */
    public static final int NOT_PERMISSION_READ_PHONE_STATE_ONLY_GPRS = -2;
    public static final int NOT_PERMISSION_ACCESS_NETWORK_STATE = -3;
}
