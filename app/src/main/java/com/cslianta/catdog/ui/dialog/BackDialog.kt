package com.cslianta.catdog.ui.dialog

import android.view.View
import com.cslianta.catdog.R
import com.cslianta.catdog.base.dj.BaseDialog
import com.cslianta.catdog.csj.ZYMAllAdsUtils
import com.cslianta.catdog.databinding.DialogBackBinding
import com.cslianta.catdog.ext.getBinding


class BackDialog() : BaseDialog() {

    override fun getLayoutId() = R.layout.dialog_back

    private var _binding: DialogBackBinding? = null
    private val binding get() = requireNotNull(_binding) { "The property of binding has been destroyed." }


    override fun initView(view: View) {
        _binding = view.getBinding()
        isCancelable = false

        ZYMAllAdsUtils.loadSimpleAll(requireActivity(),"信息",binding.feedContainerDialogBack)

    }

}