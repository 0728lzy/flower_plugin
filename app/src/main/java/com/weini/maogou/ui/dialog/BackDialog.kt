package com.weini.maogou.ui.dialog

import android.view.View
import com.weini.maogou.R
import com.weini.maogou.base.dj.BaseDialog
import com.weini.maogou.databinding.DialogBackBinding
import com.weini.maogou.ext.getBinding
import com.weini.maogou.utils.lzy.LZYADSUtils

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