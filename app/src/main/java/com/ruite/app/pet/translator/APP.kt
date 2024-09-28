package com.ruite.app.pet.translator

import android.app.Application
import com.ruite.app.pet.translator.db.RoomHelper
import com.ruite.app.pet.translator.net.GsonConverter
import com.drake.net.NetConfig
import com.drake.net.interceptor.LogRecordInterceptor
import com.drake.net.interceptor.RequestInterceptor
import com.drake.net.okhttp.setConverter
import com.drake.net.okhttp.setDebug
import com.drake.net.okhttp.setRequestInterceptor
import com.drake.net.request.BaseRequest
import com.kongzue.dialogx.DialogX
import java.util.concurrent.TimeUnit


class APP : Application() {

    companion object {
        lateinit var instance: APP
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        RoomHelper.init(this, "app.db", 1)
        NetConfig.initialize("", this) {
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
            setDebug(BuildConfig.DEBUG)
            setConverter(GsonConverter())
            setRequestInterceptor(object : RequestInterceptor {
                override fun interceptor(request: BaseRequest) {
                    request.addHeader(
                        "token",
                        "CeQhjW7RLybrzzt01ZDUWOm8IEHkiTwSiN+aawlGalgXgDL/2x2BUFeGQ6p2z1Tf7d9CGdKc9FVkOmKCg/N48hyakpIRseFZ2WDXnskM7MRvHynXcYAtA2n2Mi1cFCIBIMWuYmpeG/80LB9l0xxsUQ=="
                    )
                    request.addHeader("appclient", "100005")
                    request.addHeader("model", "")
                    request.addHeader("deviceid", "")
                    request.addHeader("channel", "003")
                    request.addHeader("realchannel", "002")
                    request.addHeader("version", "1.0.0")

                }
            })
            addInterceptor(LogRecordInterceptor(BuildConfig.DEBUG))
        }
        DialogX.init(this)
        DialogX.globalTheme = DialogX.THEME.DARK

    }
}