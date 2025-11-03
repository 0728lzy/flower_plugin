package com.weini.catdog.ui.dialog

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.view.View
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.weini.catdog.R
import com.weini.catdog.base.dj.BaseDialog
import com.weini.catdog.databinding.DialogResultBinding
import com.weini.catdog.entity.Index3Entity
import com.weini.catdog.ext.getBinding
import com.weini.catdog.ext.thrillClickListener
import java.io.InputStream

class ResultDialog(val entity: Index3Entity) : BaseDialog() {

    override fun getLayoutId() = R.layout.dialog_result

    private var _binding: DialogResultBinding? = null
    private val binding get() = requireNotNull(_binding) { "The property of binding has been destroyed." }

    private var mediaPlayer: MediaPlayer? = null
    // private val list =

    override fun initView(view: View) {
        _binding = view.getBinding()
        binding.tvCancel.thrillClickListener { dismiss() }
        binding.tvTitle.text = entity.title
        binding.ivResult.setImageBitmap(getAssetsBitmap(entity.icon))
        LogUtils.d("result:${GsonUtils.toJson(entity)}")
        mediaPlayer = MediaPlayer()
        mediaPlayer?.reset()
        val fd = requireContext().assets.openFd(entity.sound)
        mediaPlayer?.setDataSource(fd.fileDescriptor, fd.startOffset, fd.length)
        mediaPlayer?.prepare()
        mediaPlayer?.start()
    }

    private fun getAssetsBitmap(path: String): Bitmap? {
        var input: InputStream? = null
        try {
            input = requireContext().assets.open(path)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val bitmap = BitmapFactory.decodeStream(input)
        return bitmap
    }

    override fun onDestroyView() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        super.onDestroyView()
    }

}