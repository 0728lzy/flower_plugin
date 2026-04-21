package com.catcsyun.liantadog.ui.dialog

import android.app.Activity
import com.catcsyun.liantadog.R
import com.lxj.xpopup.core.CenterPopupView
import com.catcsyun.liantadog.csj.ZYMAllAdsUtils
import com.catcsyun.liantadog.databinding.DialogDeletePhotoBinding

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