package com.ruite.app.pet.translator.ui.activity

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import androidx.core.animation.addListener
import com.ruite.app.pet.translator.BuildConfig
import com.ruite.app.pet.translator.R
import com.ruite.app.pet.translator.base.BaseActivity
import com.ruite.app.pet.translator.databinding.ActivityLauncherBinding

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
        LanguageActivity.forward(this,true)
        finish()
    }
}