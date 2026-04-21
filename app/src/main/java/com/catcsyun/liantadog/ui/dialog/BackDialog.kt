package com.catcsyun.liantadog.ui.dialog

import android.view.View
import com.catcsyun.liantadog.R
import com.catcsyun.liantadog.base.dj.BaseDialog
import com.catcsyun.liantadog.csj.ZYMAllAdsUtils
import com.catcsyun.liantadog.databinding.DialogBackBinding
import com.catcsyun.liantadog.ext.getBinding


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