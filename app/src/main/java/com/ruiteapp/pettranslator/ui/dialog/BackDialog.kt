package com.ruiteapp.pettranslator.ui.dialog

import android.view.View
import com.ruiteapp.pettranslator.R
import com.ruiteapp.pettranslator.base.dj.BaseDialog
import com.ruiteapp.pettranslator.databinding.DialogBackBinding
import com.ruiteapp.pettranslator.ext.getBinding
import com.ruiteapp.pettranslator.utils.lzy.LZYADSUtils

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