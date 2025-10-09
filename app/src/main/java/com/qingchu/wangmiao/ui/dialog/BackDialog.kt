package com.qingchu.wangmiao.ui.dialog

import android.view.View
import com.qingchu.wangmiao.R
import com.qingchu.wangmiao.base.dj.BaseDialog
import com.qingchu.wangmiao.databinding.DialogBackBinding
import com.qingchu.wangmiao.ext.getBinding
import com.qingchu.wangmiao.utils.lzy.LZYADSUtils

class BackDialog() : BaseDialog() {

    override fun getLayoutId() = R.layout.dialog_back

    private var _binding: DialogBackBinding? = null
    private val binding get() = requireNotNull(_binding) { "The property of binding has been destroyed." }


    override fun initView(view: View) {
        _binding = view.getBinding()
        isCancelable = false
        LZYADSUtils("BackDialog",requireActivity()).loadSimpleAdTurn(binding.feedContainerDialogBack,280)
    }

}