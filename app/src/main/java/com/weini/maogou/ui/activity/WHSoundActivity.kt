package com.weini.maogou.ui.activity

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import android.widget.ArrayAdapter
import android.widget.SeekBar
import androidx.appcompat.app.ActionBar.LayoutParams
import androidx.appcompat.widget.ListPopupWindow
import androidx.core.view.isVisible
import com.weini.maogou.R
import com.weini.maogou.base.dj.BaseActivity
import com.weini.maogou.databinding.ActivitySoundDetailBinding
import com.weini.maogou.ext.dp2px
import com.weini.maogou.ext.thrillClickListener
import com.weini.maogou.utils.lzy.LZYADSUtils
import java.math.BigDecimal
import java.math.RoundingMode

class WHSoundActivity : BaseActivity() {

    companion object {
        fun show(context: Context, isDog: Boolean, index: Int) {
            context.startActivity(Intent(context, WHSoundActivity::class.java).apply {
                putExtra("index", index)
                putExtra("isDog", isDog)
            })
        }
    }

    override fun getLayoutId() = R.layout.activity_sound_detail

    private lateinit var binding: ActivitySoundDetailBinding
    private lateinit var lzyadsUtils: LZYADSUtils

    private var rawPath: Int = 0

    private var mediaPlayer: MediaPlayer? = null

    private var isLoop = false

    private var duration = 0

    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun dispatchMessage(msg: Message) {
            super.dispatchMessage(msg)
            duration -= 1000
            binding.tvTextTimeDelay.text = (duration / 1000).toString() + "s"
            if (duration <= 0) {
                binding.tvTextDelayTitle.isVisible = false
                binding.tvTextTimeDelay.isVisible = false
                binding.btnEndTime.isVisible = false
                duration = 0
                play()
            } else {
                send()
            }
        }
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        binding = ActivitySoundDetailBinding.bind(view)
        lzyadsUtils=LZYADSUtils("SoundActivity",this@WHSoundActivity)
        lzyadsUtils.showAdCpTurn()
        lzyadsUtils.loadSimpleAdTurn(binding.feedContainerActivitySound,-1)
        binding.toolbar.ivMenu.setImageResource(R.drawable.ic_arrow_back_24)
        binding.toolbar.ivMenu.thrillClickListener { onBackPressed() }
        val index = intent.getIntExtra("index", 1)
        val isDog = intent.getBooleanExtra("isDog", false)

        val item = if (isDog) {
            com.weini.maogou.AppConst.dogSoundList(this)[index]
        } else {
            com.weini.maogou.AppConst.catSoundList(this)[index]
        }
        binding.ivMusic.setImageResource(item.icon)
        binding.toolbar.tvTitle.text = item.title
        rawPath = item.sound

        binding.lnMenuSetTime.thrillClickListener {
            val list = listOf("OFF", "15s", "30s", "45s", "1m", "2m", "5m")
            val listPop = ListPopupWindow(this)
            listPop.setAdapter(ArrayAdapter(this, android.R.layout.simple_list_item_1, list))
            listPop.width = dp2px(200f)
            listPop.height = LayoutParams.WRAP_CONTENT
            listPop.anchorView = binding.lnMenuSetTime
            listPop.isModal = true
            listPop.setOnItemClickListener { adapterView, view, position, l ->
                if (position == 0) {
                    binding.tvTextDelayTitle.isVisible = false
                    binding.tvTextTimeDelay.isVisible = false
                    binding.btnEndTime.isVisible = false
                    handler.removeMessages(1)
                    listPop.dismiss()
                    return@setOnItemClickListener
                }
                duration = when (position) {
                    1 -> 15000
                    2 -> 30000
                    3 -> 45000
                    4 -> 60000
                    5 -> 60000 * 2
                    6 -> 60000 * 5
                    else -> 0
                }
                binding.tvTextDelayTitle.isVisible = true
                binding.tvTextTimeDelay.isVisible = true
                binding.btnEndTime.isVisible = true
                binding.tvTextTimeDelay.text = (duration / 1000).toString() + "s"
                send()
                listPop.dismiss()
            }
            listPop.show()
        }
        binding.btnEndTime.thrillClickListener {
            binding.tvTextDelayTitle.isVisible = false
            binding.tvTextTimeDelay.isVisible = false
            binding.btnEndTime.isVisible = false
            handler.removeMessages(1)
        }
        binding.switchCbLoop.setOnCheckedChangeListener { compoundButton, b ->
            isLoop = b
        }
        binding.seekBarVolume.progress = 100
        binding.seekBarVolume.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                p0?.let { seekbar ->
                    val volume =
                        BigDecimal(seekbar.progress).divide(BigDecimal(100)).setScale(2, RoundingMode.HALF_UP).toFloat()
                    mediaPlayer?.setVolume(volume, volume)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })

        mediaPlayer = MediaPlayer()
        mediaPlayer?.reset()
        val fd = resources.openRawResourceFd(rawPath)
        mediaPlayer?.setDataSource(fd.fileDescriptor, fd.startOffset, fd.length)
        mediaPlayer?.prepare()
        mediaPlayer?.setOnCompletionListener {
            pause()
        }
        // mediaPlayer?.setlistener
        binding.ivPlayPause.thrillClickListener {
            switch()
        }
    }

    private fun send() {
        handler.sendEmptyMessageDelayed(1, 1000)
    }

    private fun switch() {
        if (mediaPlayer?.isPlaying == true) {
            pause()
        } else {
            play()
        }
    }

    private fun play() {
        binding.ivPlayPause.setImageResource(R.drawable.img_pause)
        mediaPlayer?.isLooping = isLoop
        mediaPlayer?.start()
        binding.rippleBackground.startRippleAnimation()
    }

    private fun pause() {
        binding.ivPlayPause.setImageResource(R.drawable.img_play)
        mediaPlayer?.isLooping = isLoop
        mediaPlayer?.pause()
        binding.rippleBackground.stopRippleAnimation()

    }

    override fun onPause() {
        super.onPause()
        pause()
    }

    override fun onDestroy() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        super.onDestroy()
    }

    override fun onBackPressed() {
        finish()
    }

}