package com.pet.translator.ui.dialog

import android.view.View
import com.pet.translator.R
import com.pet.translator.base.dj.BaseDialog
import com.pet.translator.databinding.DialogBackBinding
import com.pet.translator.ext.getBinding

class BackDialog() : BaseDialog() {

    override fun getLayoutId() = R.layout.dialog_back

    private var _binding: DialogBackBinding? = null
    private val binding get() = requireNotNull(_binding) { "The property of binding has been destroyed." }



    override fun initView(view: View) {
        _binding = view.getBinding()
        isCancelable = false
    }

}