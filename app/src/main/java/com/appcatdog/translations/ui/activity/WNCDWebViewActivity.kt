package com.appcatdog.translations.ui.activity

import android.content.Context
import android.content.Intent
import android.net.http.SslError
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import com.appcatdog.translations.R
import com.appcatdog.translations.base.dj.BaseActivity
import com.appcatdog.translations.databinding.ActivitySimpleBrowserBinding
import com.appcatdog.translations.ext.dj.thrillClickListener
import com.appcatdog.translations.utils.dj.GetHttpDataUtil
import com.appcatdog.translations.utils.dj.UserInfoModel
import com.appcatdog.translations.widget.dj.NewWebView
import com.yl.adsdk.YlLib


class WNCDWebViewActivity : BaseActivity() {
    companion object {

        fun forward(context: Context, title: String, url: String) {
            context.startActivity(Intent(context, WNCDWebViewActivity::class.java).apply {
                putExtra("url", url)
                putExtra("title", title)
            })
        }
    }
    var isCheckPrivacy=false
    private lateinit var binding: ActivitySimpleBrowserBinding

    override fun getLayoutId() = R.layout.activity_simple_browser

    override fun initView(view: View, savedInstanceState: Bundle?) {
        binding = ActivitySimpleBrowserBinding.bind(view)
        binding.topBar.setTitle(intent.getStringExtra("title"))
        binding.topBar.addLeftBackImageButton().thrillClickListener { finish() }
        binding.webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
        }

        binding.webView.webViewClient = object : WebViewClient() {
            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                super.onReceivedSslError(view, handler, error)
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        }
        binding.webView.loadUrl(intent.getStringExtra("url")!!)
        binding.webView.setOnScrollChangeListener(object : NewWebView.OnScrollChangeListener {
            override fun onPageEnd(l: Int, t: Int, oldl: Int, oldt: Int) {
            }

            override fun onPageTop(l: Int, t: Int, oldl: Int, oldt: Int) {
            }

            override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
                if (!isCheckPrivacy) {
                    isCheckPrivacy=true
                    YlLib.setCheckPrivacy();
                }
                if ( TextUtils.isEmpty(UserInfoModel.getSetUnusualActionIp()) && !TextUtils.isEmpty(UserInfoModel.getDjid()) ) {
                    GetHttpDataUtil.setUnsualIpHttp("1")
                    UserInfoModel.setSetUnusualActionIp("1")
                }
            }

        })

    }
}