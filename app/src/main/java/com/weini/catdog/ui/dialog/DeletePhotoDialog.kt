package com.weini.catdog.ui.dialog

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.view.View
import com.bumptech.glide.Glide
import com.weini.catdog.R
import com.weini.catdog.databinding.DialogUploadPhotoBinding
import com.weini.catdog.model.Pet
import com.weini.catdog.utils.ToastUtils
import com.lxj.xpopup.core.CenterPopupView
import org.litepal.LitePal
import com.github.dhaval2404.imagepicker.ImagePicker
import com.weini.catdog.csj.ZYMAllAdsUtils
import com.weini.catdog.databinding.DialogDeletePhotoBinding
import com.weini.catdog.ui.activity.MainActivity
import com.weini.catdog.widget.popup.dj.ExitDialogPopup

/**
 * 上传照片对话框
 */
class DeletePhotoDialog(
    activity: Activity
) : CenterPopupView(activity) {

    private lateinit var binding: DialogDeletePhotoBinding


    var listener: OnOperatorListener? = null

    override fun getImplLayoutId(): Int {
        return R.layout.dialog_delete_photo
    }

    override fun onCreate() {
        super.onCreate()
        binding = DialogDeletePhotoBinding.bind(popupImplView)

        binding.btnCancel.setOnClickListener({
            dismiss()
            listener?.cancel()
        })

        binding.btnDelete.setOnClickListener({
            dismiss()
            listener?.ok()
        })

    }


    override fun onShow() {
        super.onShow()
        ZYMAllAdsUtils.loadSimpleAll(activity,"信息",binding.feedContainerDialogExit)
    }


    interface OnOperatorListener {
        fun cancel()
        fun ok()
    }



}