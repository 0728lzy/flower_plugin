package com.weini.maogou.utils.dj

import android.os.Handler
import android.os.Message

interface ICountDown {
    fun start()
    fun pause()
    fun resume()
    fun stop()
}

internal abstract class CountDownTool(  // 计时总时长
    private val mTotalTime: Long
) : ICountDown {
    private var isPause = false
    private val mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            var second = msg.obj as Long
            if (second == 0L) {
                finishTime()
                return
            }
            if (!isPause) {
                onTick(second)
                second--
            }
            val message = obtainMessage()
            message.obj = second
            sendMessageDelayed(message, 1000)
        }
    }

    override fun start() {
        isPause = false
        mHandler.removeCallbacksAndMessages(null)
        val msg = mHandler.obtainMessage()
        msg.obj = mTotalTime
        mHandler.sendMessage(msg)
    }

    override fun stop() {
        mHandler.removeCallbacksAndMessages(null)
    }

    override fun pause() {
        isPause = true
    }

    override fun resume() {
        isPause = false
    }

    protected abstract fun onTick(second: Long)
    protected abstract fun finishTime()
}