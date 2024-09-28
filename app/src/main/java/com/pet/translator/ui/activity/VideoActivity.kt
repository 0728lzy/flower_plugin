package com.pet.translator.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.pet.translator.R
import com.pet.translator.base.dj.BaseActivity
import com.pet.translator.databinding.ActivityOverviewCallBinding
import com.pet.translator.ext.thrillClickListener
import com.pet.translator.utils.lzy.LZYADSUtils
import java.math.BigDecimal
import java.math.RoundingMode


class VideoActivity : BaseActivity() {

    companion object {
        fun show(context: Context, index: Int) {
            context.startActivity(Intent(context, VideoActivity::class.java).apply {
                putExtra("index", index)
            })
        }
    }

    override fun getLayoutId() = R.layout.activity_overview_call

    private lateinit var binding: ActivityOverviewCallBinding

    private var index = 0

    private var total = 0

    private var duration = 0

    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun dispatchMessage(msg: Message) {
            super.dispatchMessage(msg)
            duration -= 1
            updatePanel()

            if (duration <= 0) {
                goCall()
            } else {
                send()
            }
        }
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        binding = ActivityOverviewCallBinding.bind(view)
        binding.toolbar.ivMenu.setImageResource(R.drawable.ic_arrow_back_24)
        binding.toolbar.ivMenu.thrillClickListener { onBackPressed() }
        LZYADSUtils("VideoActivity",this).showAdCpTurn()
        index = intent.getIntExtra("index", 1)
        var name = ""
        var icon = 0
        when (index) {
            1 -> {
                name = "Husky"
                icon = R.mipmap.call_1
            }

            2 -> {
                name = "Husky striped"
                icon = R.mipmap.call_2
            }

            3 -> {
                name = "British short hair cat"
                icon = R.mipmap.call_3
            }

            4 -> {
                name = "Golden"
                icon = R.mipmap.call_4
            }

            5 -> {
                name = "Cat"
                icon = R.mipmap.call_5
            }

            else -> {}
        }
        binding.ivAvatar.setImageResource(icon)
        binding.tvName.text = name
        binding.layoutSetTime.tvOff.thrillClickListener {
            reset()
            duration = 0

        }
        binding.layoutSetTime.tv5s.thrillClickListener {
            reset()
            duration = 5
            binding.layoutSetTime.tvOff.setBackgroundResource(R.drawable.bg_button_fake_call)
            binding.layoutSetTime.tvOff.setTextColor(ContextCompat.getColor(this, R.color.white))
            startTime()
        }
        binding.layoutSetTime.tv15s.thrillClickListener {
            reset()
            duration = 15
            binding.layoutSetTime.tv5s.setBackgroundResource(R.drawable.bg_button_fake_call)
            binding.layoutSetTime.tv5s.setTextColor(ContextCompat.getColor(this, R.color.white))
            startTime()
        }
        binding.layoutSetTime.tv30s.thrillClickListener {
            reset()
            duration = 30
            binding.layoutSetTime.tv15s.setBackgroundResource(R.drawable.bg_button_fake_call)
            binding.layoutSetTime.tv15s.setTextColor(ContextCompat.getColor(this, R.color.white))
            startTime()
        }
        binding.layoutSetTime.tv1m.thrillClickListener {
            reset()
            duration = 60
            binding.layoutSetTime.tv30s.setBackgroundResource(R.drawable.bg_button_fake_call)
            binding.layoutSetTime.tv30s.setTextColor(ContextCompat.getColor(this, R.color.white))
            startTime()
        }
        binding.layoutSetTime.tv3m.thrillClickListener {
            reset()
            duration = 60 * 3
            binding.layoutSetTime.tv1m.setBackgroundResource(R.drawable.bg_button_fake_call)
            binding.layoutSetTime.tv1m.setTextColor(ContextCompat.getColor(this, R.color.white))
            startTime()
        }
        binding.layoutSetTime.tv5m.thrillClickListener {
            reset()
            duration = 60 * 5
            binding.layoutSetTime.tv3m.setBackgroundResource(R.drawable.bg_button_fake_call)
            binding.layoutSetTime.tv3m.setTextColor(ContextCompat.getColor(this, R.color.white))
            startTime()
        }
        binding.tvCancel.thrillClickListener {
            stopTime()
        }
        binding.tvTakeAVideoCall.thrillClickListener {
            goCall()
        }
    }

    private fun reset() {
        binding.layoutSetTime.tvOff.setBackgroundResource(R.drawable.bg_set_time)
        binding.layoutSetTime.tvOff.setTextColor(ContextCompat.getColor(this, R.color.black))
        binding.layoutSetTime.tv5s.setBackgroundResource(R.drawable.bg_set_time)
        binding.layoutSetTime.tv5s.setTextColor(ContextCompat.getColor(this, R.color.black))
        binding.layoutSetTime.tv15s.setBackgroundResource(R.drawable.bg_set_time)
        binding.layoutSetTime.tv15s.setTextColor(ContextCompat.getColor(this, R.color.black))
        binding.layoutSetTime.tv30s.setBackgroundResource(R.drawable.bg_set_time)
        binding.layoutSetTime.tv30s.setTextColor(ContextCompat.getColor(this, R.color.black))
        binding.layoutSetTime.tv1m.setBackgroundResource(R.drawable.bg_set_time)
        binding.layoutSetTime.tv1m.setTextColor(ContextCompat.getColor(this, R.color.black))
        binding.layoutSetTime.tv3m.setBackgroundResource(R.drawable.bg_set_time)
        binding.layoutSetTime.tv3m.setTextColor(ContextCompat.getColor(this, R.color.black))
        binding.layoutSetTime.tv5m.setBackgroundResource(R.drawable.bg_set_time)
        binding.layoutSetTime.tv5m.setTextColor(ContextCompat.getColor(this, R.color.black))
    }

    private fun startTime() {
        binding.rlCountDown.isVisible = true
        binding.rlTime.isVisible = false
        binding.tvTakeAVideoCall.isVisible = false
        total = duration
        updatePanel()
        send()
    }

    private fun stopTime() {
        binding.rlCountDown.isVisible = false
        binding.rlTime.isVisible = true
        binding.tvTakeAVideoCall.isVisible = true
        duration = 0
        total = 0
        handler.removeMessages(1)
        binding.layoutSetTime.tvOff.setBackgroundResource(R.drawable.bg_button_fake_call)
        binding.layoutSetTime.tvOff.setTextColor(ContextCompat.getColor(this, R.color.white))
    }

    private fun send() {
        handler.sendEmptyMessageDelayed(1, 1000)
    }

    private fun updatePanel() {
        val minute = duration / 60
        val second = duration % 60
        val minuteStr = if (minute < 10) "0${minute}" else minute.toString()
        val secondStr = if (second < 10) "0${second}" else second.toString()

        binding.tvTimeCountDown.text = "${minuteStr}:${secondStr}"
        binding.progress.progress =
            BigDecimal(duration).divide(BigDecimal(total), 2, RoundingMode.HALF_UP).multiply(BigDecimal(100)).toInt()
    }

    private fun goCall() {
        stopTime()
        reset()
        CallActivity.show(this, index)
    }

}