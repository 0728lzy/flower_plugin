package com.catcsyun.liantadog.utils

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper

class AudioPlayer {

    private var mediaPlayer: MediaPlayer? = null
    private val handler = Handler(Looper.getMainLooper())
    private var progressRunnable: Runnable? = null

    // 初始化播放器
    fun initPlayer(audioUrl: String, onProgress: (current: Int, total: Int) -> Unit) {
        mediaPlayer = MediaPlayer().apply {
            setDataSource(audioUrl)
            prepareAsync()
            setOnPreparedListener {
                start()
                startProgressUpdates(onProgress)
            }
            setOnCompletionListener {
                stopProgressUpdates()
            }
        }
    }

    // 开始定时更新进度
    private fun startProgressUpdates(onProgress: (current: Int, total: Int) -> Unit) {
        progressRunnable = object : Runnable {
            override fun run() {
                mediaPlayer?.let {
                    val current = it.currentPosition
                    val total = it.duration
                    onProgress(current, total)
                    handler.postDelayed(this, 500) // 每 500ms 更新一次
                }
            }
        }
        handler.post(progressRunnable!!)
    }

    // 停止更新
    private fun stopProgressUpdates() {
        progressRunnable?.let { handler.removeCallbacks(it) }
    }

    // 释放资源
    fun release() {
        stopProgressUpdates()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    // 暂停
    fun pause() {
        mediaPlayer?.pause()
    }

    // 继续播放
    fun resume() {
        mediaPlayer?.start()
    }

    // 跳转到指定位置
    fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
    }
}
