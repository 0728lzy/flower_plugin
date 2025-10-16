package com.cslt.maogoufanyi.ui.dialog

import android.view.View
import com.cslt.maogoufanyi.R
import com.cslt.maogoufanyi.base.dj.BaseDialog
import com.cslt.maogoufanyi.csj.ZYMAllAdsUtils
import com.cslt.maogoufanyi.databinding.DialogBackBinding
import com.cslt.maogoufanyi.ext.getBinding


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