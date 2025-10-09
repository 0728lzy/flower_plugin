package com.qingchu.wangmiao.base.dj

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.gyf.immersionbar.ImmersionBar
import com.qingchu.wangmiao.R


abstract class BaseActivity : RootActivity() {

    protected abstract fun getLayoutId(): Int

    protected abstract fun initView(view: View, savedInstanceState: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initStatus()
        performInitView(savedInstanceState)
    }

    /**
     * 1，设置 ViewBinding 可简化 findViewById 的操作，
     * 必须在[AppCompatActivity.setContentView] 之后调用
     *
     *
     * 2，初始化 View 相关操作
     */
    private fun performInitView(savedInstanceState: Bundle?) {
        val rootView = findViewById<ViewGroup>(android.R.id.content)
        if (rootView.childCount > 0) {
            val contentView = rootView.getChildAt(rootView.childCount - 1)
            initView(contentView, savedInstanceState)
        }
    }

    open fun initStatus() {
        ImmersionBar.with(this)
            .statusBarColor(R.color.white)
            .autoDarkModeEnable(true)
            .init()
    }

}