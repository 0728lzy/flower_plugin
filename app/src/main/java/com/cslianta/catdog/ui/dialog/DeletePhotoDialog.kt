package com.cslianta.catdog.ui.dialog

import android.app.Activity
import com.cslianta.catdog.R
import com.lxj.xpopup.core.CenterPopupView
import com.cslianta.catdog.csj.ZYMAllAdsUtils
import com.cslianta.catdog.databinding.DialogDeletePhotoBinding

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