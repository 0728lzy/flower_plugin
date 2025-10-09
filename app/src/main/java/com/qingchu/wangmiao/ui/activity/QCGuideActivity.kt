package com.qingchu.wangmiao.ui.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.blankj.utilcode.util.SPUtils
import com.qingchu.wangmiao.R
import com.qingchu.wangmiao.base.dj.BaseActivity
import com.qingchu.wangmiao.databinding.ActivityGuideBinding
import com.qingchu.wangmiao.ui.fragment.WHGuideFragment

class QCGuideActivity : BaseActivity() {

    companion object {
        fun forward(context: Context) {
            context.startActivity(Intent(context, QCGuideActivity::class.java))
        }
    }

    override fun getLayoutId() = R.layout.activity_guide

    private lateinit var binding: ActivityGuideBinding

    private lateinit var pagerAdater: ScreenSlidePagerAdapter


    override fun initView(view: View, savedInstanceState: Bundle?) {
        binding = ActivityGuideBinding.bind(view)
        pagerAdater = ScreenSlidePagerAdapter(this)
        binding.viewPager.adapter = pagerAdater
    }

    override fun onBackPressed() {
        if (binding.viewPager.currentItem == 0) {
            super.onBackPressed()
        } else {
            binding.viewPager.currentItem -= 1
        }
    }

    fun next() {
        if (binding.viewPager.currentItem == 3) {
            forward()
        } else {
            binding.viewPager.currentItem += 1
        }
    }

    private fun forward() {
        if (SPUtils.getInstance().getBoolean("first_permission", true)
            || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            SPUtils.getInstance().put("first_permission", false)
            QCPermissionActivity.forward(this)
        } else {
            QCMainActivity.forward(this)
            finish()
        }
    }

    private inner class ScreenSlidePagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

        override fun getItemCount(): Int = 4

        override fun createFragment(position: Int): Fragment = WHGuideFragment(position + 1)

    }
}