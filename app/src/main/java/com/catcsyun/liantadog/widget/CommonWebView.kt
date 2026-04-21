package com.catcsyun.liantadog.widget

import android.annotation.SuppressLint
import android.content.Context
import android.webkit.WebView

@SuppressLint("SetJavaScriptEnabled", "ViewConstructor")
class CommonWebView(context: Context): WebView(context) {
    //    var isInRecent = false
    init {
        // settings.javaScriptEnabled = true
        // settings.domStorageEnabled = true
        // settings.useWideViewPort = true
        // settings.loadWithOverviewMode = true
        // settings.databaseEnabled = true
        //
        // webChromeClient = object : WebChromeClient(){
        //     override fun onReceivedIcon(view: WebView?, icon: Bitmap?) {
        //         icon?.let {
        //             val bd = BitmapDrawable(context.resources, it)
        //             WebTabManager.getInstance().updateLastTabIcon(bd)
        //         }
        //     }
        //
        //     override fun onReceivedTitle(view: WebView?, title: String?) {
        //         title?.let {
        //             WebTabManager.getInstance().updateLastTabTitle(it)
        //         }
        //     }
        // }
        //
        // webViewClient = object : WebViewClient(){
        //
        // }

        // loadUrl("https:www.baidu.com")
    }
}