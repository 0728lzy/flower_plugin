package com.ruite.app.pet.translator.ui.dialog

import android.view.View
import com.ruite.app.pet.translator.R
import com.ruite.app.pet.translator.base.BaseDialog
import com.ruite.app.pet.translator.databinding.DialogBackBinding
import com.ruite.app.pet.translator.ext.getBinding

class BackDialog() : BaseDialog() {

    override fun getLayoutId() = R.layout.dialog_back

    private var _binding: DialogBackBinding? = null
    private val binding get() = requireNotNull(_binding) { "The property of binding has been destroyed." }



    override fun initView(view: View) {
        _binding = view.getBinding()
        isCancelable = false
    }

}