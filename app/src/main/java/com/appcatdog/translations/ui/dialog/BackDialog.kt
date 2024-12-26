package com.appcatdog.translations.ui.dialog

import android.view.View
import com.appcatdog.translations.R
import com.appcatdog.translations.base.dj.BaseDialog
import com.appcatdog.translations.databinding.DialogBackBinding
import com.appcatdog.translations.ext.getBinding
import com.appcatdog.translations.utils.lzy.LZYADSUtils

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