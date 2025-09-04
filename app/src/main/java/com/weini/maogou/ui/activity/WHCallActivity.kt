package com.weini.maogou.ui.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.AssetDataSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.common.util.concurrent.ListenableFuture
import com.hjq.permissions.Permission
import com.weini.maogou.R
import com.weini.maogou.base.dj.BaseActivity
import com.weini.maogou.databinding.ActivityIncomingCallBinding
import com.weini.maogou.ext.countDown
import com.weini.maogou.ext.thrillClickListener
import com.weini.maogou.utils.lzy.PermissionUtils
import java.util.Formatter
import java.util.TimerTask
import java.util.concurrent.ExecutionException


class WHCallActivity : BaseActivity() {


    companion object {
        fun show(context: Context, index: Int) {
            context.startActivity(Intent(context, WHCallActivity::class.java).apply {
                putExtra("index", index)
            })
        }
    }

    override fun getLayoutId() = R.layout.activity_incoming_call

    private lateinit var binding: ActivityIncomingCallBinding

    private var index = 0
    private var path = ""

    override fun initView(view: View, savedInstanceState: Bundle?) {
        binding = ActivityIncomingCallBinding.bind(view)


        index = intent.getIntExtra("index", 1)
        var name = ""
        var icon = 0
        when (index) {
            1 -> {
                name = getString(R.string.call_3)
                icon = R.mipmap.call_3
                path = "assets:///video_call/" + "call_3.mp4"
            }

            2 -> {
                name = getString(R.string.call_1)
                icon = R.mipmap.call_1
                path = "file:///android_asset/video_call/" + "call_1.mp4"
            }

            3 -> {
                name = getString(R.string.call_5)
                icon = R.mipmap.call_5
                path = "file:///android_asset/video_call/" + "call_5.mp4"
            }

            4 -> {
                name = getString(R.string.call_2)
                icon = R.mipmap.call_2
                path = "file:///android_asset/video_call/" + "call_2.mp4"
            }

            5 -> {
                name = getString(R.string.call_4)
                icon = R.mipmap.call_4
                path = "file:///android_asset/video_call/" + "call_4.mp4"
            }

            else -> {}
        }
        binding.ivThumbnailVideo.setImageResource(icon)
        binding.ivAvatar.setImageResource(icon)
        binding.tvNameIncoming.text = name

        binding.ivCloseIncoming.thrillClickListener {
            onBackPressed()
        }
        binding.ivAnswerIncoming.thrillClickListener {
            PermissionUtils.tryToDoSomethingWithCheckPermissionAndCode(
                this,
                arrayOf(
                    Permission.CAMERA,
                ),
                2,
                "权限被拒绝，无法使用该功能"
            ) {
                answer()
                com.weini.maogou.utils.VibrateTool.vibrateStop()
            }
        }
        binding.rlIncomingCall.isVisible = true
        binding.rlAnswerCall.isVisible = false
        com.weini.maogou.utils.VibrateTool.vibrateComplicated(this, longArrayOf(500L, 1000L), 0)
    }

    private var player: SimpleExoPlayer? = null
    private val timerTask: TimerTask? = null

    private fun answer() {
        binding.rlIncomingCall.isVisible = false
        binding.rlAnswerCall.isVisible = true


        playVideo()
        countDown(
            time = 100000,
            start = {

            },
            next = {
                val time = 100000 - it.toInt()
                val seconds = time % 60
                val minutes = (time / 60) % 60
                val hours = time / 3600
                // 计时
                binding.tvTime.text = Formatter().format("%02d:%02d:%02d", hours, minutes, seconds).toString()
            },
            end = {

            }
        )


        binding.ivCameraCall.thrillClickListener {
            // 切换摄像头
            if (cameraState == CameraSelector.LENS_FACING_FRONT) {
                cameraState = CameraSelector.LENS_FACING_BACK
            } else {
                cameraState = CameraSelector.LENS_FACING_FRONT
            }
            processCameraProvider?.let {
                bindPreview(it)
            }
        }
        binding.ivCloseAnswer.thrillClickListener {
            onBackPressed()
        }
        startPreview()
    }

    private fun startPreview() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(this,"请打开相机权限",Toast.LENGTH_SHORT).show()
        } else {
            //启动相机
            startCamera()
        }
    }

    private var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>? = null

    private var processCameraProvider: ProcessCameraProvider? = null

    private var cameraState = CameraSelector.LENS_FACING_FRONT

    private fun startCamera() {
        // 请求 CameraProvider

        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        //检查 CameraProvider 可用性，验证它能否在视图创建后成功初始化
        cameraProviderFuture?.addListener({
            try {
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture!!.get()
                bindPreview(cameraProvider)
            } catch (e: ExecutionException) {
                // No errors need to be handled for this Future.
                // This should never be reached.
            } catch (e: InterruptedException) {
            }
        }, ContextCompat.getMainExecutor(this))
    }

    //选择相机并绑定生命周期和用例
    private fun bindPreview(cameraProvider: ProcessCameraProvider) {
        processCameraProvider = cameraProvider
        val preview = Preview.Builder()
            .build()

        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(cameraState)
            .build()

        preview.setSurfaceProvider(binding.cameraPreview.getSurfaceProvider())
        cameraProvider.unbindAll()
        val camera: Camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview)
        // camera.cameraControl.
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 11) { //获取权限后，开启摄像头
            //启动相机
            startCamera()
        }
    }

    override fun onBackPressed() {
        com.weini.maogou.utils.VibrateTool.vibrateStop()
        releaseVideo()
        super.onBackPressed()
    }

    private fun playVideo() {
        player = ExoPlayerFactory.newSimpleInstance(this, DefaultTrackSelector(), DefaultLoadControl())

        player?.playWhenReady = true
        player?.repeatMode = SimpleExoPlayer.REPEAT_MODE_ONE
        binding.playerView.player = player
        val dataSourceFactory = DataSource.Factory { AssetDataSource(this@WHCallActivity) }
        val uri = Uri.parse(path)
        val videoSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri)
        // 播放
        player?.prepare(videoSource)
    }

    private fun releaseVideo() {
        player?.stop()
        player?.release()
        player = null
    }

    override fun onDestroy() {
        releaseVideo()
        processCameraProvider?.unbindAll()
        super.onDestroy()
    }

}