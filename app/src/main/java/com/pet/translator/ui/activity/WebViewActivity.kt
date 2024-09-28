package com.pet.translator.ui.activity

import android.content.Context
import android.content.Intent
import android.net.http.SslError
import android.os.Bundle
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import com.pet.translator.R
import com.pet.translator.base.dj.BaseActivity
import com.pet.translator.databinding.ActivitySimpleBrowserBinding
import com.pet.translator.ext.dj.thrillClickListener


class WebViewActivity : BaseActivity() {
    companion object {

        fun forward(context: Context, title: String, url: String) {
            context.startActivity(Intent(context, WebViewActivity::class.java).apply {
                putExtra("url", url)
                putExtra("title", title)
            })
        }
    }

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
    }
}