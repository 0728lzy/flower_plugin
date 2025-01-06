package com.ruiteapp.pettranslator.utils;

import androidx.annotation.IntDef;

@IntDef({NetState.NET_NO_CONNECT, NetState.NET_2G, NetState.NET_3G, NetState.NET_4G, NetState.NET_5G
        , NetState.NET_UNKNOWN, NetState.NET_WIFI
        ,NetState.NOT_PERMISSION_ACCESS_NETWORK_STATE,NetState.NOT_PERMISSION_READ_PHONE_STATE_ONLY_GPRS})
public @interface NetStateInt {

}
