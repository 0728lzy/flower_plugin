package com.pet.translator.ui.activity

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import androidx.core.animation.addListener
import com.pet.translator.BuildConfig
import com.pet.translator.R
import com.pet.translator.base.dj.BaseActivity
import com.pet.translator.databinding.ActivityLauncherBinding
import com.pet.translator.utils.LanguageUtils

class LauncherActivity : BaseActivity() {

    override fun getLayoutId() = R.layout.activity_launcher

    private lateinit var binding: ActivityLauncherBinding


    override fun initView(view: View, savedInstanceState: Bundle?) {
        binding = ActivityLauncherBinding.bind(view)

        startTime()
    }

    private fun startTime() {
        val animate = ValueAnimator.ofInt(0, 100)
        animate.duration = 5000

        if (BuildConfig.DEBUG) {
            // forward()
            animate.duration = 1000
        }

        animate.addListener(onEnd = {
            forward()
        })
        animate.start()
    }

    private fun forward() {
        LanguageUtils.setIndex(0)
        MainActivity.forward(this)
        finish()
    }
}