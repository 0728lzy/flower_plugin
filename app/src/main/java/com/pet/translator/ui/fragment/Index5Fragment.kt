package com.pet.translator.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.pet.translator.R
import com.pet.translator.base.dj.RootFragment
import com.pet.translator.databinding.FragmentIndex5Binding
import com.pet.translator.databinding.Item5Binding
import com.pet.translator.ext.getBinding
import com.pet.translator.ext.thrillClickListener
import com.pet.translator.ui.activity.VideoActivity


class Index5Fragment : RootFragment(R.layout.fragment_index_5) {

    var _binding: FragmentIndex5Binding? = null

    val binding get() = _binding!!

    val list by lazy {  listOf(
        getString(R.string.call_1) to R.mipmap.call_1,
        getString(R.string.call_2) to R.mipmap.call_2,
        getString(R.string.call_3) to R.mipmap.call_3,
        getString(R.string.call_4) to R.mipmap.call_4,
        getString(R.string.call_5) to R.mipmap.call_5,
    ) }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        _binding = view.getBinding()

        binding.rvList.linear().setup {
            addType<Pair<String, Int>>(R.layout.item_5)
            onBind {
                getBinding<Item5Binding>().apply {
                    val item = getModel<Pair<String, Int>>()
                    ivAvatar.setImageResource(item.second)
                    tvName.text = item.first
                    root.thrillClickListener {
                        val index = list.indexOf(item) + 1
                        VideoActivity.show(requireContext(), index)
                    }
                }
            }
        }

        binding.rvList.bindingAdapter.models = list
        binding.etSearch.doAfterTextChanged {
            it?.let {
                val keyworkds = it.toString()
                binding.rvList.bindingAdapter.models =
                    list.filter { it.first.contains(keyworkds) || keyworkds.isEmpty() }
            }
        }
    }
}