package com.weini.maogou.csj.lzy

import android.content.Context
import com.weini.maogou.utils.dj.BatteryUtils
import com.weini.maogou.utils.dj.DeviceUtils
import com.weini.maogou.utils.dj.EmulatorUtils
import com.weini.maogou.utils.dj.RootUtil


object LzyUtils {
    fun isNormalUser(context: Context):Boolean{
        return DeviceUtils.hasSimCard(context)&&//有无sim卡
                !EmulatorUtils.isEmulator2(context)&&//是否虚拟机
                !RootUtil.isCurrDeviceRooted()&&//是否root
                !DeviceUtils.usbStatus(context)&&//是否开启usb调试
                !BatteryUtils.isBatteryCharging(context)//是否正在充电
    }

    fun isNormalUser2(context: Context):Boolean{
        return !BatteryUtils.isBatteryCharging(context)//是否正在充电
    }
}